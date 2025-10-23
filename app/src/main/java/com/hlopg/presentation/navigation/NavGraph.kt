package com.hlopg.presentation.navigation

import PGSearchScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hlopg.HomeScreen
import com.hlopg.presentation.screen.*
import com.hlopg.presentation.navigation.Screen


@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Bookings.route) { PGSearchScreen() }
        composable(Screen.Favorites.route) { FavoritesScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }

    }
}
