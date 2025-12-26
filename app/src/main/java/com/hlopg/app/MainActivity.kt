package com.hlopg.app

import BottomNavBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hlopg.presentation.navigation.Screen
import com.hlopg.presentation.ui.theme.HloPGTheme
import com.hlopg.presentation.user.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HloPGTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Show bottom nav only for main tabs
                val showBottomBar = currentRoute in listOf(
                    Screen.Home.route,
                    Screen.Search.route,
                    Screen.Favorites.route,
                    Screen.Profile.route
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    NavGraph(navController = navController)

                    if (showBottomBar) {
                        BottomNavBar(
                            selectedTab = when (currentRoute) {
                                Screen.Home.route -> Screen.Home
                                Screen.Search.route -> Screen.Search
                                Screen.Favorites.route -> Screen.Favorites
                                Screen.Profile.route -> Screen.Profile
                                else -> Screen.Home
                            },
                            onTabSelected = { screen ->
                                if (screen.route != currentRoute) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding()
                                .align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }
}
