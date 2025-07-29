package com.example.tunefulmobile.ui.screen

import android.media.MediaPlayer
import android.os.Build
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tunefulmobile.data.AppleMusicSongData
import kotlinx.coroutines.launch
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tunefulmobile.R
import com.example.tunefulmobile.ui.components.AnimatedGradientText
import com.example.tunefulmobile.ui.components.AppleSongCardContent
import com.example.tunefulmobile.ui.components.BlurredBackgroundComponent
import com.example.tunefulmobile.ui.components.BottomTripleFAB
import com.example.tunefulmobile.ui.theme.Dimensions

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SongScreen(
    viewModel: AppleViewModel = hiltViewModel(),
    onFinish: (List<AppleMusicSongData>) -> Unit // pass navigation callback
) {
    var query by remember { mutableStateOf("") }
    val songs by viewModel.nextRecommendedSongs.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var dialogInput by remember { mutableStateOf("") }
    var backgroundColor by remember { mutableStateOf<Color>(Color.White)}

    LaunchedEffect(Unit) {
        viewModel.generateRecommendations()
    }
    // Outer container to support layering
    Box(modifier = Modifier.fillMaxSize()) {

        // If no songs yet, show loading
        if (songs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            viewModel.generateRecommendations()
            return@Box
        }

        val song = songs[0]
        song.attributes.artwork?.bgColor?.let { hex ->
            backgroundColor = parseHexColor(hex)
        }

        BlurredBackgroundComponent(song) {
            // This content goes inside BlurredBackgroundComponent
            SwipableCard(
                song = song,
                onSwipedLeft = {
                    val current = songs[0]
                    viewModel.addToDislikedList(current)
                    viewModel.popFromNextSongsList()
                    if (songs.size <= 2) viewModel.generateRecommendations()
                },
                onSwipedRight = {
                    val current = songs[0]
                    viewModel.addToLikedList(current)
                    viewModel.popFromNextSongsList()
                    if (songs.size <= 2) viewModel.generateRecommendations()
                }
            )
        }

        // Overlay the FABs above everything else
        BottomTripleFAB(
            onLeftClick = { showDialog = true },
            onCenterClick = { viewModel.resetSession() },
            onRightClick = {onFinish(viewModel.likedSongsList)},
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Dialog (still visible above background)
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(onClick = {
                        viewModel.searchSongs(dialogInput) {
                            viewModel.addQueryToNextSongs()
                        }
                        showDialog = false
                    }) {
                        Text(stringResource(R.string.add))
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                },
                title = { Text(stringResource(R.string.add_your_song_to_queue)) },
                text = {
                    OutlinedTextField(
                        value = dialogInput,
                        onValueChange = { dialogInput = it },
                        label = { Text(stringResource(R.string.song_name)) },
                        singleLine = true
                    )
                }
            )
        }
    }

}


@Composable
fun SwipableCard(
    song: AppleMusicSongData,
    onSwipedLeft: () -> Unit,
    onSwipedRight: () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    val screenWidth = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val mediaPlayer = remember { MediaPlayer() }
    val progress = remember { mutableFloatStateOf(0f) }
    val handler = remember { Handler(Looper.getMainLooper()) }

    // Auto-play new song when it changes
    val previewUrl = song.attributes.previews.firstOrNull()?.url
    LaunchedEffect(previewUrl) {
        mediaPlayer.reset()
        progress.floatValue = 0f
        if (previewUrl != null) {
            try {
                mediaPlayer.setDataSource(previewUrl)
                mediaPlayer.prepare()
                mediaPlayer.start()
                mediaPlayer.setOnCompletionListener {
                    progress.floatValue = 0f
                }
                handler.post(object : Runnable {
                    override fun run() {
                        if (mediaPlayer.isPlaying) {
                            progress.floatValue = mediaPlayer.currentPosition / mediaPlayer.duration.toFloat()
                            handler.postDelayed(this, 100)
                        }
                    }
                })
            } catch (e: Exception) {
                Log.e(context.getString(R.string.mediaplayer), context.getString(R.string.playback_error, e.message))
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            handler.removeCallbacksAndMessages(null)
            mediaPlayer.release()
        }
    }

    fun resetAndSwipe(action: () -> Unit) {
        mediaPlayer.stop()
        progress.floatValue = 0f
        action()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .width(300.dp)
                .wrapContentHeight()
                .graphicsLayer {
                    translationX = offsetX.value
                    rotationZ = rotation.value
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            val threshold = screenWidth * 0.25f
                            val currentOffset = offsetX.value

                            if (kotlin.math.abs(currentOffset) > threshold) {
                                val isRight = currentOffset > 0
                                val targetOffset = if (isRight) screenWidth else -screenWidth

                                coroutineScope.launch {
                                    offsetX.animateTo(targetOffset, animationSpec = tween(300))
                                    if (isRight) resetAndSwipe(onSwipedRight)
                                    else resetAndSwipe(onSwipedLeft)
                                    offsetX.snapTo(0f)
                                    rotation.snapTo(0f)
                                }
                            } else {
                                coroutineScope.launch {
                                    offsetX.animateTo(0f, animationSpec = tween(300))
                                    rotation.animateTo(0f, animationSpec = tween(300))
                                }
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            coroutineScope.launch {
                                offsetX.snapTo(offsetX.value + dragAmount.x)
                                rotation.snapTo(offsetX.value / 30)
                            }
                        }
                    )
                }
        ) {
            AppleSongCardContent(
                song = song,
                progress = progress.floatValue,
                onPlayPauseToggle = {
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.pause()
                    } else {
                        mediaPlayer.start()
                    }
                },
                onLike = { resetAndSwipe(onSwipedRight) },
                onDislike = { resetAndSwipe(onSwipedLeft) }
            )
        }
    }
}

@Composable
fun TunefulHeader(
    onFinish: () -> Unit,
    onAddQueue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8F9FD))
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AnimatedGradientText(text = "tuneful")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { onFinish() },
                shape = RoundedCornerShape(Dimensions.ButtonCornerRadius),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Finish", color = Color.White)
            }
        }
    }
}


@Preview
@Composable
fun PreviewHeader() {
    TunefulHeader({},{})
}

fun parseHexColor(hex: String): Color {
    val cleanedHex = hex.removePrefix("#")

    return when (cleanedHex.length) {
        6 -> { // RGB
            val r = cleanedHex.substring(0, 2).toInt(16)
            val g = cleanedHex.substring(2, 4).toInt(16)
            val b = cleanedHex.substring(4, 6).toInt(16)
            Color(r, g, b)
        }
        8 -> { // ARGB or RGBA (Apple uses RGBA)
            val r = cleanedHex.substring(0, 2).toInt(16)
            val g = cleanedHex.substring(2, 4).toInt(16)
            val b = cleanedHex.substring(4, 6).toInt(16)
            val a = cleanedHex.substring(6, 8).toInt(16)
            Color(r, g, b, a)
        }
        else -> Color.White // Fallback
    }
}