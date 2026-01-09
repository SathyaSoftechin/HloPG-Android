package com.hlopg.app

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
import com.hlopg.presentation.admin.components.AdminBottomNavBar
import com.hlopg.presentation.ui.theme.HloPGTheme
import com.hlopg.presentation.user.components.BottomNavBar
import com.hlopg.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HloPGTheme {

                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val isAdmin = true //sessionManager.isAdmin()
                val isUser = !sessionManager.isAdmin()

                // User bottom nav routes
                val userBottomNavRoutes = listOf(
                    Screen.Home.route,
                    Screen.Search.route,
                    Screen.Favorites.route,
                    Screen.Profile.route
                )

                val adminRoutes = setOf(
                    Screen.AdminHome.route,
                    Screen.AdminProfile.route,
                    Screen.PaymentList.route,
                    Screen.PGMembersList.route,
                )

                val showAdminBottomBar = isAdmin && currentRoute in adminRoutes

                // Show user bottom bar only on user tabs
                val showUserBottomBar = isUser && currentRoute in userBottomNavRoutes

                // TEMP start destination
                val startDestination = Screen.AdminHome.route

                Box(modifier = Modifier.fillMaxSize()) {

                    RootNavGraph(
                        navController = navController,
                        startDestination = startDestination
                        // ← sessionManager removed - ViewModel handles session saving!
                    )

                    // ADMIN BOTTOM BAR
                    if (showAdminBottomBar) {
                        AdminBottomNavBar(
                            currentRoute = currentRoute,
                            onNavigate = { route ->
                                if (route != currentRoute) {
                                    navController.navigate(route) {
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

                    // USER BOTTOM BAR
                    else if (showUserBottomBar) {
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