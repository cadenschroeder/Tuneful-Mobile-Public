package com.example.tunefulmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun AppleSongCardContent(
    song: AppleMusicSongData,
    progress: Float,
    onPlayPauseToggle: () -> Unit,
    onLike: () -> Unit,
    onDislike: () -> Unit
) {
    val artwork = song.attributes.artwork
    val imageUrl = artwork?.url
        ?.replace(stringResource(R.string.w), artwork.width.toString())
        ?.replace(stringResource(R.string.h), artwork.height.toString())
    var isPlaying = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(Dimensions.CardCornerRadius))
            .padding(16.dp)
    ) {
        Text(
            text = song.attributes.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        imageUrl?.let {
            AsyncImage(
                model = it,
                contentDescription = stringResource(R.string.album_cover),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(Dimensions.CardCornerRadius))
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = song.attributes.artistName,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = song.attributes.albumName,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50)),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onDislike) {
                Icon(Icons.Default.Close, contentDescription = stringResource(R.string.dislike), tint = Color.Red)
            }

            IconButton(onClick = {
                onPlayPauseToggle()
                isPlaying.value = !isPlaying.value
            })
            {
                Icon(
                    imageVector = if(isPlaying.value) Icons.Default.Pause else Icons.Default.PlayArrow, // Will still pause/resume
                    contentDescription = stringResource(R.string.play_pause),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onLike) {
                Icon(Icons.Default.Favorite, contentDescription = stringResource(R.string.like), tint = Color.Green)
            }
        }
    }
}

@Preview
@Composable
fun PreviewSongCard(){
    val mockSong = AppleMusicSongData(
        id = "1200868882",
        attributes =
            AppleMusicSongAttributes(name="One of Wun", artistName="Gunna", albumName="One of Wun", artwork=Artwork(
                width = 1500,
                height = 1500,
                url = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/f8/45/5a/f8455a71-8307-aa9a-9c95-3d22efe0804f/886446326146.jpg/{w}x{h}bb.jpg",
                bgColor = ""
            ), previews=listOf(AppleMusicPreview(url="https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview112/v4/85/59/2f/85592f16-bf87-c534-ff1b-7a952be0fedf/mzaf_1038687697803928956.plus.aac.p.m4a")))
    )
    AppleSongCardContent(mockSong, 0.toFloat(), {},{},{})

}