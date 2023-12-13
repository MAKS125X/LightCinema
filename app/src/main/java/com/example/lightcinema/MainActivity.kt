package com.example.lightcinema

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.lightcinema.ui.common.LightCinemaScaffold
import com.example.lightcinema.ui.navigation.AppState
import com.example.lightcinema.ui.navigation.MainDestinations
import com.example.lightcinema.ui.screens.authorization.AuthScreen
import com.example.lightcinema.ui.screens.cinemahall.CinemaScreen
import com.example.lightcinema.ui.screens.movie_info.MovieInfoScreen
import com.example.lightcinema.ui.screens.poster.PosterScreen
import com.example.lightcinema.ui.screens.profile.ProfileScreen
import com.example.lightcinema.ui.theme.LightCinemaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            LightCinemaTheme {
                val appState = rememberAppState()

                LightCinemaScaffold(
                    showTopAppBar = appState.shouldShowTopAppBar,
                    onProfileClick = appState::navigateToProfileScreen,
                    onLogoutClick = appState::navigateToAuth
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {
//                        SeatsModelCollectionPreview()
                        NavHost(
                            navController = appState.navController,
                            startDestination = MainDestinations.AUTH,
                        ) {
                            navGraph(
                                onSuccess = appState::navigateToVisitorModule,
                                onMovieClick = appState::navigateToMovieInfo,
                                profileUpPress = appState::upPress,
                                onSessionClick = appState::navigateToSessionsScreen
                            )
                        }
                    }

                }
            }
        }
    }

    @Composable
    fun rememberAppState(
        navController: NavHostController = rememberNavController()
    ) = remember(navController) {
        AppState(navController)
    }

    private fun NavGraphBuilder.navGraph(
        onSuccess: (NavBackStackEntry) -> Unit,
        onMovieClick: (Int, NavBackStackEntry) -> Unit,
        onSessionClick: (Int, Int, NavBackStackEntry) -> Unit,
        profileUpPress: () -> Unit,
    ) {
        addAuthGraph(onSuccess = onSuccess)
        addVisitorGraph(
            onMovieClick = onMovieClick,
            profileUpPress = { profileUpPress() },
            onSessionClick = onSessionClick
        )
    }

    private fun NavGraphBuilder.addAuthGraph(onSuccess: (NavBackStackEntry) -> Unit) {
        composable(
            route = MainDestinations.AUTH,
        ) {
            AuthScreen(onSuccess = {
                onSuccess(it)
            }
            )
        }
    }

    private fun NavGraphBuilder.addVisitorGraph(
        onMovieClick: (Int, NavBackStackEntry) -> Unit,
        onSessionClick: (Int, Int, NavBackStackEntry) -> Unit,
        profileUpPress: () -> Unit,
    ) {
        navigation(
            route = MainDestinations.VISITOR_ROUTE,
            startDestination = MainDestinations.POSTER_ROUTE
        ) {

            composable(MainDestinations.POSTER_ROUTE) {
                PosterScreen(
                    onMovieClick = { id -> onMovieClick(id, it) },
                )
            }
            composable(
                "${MainDestinations.POSTER_ROUTE}/{${MainDestinations.MOVIE_INFO}}",
                arguments = listOf(navArgument(MainDestinations.MOVIE_INFO) {
                    type = NavType.IntType
                })
            ) {
                MovieInfoScreen(onSessionClick = { movieId, sessionId ->
                    onSessionClick(
                        movieId,
                        sessionId,
                        it
                    )
                })
            }
            composable("${MainDestinations.VISITOR_ROUTE}/${MainDestinations.PROFILE}") {
                ProfileScreen(
                    upPress = { profileUpPress() }
                )
            }
            composable(
                "${MainDestinations.POSTER_ROUTE}/{${MainDestinations.MOVIE_INFO}}/{${MainDestinations.SESSIONS}}",
                arguments = listOf(
                    navArgument(
                        MainDestinations.MOVIE_INFO,
                        ) {
                        type = NavType.IntType
                    },
                    navArgument(MainDestinations.SESSIONS) {
                        type = NavType.IntType
                    })
            ) {
                CinemaScreen(
//                    upPress = { profileUpPress() }
                )
            }
        }
    }
}


