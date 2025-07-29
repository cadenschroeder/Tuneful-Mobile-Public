package com.example.tunefulmobile.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tunefulmobile.data.Track
import com.example.tunefulmobile.network.SpotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotifyViewModel @Inject constructor(
    private val spotifyRepository: SpotifyRepository
) : ViewModel() {

    private var accessToken: String? = null
    private var tokenExpiryTime: Long = 0L // Epoch time in ms

    private val _searchResults = MutableStateFlow<List<Track>>(emptyList())
    val searchResults: StateFlow<List<Track>> = _searchResults

    fun searchSong(query: String) {
        viewModelScope.launch {
            val token = getValidAccessToken()
            val results = spotifyRepository.searchTracks(query, token)
            _searchResults.value = results
        }
    }

    private suspend fun getValidAccessToken(): String {
        val currentTime = System.currentTimeMillis()
        if (accessToken == null || currentTime >= tokenExpiryTime) {
            val (newToken, expiresInSec) = spotifyRepository.fetchAccessToken()
            accessToken = newToken
            tokenExpiryTime = currentTime + expiresInSec * 1000
        }
        return accessToken!!
    }
}