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
            Log.d("Aboba", "${navController.currentDestination?.route}")
            return navController.currentBackStackEntryAsState().value?.destination?.route != MainDestinations.AUTH
        }

    val currentRoute: String?
        get() = navController.currentDestination?.route

    fun upPress() {
        navController.navigateUp()
    }

    fun navigateToBottomBarRoute(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
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
            navController.navigate("${MainDestinations.POSTER_ROUTE}/$movie")
        }
    }

    fun navigateToVisitorModule(from: NavBackStackEntry) {
        navController.navigate(MainDestinations.VISITOR_ROUTE) {
            currentRoute?.let {
                popUpTo(it) {
                    inclusive = true
                }
            }
        }
    }

    fun navigateToAdminModule(from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainDestinations.ADMIN_ROUTE)
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

    fun navigateToSessionsScreen(movie: Int, sessionId: Int, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.POSTER_ROUTE}/$movie/$sessionId") {
//                launchSingleTop = true
//                restoreState = true
            }
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