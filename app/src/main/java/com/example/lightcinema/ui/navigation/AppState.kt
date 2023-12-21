package com.example.lightcinema.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

class AppState(
    val navController: NavHostController
) {

    val shouldShowTopAppBar: Boolean
        @Composable get() {
            Log.d("Aboba121", "${navController.currentDestination?.route}")
            return navController.currentBackStackEntryAsState().value?.destination?.route != MainDestinations.AUTH
                    && !(navController.currentBackStackEntryAsState().value?.destination?.route?.contains(
                "${MainDestinations.VISITOR_ROUTE}/${MainDestinations.SUCCESS_ROUTE}/"
            ) ?: false)

        }

    val currentRoute: String?
        get() = navController.currentDestination?.route

    fun upPress() {
        navController.navigateUp()
    }

    fun navigateToAuth() {
        navController.navigate(MainDestinations.AUTH) {
            currentRoute?.let {
                popUpTo(it) {
                    inclusive = true
                }
            }
        }
    }

    fun navigateToMovieInfo(movie: Int, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.VISITOR_ROUTE}/${MainDestinations.MOVIES}/$movie")
        }
    }

    fun navigateToSessionsScreen(sessionId: Int, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.VISITOR_ROUTE}/${MainDestinations.SESSIONS}/$sessionId") {
//                launchSingleTop = true
//                restoreState = true
            }
        }
    }

    fun navigateToSuccessScreen(successString: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(
                "${MainDestinations.VISITOR_ROUTE}/" +
                        "${MainDestinations.SUCCESS_ROUTE}/" + successString
            ) {
                currentRoute?.let {
                    popUpTo(it) {
                        inclusive = true
                    }
                }
            }
        }
    }

    fun navigateToVisitorModule(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainDestinations.VISITOR_ROUTE) {
                currentRoute?.let {
                    popUpTo(it) {
                        inclusive = true
                    }
                }
            }
        }
    }

    fun navigateToAdminModule(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainDestinations.ADMIN_ROUTE) {
                currentRoute?.let {
                    popUpTo(it) {
                        inclusive = true
                    }
                }
            }
        }
    }

    fun navigateToProfileScreen() {
        navController.navigate(
            "${MainDestinations.VISITOR_ROUTE}/" +
                    "${MainDestinations.PROFILE}"
        ) {
            launchSingleTop = true
            restoreState = true
        }
    }


}

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}