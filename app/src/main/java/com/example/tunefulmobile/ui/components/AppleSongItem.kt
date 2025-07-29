package com.example.tunefulmobile.ui.components

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tunefulmobile.R
import com.example.tunefulmobile.data.AppleMusicPreview
import com.example.tunefulmobile.data.AppleMusicSongAttributes
import com.example.tunefulmobile.data.AppleMusicSongData
import com.example.tunefulmobile.data.Artwork
import com.example.tunefulmobile.ui.theme.Dimensions

@Composable
fun AppleSongItem(song: AppleMusicSongData) {
    var isPlaying by remember { mutableStateOf(false) }
    val context = LocalContext.current
//    val mediaPlayer = remember { MediaPlayer() }
    val isInPreview = LocalInspectionMode.current
    val mediaPlayer = remember {
        if (!isInPreview) MediaPlayer() else null
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val artwork = song.attributes.artwork
        if (artwork != null) {
                if (isInPreview) {
                    // Placeholder image
                    Image(
                        painter = painterResource(id = R.drawable.one_of_wun),
                        contentDescription = "Album Cover",
                        modifier = Modifier.size(64.dp)
                            .clip(
                                RoundedCornerShape(Dimensions.CardCornerRadius)
                            )
                        )
                } else {
                    val imageUrl = artwork.url
                        .replace("{w}", artwork.width.toString())
                        .replace("{h}", artwork.height.toString())
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = stringResource(R.string.album_cover),
                        modifier = Modifier.size(64.dp)
                            .clip(
                                RoundedCornerShape(Dimensions.CardCornerRadius)
                            )
                    )
                }
        }



        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                song.attributes.name,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                song.attributes.artistName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(song.attributes.albumName,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)
        }

        // Preview button if URL is available
        val previewUrl = song.attributes.previews.firstOrNull()?.url
        if (previewUrl != null) {
            IconButton(onClick = {
                if (isInPreview) {

                } else {
                    if (!isPlaying) {
                        try {
                            mediaPlayer?.apply {
                                reset()
                                setDataSource(previewUrl)
                                prepare()
                                start()
                            }
                            isPlaying = true

                            mediaPlayer?.setOnCompletionListener {
                                isPlaying = false
                            }
                        } catch (e: Exception) {
                            Log.e(
                                context.getString(R.string.mediaplayer),
                                context.getString(R.string.error_playing_preview, e.message)
                            )
                            isPlaying = false
                        }
                    } else {
                        mediaPlayer?.stop()
                        isPlaying = false
                    }
                }
            },
                modifier = Modifier.size(64.dp)) {
                Icon(
                    imageVector = if(isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, //TODO: or switch to music note (audio track)
                    contentDescription = stringResource(R.string.play_pause),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            Icon(
                imageVector = Icons.Default.Block, // Will still pause/resume
                contentDescription = stringResource(R.string.preview_not_available),
                tint = MaterialTheme.colorScheme.primary
            ) //TODO: give alert that not availible
        }
    }
}

@Preview
@Composable
fun PreviewComponent(){
    val mockSong = AppleMusicSongData(
        id = "1200868882",
        attributes =
            AppleMusicSongAttributes(name="One of Wun", artistName="Gunna", albumName="American Teen", artwork=Artwork(
                width = 1500,
                height = 1500,
                url = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/f8/45/5a/f8455a71-8307-aa9a-9c95-3d22efe0804f/886446326146.jpg/{w}x{h}bb.jpg",
                bgColor = ""
            ), previews=listOf(AppleMusicPreview(url="https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview112/v4/85/59/2f/85592f16-bf87-c534-ff1b-7a952be0fedf/mzaf_1038687697803928956.plus.aac.p.m4a")))
    )
    AppleSongItem(mockSong)

}