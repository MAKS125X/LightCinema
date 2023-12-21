package com.example.lightcinema.ui

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
import com.example.lightcinema.ui.screens.visitor.movie_info.MovieInfoScreen
import com.example.lightcinema.ui.screens.visitor.poster.PosterScreen
import com.example.lightcinema.ui.screens.visitor.profile.ProfileScreen
import com.example.lightcinema.ui.screens.visitor.reserving_screen.ReservingScreen
import com.example.lightcinema.ui.screens.visitor.reserving_screen.SuccessScreen
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
                            startDestination = MainDestinations.SHARE,
                        ) {
                            navGraph(
                                onSuccess = appState::navigateToVisitorModule,
                                onMovieClick = appState::navigateToMovieInfo,
                                upPress = appState::upPress,
                                onSessionClick = appState::navigateToSessionsScreen,
                                onSuccessReservation = appState::navigateToSuccessScreen,
                                finishReserving = appState::navigateToVisitorModule,
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
        onSessionClick: (Int, NavBackStackEntry) -> Unit,
        upPress: () -> Unit,
        onSuccessReservation: (String, NavBackStackEntry) -> Unit,
        finishReserving: (NavBackStackEntry) -> Unit
    ) {
        addAuthGraph(onSuccess = onSuccess)
        addVisitorGraph(
            onMovieClick = onMovieClick,
            upPress = upPress,
            onSessionClick = onSessionClick,
            onSuccessReservation = onSuccessReservation,
            finishReserving = finishReserving
        )
    }

    private fun NavGraphBuilder.addAuthGraph(onSuccess: (NavBackStackEntry) -> Unit) {
        navigation(
            route = MainDestinations.SHARE,
            startDestination = MainDestinations.AUTH
        ) {
            composable(
                route = MainDestinations.AUTH,
            ) {
                AuthScreen(onSuccess = { onSuccess(it) })
            }
        }
    }

    private fun NavGraphBuilder.addVisitorGraph(
        onMovieClick: (Int, NavBackStackEntry) -> Unit,
        onSessionClick: (Int, NavBackStackEntry) -> Unit,
        upPress: () -> Unit,
        onSuccessReservation: (String, NavBackStackEntry) -> Unit,
        finishReserving: (NavBackStackEntry) -> Unit,
    ) {
        navigation(
            route = MainDestinations.VISITOR_ROUTE,
            startDestination =
            "${MainDestinations.VISITOR_ROUTE}/${MainDestinations.MOVIES}"
        ) {
            composable("${MainDestinations.VISITOR_ROUTE}/${MainDestinations.MOVIES}") {
                PosterScreen(
                    onMovieClick = { id -> onMovieClick(id, it) },
                    onSessionClick = { sessionId -> onSessionClick(sessionId, it) },
                )
            }
            composable(
                "${MainDestinations.VISITOR_ROUTE}/${MainDestinations.MOVIES}/{${MainDestinations.MOVIE_INFO}}",
                arguments = listOf(navArgument(MainDestinations.MOVIE_INFO) {
                    type = NavType.IntType
                })
            ) {
                MovieInfoScreen(onSessionClick = { sessionId -> onSessionClick(sessionId, it) })
            }
            composable("${MainDestinations.VISITOR_ROUTE}/${MainDestinations.PROFILE}") {
                ProfileScreen(
                    upPress = { upPress() }
                )
            }
            composable(
                "${MainDestinations.VISITOR_ROUTE}/${MainDestinations.SESSIONS}/{${MainDestinations.SESSION}}",
                arguments = listOf(navArgument(MainDestinations.SESSION) {
                    type = NavType.IntType
                })
            ) {
                ReservingScreen(
                    upPress = { upPress() },
                    finishReserving = { success -> onSuccessReservation(success, it) }
                )
            }
            composable(
                "${MainDestinations.VISITOR_ROUTE}/${MainDestinations.SUCCESS_ROUTE}/{${MainDestinations.SUCCESS}}",
                arguments = listOf(navArgument(MainDestinations.SUCCESS) {
                    type = NavType.StringType
                })
            ) {
                SuccessScreen(
                    onFinish = { finishReserving(it) }
                )
            }
        }
    }

    private fun NavGraphBuilder.addAdminGraph(
        onMovieClick: (Int, NavBackStackEntry) -> Unit,
        onSessionClick: (Int, NavBackStackEntry) -> Unit,
        upPress: () -> Unit,
        onSuccessReservation: (String, NavBackStackEntry) -> Unit,
        finishReserving: (NavBackStackEntry) -> Unit,
    ) {
        navigation(
            route = MainDestinations.VISITOR_ROUTE,
            startDestination =
            "${MainDestinations.VISITOR_ROUTE}/${MainDestinations.MOVIES}"
        ) {
            composable("${MainDestinations.VISITOR_ROUTE}/${MainDestinations.MOVIES}") {
                PosterScreen(
                    onMovieClick = { id -> onMovieClick(id, it) },
                    onSessionClick = { sessionId -> onSessionClick(sessionId, it) },
                )
            }
            composable(
                "${MainDestinations.VISITOR_ROUTE}/${MainDestinations.MOVIES}/{${MainDestinations.MOVIE_INFO}}",
                arguments = listOf(navArgument(MainDestinations.MOVIE_INFO) {
                    type = NavType.IntType
                })
            ) {
                MovieInfoScreen(onSessionClick = { sessionId -> onSessionClick(sessionId, it) })
            }
            composable("${MainDestinations.VISITOR_ROUTE}/${MainDestinations.PROFILE}") {
                ProfileScreen(
                    upPress = { upPress() }
                )
            }
            composable(
                "${MainDestinations.VISITOR_ROUTE}/${MainDestinations.SESSIONS}/{${MainDestinations.SESSION}}",
                arguments = listOf(navArgument(MainDestinations.SESSION) {
                    type = NavType.IntType
                })
            ) {
                ReservingScreen(
                    upPress = { upPress() },
                    finishReserving = { success -> onSuccessReservation(success, it) }
                )
            }
            composable(
                "${MainDestinations.VISITOR_ROUTE}/${MainDestinations.SUCCESS_ROUTE}/{${MainDestinations.SUCCESS}}",
                arguments = listOf(navArgument(MainDestinations.SUCCESS) {
                    type = NavType.StringType
                })
            ) {
                SuccessScreen(
                    onFinish = { finishReserving(it) }
                )
            }
        }
    }
}


