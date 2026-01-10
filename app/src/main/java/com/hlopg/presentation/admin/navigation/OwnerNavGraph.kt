package com.hlopg.presentation.admin.navigation

import androidx.activity.compose.BackHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hlopg.app.Screen
import com.hlopg.presentation.admin.screen.EditOwnerProfileScreen
import com.hlopg.presentation.admin.screen.OwnerDashboardScreen
import com.hlopg.presentation.admin.screen.OwnerProfileScreen
import com.hlopg.presentation.admin.screen.PGMembersListScreen
import com.hlopg.presentation.admin.screen.PaymentsListScreen
import com.hlopg.presentation.admin.screen.UploadPGScreen
import com.hlopg.presentation.admin.screens.RoomManagementScreen
import com.hlopg.presentation.admin.viewmodel.OwnerDashboardViewModel
import com.hlopg.presentation.admin.viewmodel.PGMembersViewModel
import com.hlopg.presentation.admin.viewmodel.PaymentsViewModel
import com.hlopg.presentation.admin.viewmodel.RoomManagementViewModel
import com.hlopg.presentation.admin.viewmodel.UploadPGViewModel

/**
 * Owner navigation graph containing all admin-related screens
 */
fun NavGraphBuilder.adminNavGraph(
    navController: NavHostController
) {

    // ================= ADMIN HOME/DASHBOARD =================
    composable(Screen.OwnerHome.route) {
        // Prevent navigating back to auth/login
        BackHandler(enabled = true) {
            // Do nothing - prevent back navigation
        }

        val viewModel: OwnerDashboardViewModel = hiltViewModel()

        OwnerDashboardScreen(
            navController = navController,
            viewModel = viewModel
        )
    }

    // ================= ADMIN PROFILE =================
    composable(Screen.OwnerProfile.route) {
        OwnerProfileScreen(
            onNavigate = { route ->
                navController.navigate(route)
            }
        )
    }

    // ================= EDIT ADMIN PROFILE =================
    composable(Screen.EditOwnerProfileScreen.route) {
        EditOwnerProfileScreen(
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

    // ================= UPLOAD PG =================
    composable(Screen.UploadPG.route) {
        val viewModel: UploadPGViewModel = hiltViewModel()

        UploadPGScreen(
            navController = navController,
            viewModel = viewModel
        )
    }

    // ================= ROOM MANAGEMENT =================
    composable(Screen.RoomManagement.route) {
        val viewModel: RoomManagementViewModel = hiltViewModel()

        RoomManagementScreen(
            navController = navController,
            viewModel = viewModel
        )
    }


}