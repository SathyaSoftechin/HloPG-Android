package com.hlopg.presentation.admin.navigation

import androidx.activity.compose.BackHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hlopg.app.Screen
import com.hlopg.presentation.admin.AdminDashboardScreen
import com.hlopg.presentation.admin.screen.AdminProfileScreen
import com.hlopg.presentation.admin.screen.EditAdminProfileScreen
import com.hlopg.presentation.admin.screen.PGMembersListScreen
import com.hlopg.presentation.admin.screen.PaymentsListScreen
import com.hlopg.presentation.admin.viewmodel.AdminDashboardViewModel
import com.hlopg.presentation.admin.viewmodel.PGMembersViewModel
import com.hlopg.presentation.admin.viewmodel.PaymentsViewModel

/**
 * Admin navigation graph containing all admin-related screens
 */
fun NavGraphBuilder.adminNavGraph(
    navController: NavHostController
) {

    // ================= ADMIN HOME/DASHBOARD =================
    composable(Screen.AdminHome.route) {
        // Prevent navigating back to auth/login
        BackHandler(enabled = true) {
            // Do nothing - prevent back navigation
        }

        val viewModel: AdminDashboardViewModel = hiltViewModel()

        AdminDashboardScreen(
            navController = navController,
            viewModel = viewModel
        )
    }

    // ================= ADMIN PROFILE =================
    composable(Screen.AdminProfile.route) {
        AdminProfileScreen(
            onNavigate = { route ->
                navController.navigate(route)
            }
        )
    }

    // ================= EDIT ADMIN PROFILE =================
    composable(Screen.EditAdminProfileScreen.route) {
        EditAdminProfileScreen(
            navController = navController
        )
    }

    // ================= PAYMENTS LIST =================
    composable(Screen.PaymentList.route) {
        val viewModel: PaymentsViewModel = hiltViewModel()

        PaymentsListScreen(
            viewModel = viewModel
        )
    }

    // ================= PG MEMBERS LIST =================
    composable(Screen.PGMembersList.route) {
        val viewModel: PGMembersViewModel = hiltViewModel()

        PGMembersListScreen(
            navController = navController,
            viewModel = viewModel
        )
    }
}