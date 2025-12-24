package com.hlopg.presentation.user.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hlopg.HomeScreen
import com.hlopg.presentation.navigation.Screen
import com.hlopg.presentation.screen.ForgotPasswordScreen
import com.hlopg.presentation.screen.LoginScreen
import com.hlopg.presentation.screen.NotificationsScreen
import com.hlopg.presentation.screen.PGDetailScreen
import com.hlopg.presentation.screen.PaymentScreen
import com.hlopg.presentation.screen.ProfileScreen
import com.hlopg.presentation.screen.RoleSelectionScreen
import com.hlopg.presentation.screen.SetNewPasswordScreen
import com.hlopg.presentation.screen.SignupScreen
import com.hlopg.presentation.user.screen.BookedListScreen
import com.hlopg.presentation.user.screen.EditProfileScreen
import com.hlopg.presentation.user.screen.HelpAndSupportScreen
import com.hlopg.presentation.user.screen.OTPVerificationScreen
import com.hlopg.presentation.user.screen.PaymentDetailsScreen
import com.hlopg.presentation.user.screen.TermsAndConditionsScreen
import com.hlopg.presentation.user.screens.FavoritesScreen
import com.hlopg.presentation.user.screens.PGSearchScreen
import com.hlopg.presentation.user.viewmodel.OTPViewModel
import com.hlopg.presentation.user.viewmodel.PGDetailViewModel
import com.hlopg.presentation.user.viewmodel.PGSearchViewModel
import com.hlopg.presentation.user.viewmodel.SignupViewModel

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Role.route
    ) {

        // ============= AUTH SCREENS (No back navigation) =============

        composable(Screen.Role.route) {
            // Disable back button on Role Selection
            BackHandler(enabled = true) { /* Do nothing */ }

            RoleSelectionScreen(
                onSignupClick = { userType ->
                    // Pass role to signup screen
                    navController.navigate("${Screen.Signup.route}/$userType") {
                        popUpTo(Screen.Role.route) { inclusive = false }
                    }
                },
                onLoginClick = { userType ->
                    // Pass role to login screen if needed
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Role.route) { inclusive = false }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            // Disable back button on Login
            BackHandler(enabled = true) { /* Do nothing or exit app */ }

            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true } // Clear entire back stack
                    }
                },
                onSignupClick = {
                    // Navigate back to role selection or default signup
                    navController.navigate(Screen.Role.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.Forgotpass.route)
                }
            )
        }

        // Updated signup route with role parameter
        composable(
            route = "${Screen.Signup.route}/{userType}",
            arguments = listOf(navArgument("userType") {
                type = NavType.StringType
                defaultValue = "user"
            })
        ) { backStackEntry ->
            val userType = backStackEntry.arguments?.getString("userType") ?: "user"
            val viewModel: SignupViewModel = hiltViewModel()

            SignupScreen(
                userType = userType,
                viewModel = viewModel,
                onSignupSuccess = { phoneNumber ->
                    // Navigate to OTP verification with phone number and purpose
                    navController.navigate("${Screen.Otpverification.route}/$phoneNumber/register") {
                        // Don't clear back stack yet - wait for OTP verification
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Role.route) { inclusive = false }
                    }
                }
            )
        }

        // Legacy signup route without parameter for backward compatibility
        composable(Screen.Signup.route) {
            val viewModel: SignupViewModel = hiltViewModel()

            SignupScreen(
                userType = "user", // Default
                viewModel = viewModel,
                onSignupSuccess = { phoneNumber ->
                    navController.navigate("${Screen.Otpverification.route}/$phoneNumber/register")
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Role.route) { inclusive = false }
                    }
                }
            )
        }

        // OTP Verification route with phone number and purpose parameters
        composable(
            route = "${Screen.Otpverification.route}/{phoneNumber}/{purpose}",
            arguments = listOf(
                navArgument("phoneNumber") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("purpose") {
                    type = NavType.StringType
                    defaultValue = "register"
                }
            )
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            val purpose = backStackEntry.arguments?.getString("purpose") ?: "register"
            val viewModel: OTPViewModel = hiltViewModel()

            OTPVerificationScreen(
                phoneNumber = phoneNumber,
                purpose = purpose,
                viewModel = viewModel,
                onOTPVerified = {
                    // Navigate to home after successful OTP verification
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true } // Clear entire back stack
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Legacy OTP route without parameters
        composable(Screen.Otpverification.route) {
            val viewModel: OTPViewModel = hiltViewModel()

            OTPVerificationScreen(
                phoneNumber = "+91 9012345678", // Default
                purpose = "register",
                viewModel = viewModel,
                onOTPVerified = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Forgotpass.route) {
            ForgotPasswordScreen(
                onOtpSent = { phoneNumber ->
                    // Navigate to OTP with reset_password purpose
                    navController.navigate("${Screen.Otpverification.route}/$phoneNumber/reset_password")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Setnewpass.route) {
            SetNewPasswordScreen(
                onPasswordReset = { newPassword, confirmPassword ->
                    if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                        // Show error: Fields cannot be empty
                    } else if (newPassword != confirmPassword) {
                        // Show error: Passwords don't match
                    } else {
                        // Navigate back to Login after successful password reset
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true } // Clear stack
                        }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // ============= MAIN APP SCREENS =============

        composable(Screen.Home.route) {
            // Disable back button on Home (or handle app exit)
            BackHandler(enabled = true) {
                // Option 1: Do nothing (prevent going back to auth)
                // Option 2: Exit app with activity?.finish()
            }

            HomeScreen(navController = navController)
        }

        composable(Screen.Notifications.route) {
            NotificationsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.BookedList.route) {
            BookedListScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.PGDetails.route + "/{pgId}",
            arguments = listOf(navArgument("pgId") { type = NavType.StringType })
        ) {
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

        composable("search") {
            val viewModel: PGSearchViewModel = hiltViewModel()
            PGSearchScreen(
                viewModel = viewModel,
                onPGClick = { pgId ->
                    navController.navigate("pg_details/$pgId")
                }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen()
        }

        composable(Screen.EditProfileScreen.route) {
            EditProfileScreen(
                navController = navController
            )
        }

        composable(Screen.PaymentDetails.route) {
            PaymentDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Payment.route) {
            PaymentScreen()
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigate = { route ->
                    when (route) {
                        Screen.Login.route -> {
                            // Logout: Clear entire stack and go to login
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                        Screen.Terms.route,
                        Screen.Help.route,
                        Screen.EditProfileScreen.route,
                        Screen.Notifications.route -> {
                            navController.navigate(route)
                        }
                        else -> {
                            navController.navigate(route)
                        }
                    }
                }
            )
        }

        // ============= SETTINGS & INFO SCREENS =============

        composable(Screen.Terms.route) {
            TermsAndConditionsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Help.route) {
            HelpAndSupportScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}