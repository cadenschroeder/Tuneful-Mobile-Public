package com.example.tunefulmobile.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.tunefulmobile.data.AppleMusicSongData
import com.example.tunefulmobile.navigation.LikedSongScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikedSongsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var likedSongs by mutableStateOf<List<AppleMusicSongData>>(emptyList())

//    init{
//        likedSongs = savedStateHandle.toRoute<LikedSongScreenRoute>().likedSongs
//    }
}