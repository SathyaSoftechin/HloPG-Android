package com.hlopg

import BottomNavBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hlopg.presentation.navigation.NavGraph
import com.hlopg.presentation.navigation.Screen
import com.hlopg.presentation.ui.theme.HloPGTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HloPGTheme {
                val navController = rememberNavController()

                // Observe current destination from navController
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Derive selected tab based on route
                val selectedTab = when (currentRoute) {
                    Screen.Home.route -> Screen.Home
                    Screen.Bookings.route -> Screen.Bookings
                    Screen.Favorites.route -> Screen.Favorites
                    Screen.Profile.route -> Screen.Profile
                    else -> Screen.Home
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    NavGraph(navController = navController)

                    BottomNavBar(
                        selectedTab = selectedTab,
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
