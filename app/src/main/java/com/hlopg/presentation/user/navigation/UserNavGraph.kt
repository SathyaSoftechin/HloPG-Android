package com.hlopg.presentation.user.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hlopg.HomeScreen
import com.hlopg.app.NavArgs
import com.hlopg.app.Screen
import com.hlopg.presentation.screen.NotificationsScreen
import com.hlopg.presentation.screen.PGDetailScreen
import com.hlopg.presentation.screen.PaymentScreen
import com.hlopg.presentation.screen.ProfileScreen
import com.hlopg.presentation.user.screen.BookedListScreen
import com.hlopg.presentation.user.screen.EditProfileScreen
import com.hlopg.presentation.user.screen.HelpAndSupportScreen
import com.hlopg.presentation.user.screen.PaymentDetailsScreen
import com.hlopg.presentation.user.screen.TermsAndConditionsScreen
import com.hlopg.presentation.user.screens.FavoritesScreen
import com.hlopg.presentation.user.screens.PGSearchScreen
import com.hlopg.presentation.user.viewmodel.PGDetailViewModel
import com.hlopg.presentation.user.viewmodel.PGSearchViewModel

fun NavGraphBuilder.userNavGraph(navController: NavHostController) {

    // ============= USER HOME =============
    composable(Screen.Home.route) {
        BackHandler(enabled = true) { /* Prevent back to auth */ }

        HomeScreen(navController = navController)
    }

    // ============= PG DETAILS =============
    composable(
        route = "${Screen.PGDetails.route}/{${NavArgs.PG_ID}}",
        arguments = listOf(navArgument(NavArgs.PG_ID) {
            type = NavType.StringType
        })
    ) { backStackEntry ->
        val pgId = backStackEntry.arguments?.getString(NavArgs.PG_ID) ?: ""
        val viewModel: PGDetailViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()

        uiState?.let { detailUiState ->
            PGDetailScreen(
                uiState = detailUiState,
                onBackClick = { navController.popBackStack() },
                onBookConfirmed = {
                    navController.navigate(Screen.Payment.route)
                }
            )
        }
    }

    // ============= SEARCH =============
    composable(Screen.Search.route) {
        val viewModel: PGSearchViewModel = hiltViewModel()
        PGSearchScreen(
            viewModel = viewModel,
            onPGClick = { pgId ->
                navController.navigate("${Screen.PGDetails.route}/$pgId")
            }
        )
    }

    // ============= FAVORITES =============
    composable(Screen.Favorites.route) {
        FavoritesScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    // ============= BOOKED LIST =============
    composable(Screen.BookedList.route) {
        BookedListScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    // ============= PAYMENT =============
    composable(Screen.Payment.route) {
        PaymentScreen(
            onBackClick = { navController.popBackStack() },
            onPaymentSuccess = {
                navController.navigate(Screen.BookedList.route) {
                    popUpTo(Screen.Home.route) { inclusive = false }
                }
            }
        )
    }

    // ============= SHARED SCREENS (User Context) =============

    composable(Screen.Profile.route) {
        ProfileScreen(
            onNavigate = { route ->
                when (route) {
                    Screen.Login.route -> {
                        // Logout
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }

                    else -> navController.navigate(route)
                }
            }
        )
    }

    composable(Screen.EditProfileScreen.route) {
        EditProfileScreen(
            navController = navController
        )
    }

    composable(Screen.Notifications.route) {
        NotificationsScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    composable(Screen.PaymentDetails.route) {
        PaymentDetailsScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    composable(Screen.Terms.route) {
        TermsAndConditionsScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    composable(Screen.Help.route) {
        HelpAndSupportScreen(
            onBackClick = { navController.popBackStack() }
        )
    }
}