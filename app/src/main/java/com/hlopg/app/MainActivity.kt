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
import com.hlopg.presentation.admin.components.OwnerBottomNavBar
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

                val isOwner = sessionManager.isOwner()
                val isUser = sessionManager.isUser()

                // User bottom nav routes
                val userBottomNavRoutes = listOf(
                    Screen.Home.route,
                    Screen.Search.route,
                    Screen.Favorites.route,
                    Screen.Profile.route
                )

                val ownerRoutes = setOf(
                    Screen.OwnerHome.route,
                    Screen.OwnerProfile.route,
                    Screen.PaymentList.route,
                    Screen.PGMembersList.route,
                )

                val showOwnerBottomBar = isOwner && currentRoute in ownerRoutes

                // Show user bottom bar only on user tabs
                val showUserBottomBar = isUser && currentRoute in userBottomNavRoutes

                val startDestination = when {
                    sessionManager.isLoggedIn() && sessionManager.isOwner() -> {
                        // Owner is logged in → go to owner home
                        Screen.OwnerHome.route
                    }
                    sessionManager.isLoggedIn() && sessionManager.isUser() -> {
                        // User is logged in → go to user home
                        Screen.Home.route
                    }
                    else -> {
                        // Not logged in → go to role selection
                        Screen.Role.route
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {

                    RootNavGraph(
                        navController = navController,
                        startDestination = startDestination
                    )

                    // ADMIN BOTTOM BAR
                    if (showOwnerBottomBar) {
                        OwnerBottomNavBar(
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