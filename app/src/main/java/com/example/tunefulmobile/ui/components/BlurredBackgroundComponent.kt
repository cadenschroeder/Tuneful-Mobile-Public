package com.example.tunefulmobile.ui.components

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tunefulmobile.R
import com.example.tunefulmobile.data.AppleMusicSongData

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BlurredBackgroundComponent(
    song: AppleMusicSongData,
    foregroundContent: @Composable () -> Unit
) {

    val artwork = song.attributes.artwork
    val imageUrl = artwork?.url
        ?.replace(stringResource(R.string.w), artwork.width.toString())
        ?.replace(stringResource(R.string.h), artwork.height.toString())
    val transition = updateTransition(targetState = imageUrl, label = "ImageTransition")

    Box(modifier = Modifier.fillMaxSize()) {
        transition.AnimatedContent(
            transitionSpec = {
                fadeIn(animationSpec = tween(600)) togetherWith
                        fadeOut(animationSpec = tween(600))
            },
//            label = "BlurredImage"
        ) { url ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        renderEffect = RenderEffect.createBlurEffect(
                            70f, 70f, Shader.TileMode.CLAMP
                        ).asComposeRenderEffect()
                    },
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            foregroundContent()
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.S)
//@Preview
//@Composable
//fun PreviewBlurredBackground(){
//    val images = listOf(
//        painterResource(R.drawable.one_of_wun),
//        painterResource(R.drawable.music_logo)
//    )
//    var index by remember { mutableStateOf(0) }
//
//    BlurredBackgroundComponent(
//        currentImage = images[index],
//        foregroundContent = {
//            Text("Your foreground content here", color = Color.White)
//            Spacer(Modifier.height(16.dp))
//            Button(onClick = { index = (index + 1) % images.size }) {
//                Text("Next Image")
//            }
//        }
//    )
//}
