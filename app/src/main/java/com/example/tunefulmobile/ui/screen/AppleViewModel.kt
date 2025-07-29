package com.example.tunefulmobile.ui.screen

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunefulmobile.data.AppleMusicSongData
import com.example.tunefulmobile.network.AppleMusicAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.tunefulmobile.data.SongRecommendation
import com.example.tunefulmobile.secrets.ApiKeys
import com.example.tunefulmobile.utility.GenerativeAiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.interfaces.ECPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Instant
import java.util.*

@HiltViewModel
class AppleViewModel @Inject constructor(
    private val api: AppleMusicAPI
) : ViewModel() {

    private val _songs = MutableStateFlow<List<AppleMusicSongData>>(emptyList())
    val songs: StateFlow<List<AppleMusicSongData>> = _songs
    private var tokenExpiryTime: Long = 0L
    private var accessToken: String? = null

    private val _nextRecommendedSongs = MutableStateFlow<List<AppleMusicSongData>>(emptyList())
    val nextRecommendedSongs: StateFlow<List<AppleMusicSongData>> = _nextRecommendedSongs
    val likedSongsList = mutableStateListOf<AppleMusicSongData>()//by remember( mutableStateOf())
    private var dislikedSongsList = mutableListOf<AppleMusicSongData>()

    private var allSongsRecommended = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun searchSongs(query: String, onFinish: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val token = generateAppleMusicDeveloperToken(
                    teamId = "9QK23RZ5YS",
                    keyId = "296HFKAR75",
                )
                val response = api.searchSongs(
                    authHeader = "Bearer $token", //"Bearer $developerToken",
                    term = query
                )
                _songs.value = response.results.songs.data
                onFinish()
            } catch (e: Exception) {
                e.printStackTrace()
                _songs.value = emptyList()
            }
        }
    }

    fun addQueryToNextSongs(){
        if (_songs.value .isNotEmpty()) {
            _nextRecommendedSongs.value = _nextRecommendedSongs.value + _songs.value .first()
        }
    }

    fun addToDislikedList(songData: AppleMusicSongData){
        dislikedSongsList.add(songData)
    }

    fun addToLikedList(songData: AppleMusicSongData){
        likedSongsList.add(songData)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun resetSession(){
        allSongsRecommended.clear()
        _nextRecommendedSongs.value = emptyList<AppleMusicSongData>()
        likedSongsList.clear()
        dislikedSongsList.clear()
        _songs.value = emptyList<AppleMusicSongData>()
        generateRecommendations()
    }

    fun popFromNextSongsList(): AppleMusicSongData? {
        val currentList = _nextRecommendedSongs.value
        return if (currentList.isNotEmpty()) {
            val firstSong = currentList.first()
            _nextRecommendedSongs.value = currentList.drop(1)
            firstSong
        } else {
            null
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun generateAppleMusicDeveloperToken(
        teamId: String,
        keyId: String,
    ): String {
        val currentTime = System.currentTimeMillis()
        if (accessToken == null || currentTime >= tokenExpiryTime) {
            val privateKeyContent = ApiKeys.RAW_APPLE_DEV_KEY
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\\s".toRegex(), "")

            val keyBytes = Base64.getDecoder().decode(privateKeyContent)
            val keySpec = PKCS8EncodedKeySpec(keyBytes)
            val kf = KeyFactory.getInstance("EC")
            val privateKey = kf.generatePrivate(keySpec) as ECPrivateKey

            val now = Instant.now()
            val exp = now.plusSeconds(60 * 60 * 12) // 12 hours
            tokenExpiryTime = currentTime + (60 * 60 * 12) * 1000

            accessToken = JWT.create()
                .withIssuer(teamId)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .withKeyId(keyId)
                .sign(Algorithm.ECDSA256(null, privateKey))
        }
        return accessToken!!
    }

    private val aiRepository = GenerativeAiRepository()
    @RequiresApi(Build.VERSION_CODES.O)
    fun generateRecommendations() {
        var prompt = """"""
        if (likedSongsList.isEmpty() && dislikedSongsList.isEmpty()) { // balancing between well-known and lesser-known tracks
            prompt = """
                Recommend 5 unique, random songs from a variety of genres.
                Do not repeat any songs.
                ${if(allSongsRecommended.isEmpty()) "" else "Do not recommend any songs from: ${allSongsRecommended.joinToString(", ")}"}

Return your answer strictly in the following JSON format:
[
  {"title": "Song Title 1", "artist": "Artist Name 1"},
  {"title": "Song Title 2", "artist": "Artist Name 2"},
  {"title": "Song Title 3", "artist": "Artist Name 3"},
  {"title": "Song Title 4", "artist": "Artist Name 4"},
  {"title": "Song Title 5", "artist": "Artist Name 5"}
]

Do not include any explanation, commentary, or text outside the JSON.
            """.trimIndent()
        }

        else {
            val likedSongsString =
                likedSongsList.joinToString(", ") { "${it.attributes.name} by ${it.attributes.artistName}" }
            val dislikedSongsString =
                dislikedSongsList.joinToString(", ") { "${it.attributes.name} by ${it.attributes.artistName}" }
            prompt = """
            Given that I like the following songs:
            $likedSongsString
            and dislike the following songs:
            $dislikedSongsString

            Recommend exactly 3 songs that I am likely to enjoy.
            Do not recommend any songs that are already in the liked or disliked lists.
            Recommend each of the 3 songs from a different artist.

            Return your answer strictly in the following JSON format:
            [
              {"title": "Song Title 1", "artist": "Artist Name 1"},
              {"title": "Song Title 2", "artist": "Artist Name 2"},
              {"title": "Song Title 3", "artist": "Artist Name 3"}
            ]

            Do not include any explanation, commentary, or text outside the JSON.
        """.trimIndent()
        }
        viewModelScope.launch(Dispatchers.IO) {
            val result = aiRepository.generateContent(prompt)

            try {
                val cleanJson = result
                    .replace("```json", "")
                    .replace("```", "")
                    .trim()
                val recommendations = Json.decodeFromString<List<SongRecommendation>>(cleanJson)
                for (recommendation in recommendations) {
                    val titleAndArtist = "${recommendation.title} ${recommendation.artist}"
                    if(!allSongsRecommended.contains(titleAndArtist)){
                        allSongsRecommended.add(titleAndArtist)
                        searchSongs(titleAndArtist, {addQueryToNextSongs()})
                    }

                }
            } catch (e: Exception) {
                // Handle invalid or non-JSON output from AI
                Log.e("AIParser", "Failed to parse recommendations", e)
            }
        }
    }
}