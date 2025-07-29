package com.example.tunefulmobile.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tunefulmobile.ui.theme.Dimensions

@Composable
fun AnimatedGradientText(text: String) {
    val infiniteTransition = rememberInfiniteTransition()
    val gradientOffset = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    ).value

    val colors = listOf(Color(0xFFB28900), Color(0xFFD97A00), Color(0xFFD84C2B), Color(0xFFCE0F4F))

    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(gradientOffset, 0f),
        end = Offset(gradientOffset + 300f, 0f)
    )

    Text(
        text = text,
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        style = TextStyle(brush = brush),
        fontFamily = FontFamily.SansSerif
    )
}

@Composable
fun TunefulButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Dimensions.CardCornerRadius))
            .background(
                Color.Gray
//                Brush.horizontalGradient(
//                    listOf( Color(0xFFD97A00), Color(0xFFD84C2B), Color(0xFFCE0F4F)) //Color(0xFFB28900),
//                )
            )
            .clickable(onClick = onClick)
            .padding(12.dp)
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(50))
            .wrapContentSize()
    ) {
        Text(
            text = text,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center),
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.SansSerif
        )
    }
}

@Composable
fun BottomTripleFAB(
    onLeftClick: () -> Unit,
    onCenterClick: () -> Unit,
    onRightClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Left FAB
        FloatingActionButton(
            onClick = onLeftClick,
            containerColor = Color(0xFF324772),
            modifier = Modifier
                .align(Alignment.BottomStart)
        ) {
            Icon(Icons.AutoMirrored.Filled.PlaylistAdd, contentDescription = null, tint = Color.White)        }

        // Center FAB
        FloatingActionButton(
            onClick = onCenterClick,
            containerColor = Color(0xFF324772),
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Icon(Icons.Default.RestartAlt, contentDescription = "Center", tint = Color.White)
        }

        // Right FAB
        FloatingActionButton(
            onClick = onRightClick,
            containerColor = Color(0xFF324772),
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Right", tint = Color.White)
        }
    }
}

@Preview
@Composable
fun PreviewGradientText(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
//        AnimatedGradientText(text = "tuneful")
        TunefulButton("test", {})
    }
//    Box(modifier = Modifier.fillMaxSize()) {
//        // Your card UI and blurred background here
//
//        BottomTripleFAB(
//            onLeftClick = { /* Dislike */ },
//            onCenterClick = { /* Play/Pause */ },
//            onRightClick = { /* Like */ }
//        )
//    }
}

