package com.example.tunefulmobile.ui.screen

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.tunefulmobile.data.Track
import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.ui.res.stringResource
import com.example.tunefulmobile.R

@Composable
fun SpotifySearchScreen(
    viewModel: SpotifyViewModel = hiltViewModel()
) {
    val searchResults by viewModel.searchResults.collectAsState()
    var query by remember { mutableStateOf("") }


    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text(stringResource(R.string.search_song)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.searchSong(query)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(R.string.search))
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(searchResults) { track ->
                SongItem(track)
            }
        }
    }
}

@Composable
fun SongItem(track: Track) {
    var isPlaying by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val imageUrl = track.album.images.firstOrNull()?.url
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = stringResource(R.string.album_cover),
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(track.name, fontWeight = FontWeight.Bold)
            Text(track.artists.joinToString(", ") { it.name })
            Text(track.album.name, style = MaterialTheme.typography.bodySmall)
        }

        // Preview button if URL is available
        if (track.previewUrl != null) {
            Button(onClick = {
                if (!isPlaying) {
                    try {
                        mediaPlayer.reset()
                        mediaPlayer.setDataSource(track.previewUrl)
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                        isPlaying = true

                        mediaPlayer.setOnCompletionListener {
                            isPlaying = false
                        }
                    } catch (e: Exception) {
                        Log.e(context.getString(R.string.mediaplayer),
                            context.getString(R.string.error_playing_preview, e.message))
                        isPlaying = false
                    }
                } else {
                    mediaPlayer.stop()
                    isPlaying = false
                }
            }) {
                Text(if (isPlaying) stringResource(R.string.stop) else stringResource(R.string.play_preview))
            }
        } else {
            Text(stringResource(R.string.nope))
        }
    }
}