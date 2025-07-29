package com.example.tunefulmobile.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tunefulmobile.R
import com.example.tunefulmobile.ui.components.AnimatedGradientText
import com.example.tunefulmobile.ui.components.TunefulButton

@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    onDiscoverClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedGradientText(text = "tuneful")


        TunefulButton(
            onClick = onSearchClick,
            modifier = Modifier
//                .fillMaxWidth()
                .padding(vertical = 8.dp),
            text = stringResource(R.string.search_songs)
        )
            TunefulButton(
                onClick = onDiscoverClick,
            modifier = Modifier
//                .fillMaxWidth()
                .padding(vertical = 8.dp),
                text = stringResource(R.string.discover)
            )

    }
}

@Preview
@Composable
fun PreviewHomeScreen(){
    HomeScreen({},{})
}