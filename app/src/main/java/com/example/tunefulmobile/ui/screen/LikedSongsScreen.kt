package com.example.tunefulmobile.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tunefulmobile.R
import com.example.tunefulmobile.ui.components.AppleSongItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikedSongsScreen(
    //likedSongsViewModel: LikedSongsViewModel = hiltViewModel(),
    viewModel: AppleViewModel = hiltViewModel(),
    onBack: () -> Unit
) {

    val likedSongs = viewModel.likedSongsList

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.liked_songs)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(
                            R.string.back
                        ))
                    }
                }
            )
        }
    ) { innerPadding ->
        if (likedSongs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.you_haven_t_liked_any_songs_yet))
            }
        } else {
            LazyColumn (
                modifier = Modifier.padding(innerPadding)
            ) {
                items(likedSongs) { track ->
                    AppleSongItem(track)
                }
            }
        }
    }
}