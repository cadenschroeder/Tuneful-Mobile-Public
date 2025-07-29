package com.example.tunefulmobile.ui.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunefulmobile.data.AppleMusicSongData
import com.example.tunefulmobile.secrets.ApiKeys
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(
//    val moneyAPI: MoneyAPI
) : ViewModel() {
    private val generativeModel = GenerativeModel(
        modelName =  "gemini-2.0-flash", //models/gemini-1.5-flash
        apiKey = ApiKeys.ANDROID_STUDIO_KEY
    )

    private val _textGenerationResult = MutableStateFlow<String?>(null)
    val textGenerationResult = _textGenerationResult.asStateFlow()

    var likedSongsList = mutableListOf<String>()//by remember( mutableStateOf())
    var dislikedSongsList = mutableListOf<String>()

    var nextRecommendedSongs = mutableStateListOf<AppleMusicSongData>() //TODO: make a flow?
    //TODO: when this is empty should show a loading and call for more

    fun addToDislikedList(songName: String){
        dislikedSongsList.add(songName)
    }

    fun addToLikedList(songName: String){
        likedSongsList.add(songName)
    }

    fun popFromNextSongsList(): AppleMusicSongData? {
        if (!nextRecommendedSongs.isEmpty()){
            val firstSong = nextRecommendedSongs[0]
            nextRecommendedSongs.remove(firstSong)
            return firstSong
        }
        return null
    }

    fun fetchRecommendedSongs(songNames: List<String>){
        //TODO: use the spotify API to search for the songs

    }

    fun queryGeminiForRecommendations(){
        //TODO: use the GenAIViewModel along with the the liked and disliked lists to get more
        _textGenerationResult.value = "Generating recommendations..."

        val prompt = "" //TODO: engineer this prompt
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = generativeModel.generateContent(prompt)
                //TODO: parse the text result as a JSON (list of song names)
                _textGenerationResult.value = result.text
            } catch (e: Exception) {
                //TODO: try again?
                _textGenerationResult.value = "Error: ${e.message}"
            }
        }
    }

}