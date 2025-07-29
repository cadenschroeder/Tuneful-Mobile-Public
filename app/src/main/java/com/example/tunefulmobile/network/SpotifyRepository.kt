package com.example.tunefulmobile.network

import android.content.Context
import android.util.Base64
import com.example.tunefulmobile.R
import com.example.tunefulmobile.data.Track
import com.example.tunefulmobile.secrets.ApiKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

class SpotifyRepository @Inject constructor(
    private val spotifyAPI: SpotifyAPI,
    @ApplicationContext private val context: Context
) {
    suspend fun fetchAccessToken(): Pair<String, Long> = withContext(Dispatchers.IO) {
        val clientId = ApiKeys.SPOTIFY_CLIENT_ID
        val clientSecret = ApiKeys.SPOTIFY_CLIENT_SECRET
        val credentials = "$clientId:$clientSecret"
        val encoded = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        val client = OkHttpClient()
        val requestBody = context.getString(R.string.grant_type_client_credentials)
            .toRequestBody(context.getString(R.string.application_x_www_form_urlencoded).toMediaType())

        val request = Request.Builder()
            .url(context.getString(R.string.https_accounts_spotify_com_api_token))
            .addHeader(context.getString(R.string.authorization),
                context.getString(R.string.basic, encoded))
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException(
                context.getString(
                    R.string.unexpected_code,
                    response
                ))

            val json = JSONObject(response.body?.string() ?: "")
            val token = json.getString(context.getString(R.string.access_token))
            val expiresIn = json.getLong(context.getString(R.string.expires_in)) // seconds
            token to expiresIn
        }
    }

    suspend fun searchTracks(query: String, token: String): List<Track> {
        return spotifyAPI.searchTracks(context.getString(R.string.bearer, token), query).tracks.items
    }
}