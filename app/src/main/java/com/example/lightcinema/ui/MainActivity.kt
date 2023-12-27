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
import com.example.lightcinema.ui.screens.admin.add_hall.CreateHallScreen
import com.example.lightcinema.ui.screens.authorization.AuthScreen
import com.example.lightcinema.ui.screens.visitor.about.AboutProgramScreen
import com.example.lightcinema.ui.screens.visitor.movie_info.MovieInfoScreen
import com.example.lightcinema.ui.screens.visitor.profile.ProfileScreen
import com.example.lightcinema.ui.screens.visitor.reserving.ReservingScreen
import com.example.lightcinema.ui.screens.visitor.reserving.SuccessScreen
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
                    onProfileClickAdmin = appState::navigateToProfileScreenAdmin,
                    onProfileClickVisitor = appState::navigateToProfileScreenVisitor,
                    onLogoutClick = appState::navigateToAuth
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(
                            navController = appState.navController,
                            startDestination = MainDestinations.SHARE,
                        ) {
                            navGraph(
                                onSuccessAuthAdmin = appState::navigateToAdminModule,
                                onSuccessAuthVisitor = appState::navigateToVisitorModule,
                                onMovieClickVisitor = appState::navigateToMovieInfo,
                                upPress = appState::upPress,
                                onSessionClickVisitor = appState::navigateToSessionsScreen,
                                onSuccessReservationVisitor = appState::navigateToSuccessScreen,
                                finishReservingVisitor = appState::navigateToVisitorModule,
                                onAboutProgramClickVisitor = appState::navigateToAboutProgramScreen,
                                onMovieClickAdmin = appState::navigateToMovieInfoAdmin,
                                onSessionClickAdmin = appState::navigateToMovieInfoAdmin,
                                onAddHallClickAdmin = appState::navigateToCreateHallAdmin
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
        onSuccessAuthAdmin: (NavBackStackEntry) -> Unit,
        onSuccessAuthVisitor: (NavBackStackEntry) -> Unit,
        onMovieClickVisitor: (Int, NavBackStackEntry) -> Unit,
        onSessionClickVisitor: (Int, NavBackStackEntry) -> Unit,
        upPress: () -> Unit,
        onSuccessReservationVisitor: (String, NavBackStackEntry) -> Unit,
        finishReservingVisitor: (NavBackStackEntry) -> Unit,
        onAboutProgramClickVisitor: (NavBackStackEntry) -> Unit,
        onMovieClickAdmin: (Int, NavBackStackEntry) -> Unit,
        onSessionClickAdmin: (Int, NavBackStackEntry) -> Unit,
        onAddHallClickAdmin: (NavBackStackEntry) -> Unit,
//        onDeleteMovieAdmin: (NavBackStackEntry) -> Unit,
    ) {
        addAuthGraph(
            onSuccessAuthAdmin = onSuccessAuthAdmin,
            onSuccessAuthVisitor = onSuccessAuthVisitor
        )
        addVisitorGraph(
            onMovieClick = onMovieClickVisitor,
            upPress = upPress,
            onSessionClick = onSessionClickVisitor,
            onSuccessReservation = onSuccessReservationVisitor,
            finishReserving = finishReservingVisitor,
            onAboutProgramClick = onAboutProgramClickVisitor,
        )
        addAdminGraph(
            onMovieClick = onMovieClickAdmin,
            onSessionClick = onSessionClickAdmin,
//            onDeleteMovieAdmin = onDeleteMovieAdmin,
            upPress = upPress,
            onAddHallClick = onAddHallClickAdmin,
            onAboutProgramClick = onAboutProgramClickVisitor
        )
    }

    private fun NavGraphBuilder.addAuthGraph(
        onSuccessAuthAdmin: (NavBackStackEntry) -> Unit,
        onSuccessAuthVisitor: (NavBackStackEntry) -> Unit,
    ) {
        navigation(
            route = MainDestinations.SHARE,
            startDestination = MainDestinations.AUTH
        ) {
            composable(
                route = MainDestinations.AUTH,
            ) {
                AuthScreen(
                    onSuccessAdmin = { onSuccessAuthAdmin(it) },
                    onSuccessVisitor = { onSuccessAuthVisitor(it) }
                )
            }
        }
    }

    private fun NavGraphBuilder.addVisitorGraph(
        onMovieClick: (Int, NavBackStackEntry) -> Unit,
        onSessionClick: (Int, NavBackStackEntry) -> Unit,
        upPress: () -> Unit,
        onSuccessReservation: (String, NavBackStackEntry) -> Unit,
        finishReserving: (NavBackStackEntry) -> Unit,
        onAboutProgramClick: (NavBackStackEntry) -> Unit,
    ) {
        navigation(
            route = MainDestinations.VISITOR_ROUTE,
            startDestination =
            "${MainDestinations.VISITOR_ROUTE}/${MainDestinations.MOVIES}"
        ) {
            composable("${MainDestinations.VISITOR_ROUTE}/${MainDestinations.MOVIES}") {
                com.example.lightcinema.ui.screens.visitor.poster.PosterScreen(
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
                    upPress = { upPress() },
                    onAboutProgramClick = { onAboutProgramClick(it) }
                )
            }
            composable(
                "${MainDestinations.VISITOR_ROUTE}/" +
                        "${MainDestinations.PROFILE}/${MainDestinations.ABOUT}"
            ) {
                AboutProgramScreen()
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
        onAddHallClick: (NavBackStackEntry) -> Unit,
        upPress: () -> Unit,
        onAboutProgramClick: (NavBackStackEntry) -> Unit,
    ) {
        navigation(
            route = MainDestinations.ADMIN_ROUTE,
            startDestination =
            "${MainDestinations.ADMIN_ROUTE}/${MainDestinations.MOVIES}"
        ) {
            composable("${MainDestinations.ADMIN_ROUTE}/${MainDestinations.MOVIES}") {
                com.example.lightcinema.ui.screens.admin.poster.PosterScreen(
                    onMovieClick = { id -> onMovieClick(id, it) },
                    addMovieClick = { onMovieClick(-1, it) },
                )
            }
            composable(
                "${MainDestinations.ADMIN_ROUTE}/${MainDestinations.MOVIES}/{${MainDestinations.MOVIE_INFO}}",
                arguments = listOf(navArgument(MainDestinations.MOVIE_INFO) {
                    type = NavType.IntType
                })
            ) {
                com.example.lightcinema.ui.screens.admin.edit_movie.EditMovieScreen(
                    upPress = upPress
                )
            }
            composable("${MainDestinations.ADMIN_ROUTE}/${MainDestinations.PROFILE}") {
                com.example.lightcinema.ui.screens.admin.profile.ProfileScreen(
                    upPress = upPress,
                    addHallClick = { onAddHallClick(it) },
                    onAboutProgramClick = { onAboutProgramClick(it) },
                )
            }
            composable("${MainDestinations.ADMIN_ROUTE}/${MainDestinations.PROFILE}/${MainDestinations.HALL}") {
                CreateHallScreen(upPress = upPress)
            }
            composable(
                "${MainDestinations.ADMIN_ROUTE}/" +
                        "${MainDestinations.PROFILE}/${MainDestinations.ABOUT}"
            ) {
                AboutProgramScreen()
            }
        }
    }
}


