package com.hlopg.app

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hlopg.presentation.admin.navigation.adminNavGraph
import com.hlopg.presentation.screen.ForgotPasswordScreen
import com.hlopg.presentation.screen.LoginScreen
import com.hlopg.presentation.screen.RoleSelectionScreen
import com.hlopg.presentation.screen.SetNewPasswordScreen
import com.hlopg.presentation.user.navigation.userNavGraph

@Composable
fun RootNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Role.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ============= ROLE SELECTION =============
        composable(Screen.Role.route) {
            BackHandler(enabled = false) {
                // Allow back navigation during testing
            }

            RoleSelectionScreen(
                onSignupClick = { userType ->
                    navController.navigate("${Screen.Signup.route}/$userType") {
                        popUpTo(Screen.Role.route) { inclusive = false }
                    }
                },
                onLoginClick = { userType ->
                    // Store user type for login navigation
                    navController.currentBackStackEntry?.savedStateHandle?.set("selectedUserType", userType)
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Role.route) { inclusive = false }
                    }
                }
            )
        }

        // ============= LOGIN =============
        composable(Screen.Login.route) {
            BackHandler(enabled = false) {
                // Allow back navigation during testing
            }

            LoginScreen(
                onLoginSuccess = { userType ->
                    // SessionManager is already saved in LoginViewModel!
                    // Just navigate based on the user_type returned from API

                    val homeRoute = if (userType == "admin") {
                        Screen.AdminHome.route
                    } else {
                        Screen.Home.route
                    }

                    navController.navigate(homeRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onSignupClick = {
                    navController.navigate(Screen.Role.route) {
                        launchSingleTop = true
                    }
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.Forgotpass.route)
                }
            )
        }

        // ============= FORGOT PASSWORD =============
        composable(Screen.Forgotpass.route) {
            ForgotPasswordScreen(
                onOtpSent = { phoneNumber ->
                    navController.navigate("${Screen.Otpverification.route}/$phoneNumber/reset_password/${UserType.USER}")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // ============= SET NEW PASSWORD =============
        composable(Screen.Setnewpass.route) {
            SetNewPasswordScreen(
                onPasswordReset = { newPassword, confirmPassword ->
                    if (newPassword.isNotEmpty() && confirmPassword.isNotEmpty() && newPassword == confirmPassword) {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // ============= USER NAV GRAPH =============
        userNavGraph(navController)

        // ============= ADMIN NAV GRAPH =============
        adminNavGraph(navController)
    }
}