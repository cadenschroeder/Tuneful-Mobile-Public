package com.example.tunefulmobile

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tunefulmobile.navigation.HomeScreenRoute
import com.example.tunefulmobile.navigation.LikedSongScreenRoute
import com.example.tunefulmobile.navigation.SearchScreenRoute
import com.example.tunefulmobile.navigation.SongScreenRoute
import com.example.tunefulmobile.navigation.SplashScreenRoute
import com.example.tunefulmobile.ui.components.BlurredBackgroundComponent
import com.example.tunefulmobile.ui.components.PreviewGradientText
import com.example.tunefulmobile.ui.screen.AppleSearchScreen
import com.example.tunefulmobile.ui.screen.AppleViewModel
import com.example.tunefulmobile.ui.screen.HomeScreen
import com.example.tunefulmobile.ui.screen.LikedSongsScreen
import com.example.tunefulmobile.ui.screen.SongScreen
import com.example.tunefulmobile.ui.screen.SplashScreen
import com.example.tunefulmobile.ui.theme.TunefulMobileTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TunefulMobileTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavigation(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
)
{
    val sharedViewModel: AppleViewModel = hiltViewModel()
    NavHost (
        modifier = modifier,
        navController = navController,
        startDestination = SplashScreenRoute
    )
    {

        composable<SongScreenRoute> {

            SongScreen(onFinish = {
                likedSongs ->
                navController.navigate(LikedSongScreenRoute)
            }, viewModel = sharedViewModel)
        }
        composable<LikedSongScreenRoute> {
            LikedSongsScreen(onBack = {navController.popBackStack()}, viewModel = sharedViewModel)
        }

        composable<SearchScreenRoute> {
            AppleSearchScreen(
                onBack = {navController.popBackStack()}
            )
        }
        
        composable<HomeScreenRoute> {
            HomeScreen(
                onSearchClick = { navController.navigate(SearchScreenRoute) },
                onDiscoverClick = { navController.navigate(SongScreenRoute)}
            )
//            PreviewGradientText()
        }

        composable<SplashScreenRoute> {
            SplashScreen(
                onFinished = {
                    navController.navigate(HomeScreenRoute)
                }
            )
        }

    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TunefulMobileTheme {
        Greeting("Android")
    }
}