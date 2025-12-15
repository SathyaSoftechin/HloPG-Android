package com.hlopg.presentation.navigation

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
import com.hlopg.presentation.screen.*
import com.hlopg.presentation.user.screen.EditProfileScreen
import com.hlopg.presentation.user.screen.HelpAndSupportScreen
import com.hlopg.presentation.user.screen.TermsAndConditionsScreen
import com.hlopg.presentation.user.screens.PGSearchScreen
import com.hlopg.presentation.user.viewmodel.PGSearchViewModel

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        // ============= AUTH SCREENS (No back navigation) =============

        composable(Screen.Role.route) {
            // Disable back button on Role Selection
            BackHandler(enabled = true) { /* Do nothing */ }

            RoleSelectionScreen(
                onSignupClick = { userType ->
                    navController.navigate(Screen.Signup.route) {
                        popUpTo(Screen.Role.route) { inclusive = true }
                    }
                },
                onLoginClick = { userType ->
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Role.route) { inclusive = true }
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
                    navController.navigate(Screen.Signup.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.Forgotpass.route)
                }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                onSignupSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true } // Clear entire back stack
                    }
                },
                onLoginClick = {
                    navController.popBackStack()
                    // Or: navController.navigate(Screen.Login.route) {
                    //     popUpTo(Screen.Login.route) { inclusive = true }
                    // }
                },
            )
        }

        composable(Screen.Otpverification.route) {
            OTPVerificationScreen(
                phoneNumber = "+91 9012345678",
                onOTPComplete = { otp ->
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true } // Clear entire back stack
                    }
                },
                onResendClick = { /* resend OTP API call */ },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Forgotpass.route) {
            ForgotPasswordScreen(
                onOtpSent = { phoneNumber ->
                    navController.navigate(Screen.Setnewpass.route)
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

        composable(
            route = Screen.PGDetails.route + "/{pgId}",
            arguments = listOf(navArgument("pgId") { type = NavType.StringType })
        ) { backStackEntry ->
            val viewModel: PGDetailViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            PGDetailScreen(
                pgDetails = uiState,
                onBackClick = { navController.popBackStack() },
                onBookClick = {
                    // Navigate to booking/payment screen
                    // navController.navigate(Screen.Payment.route)
                }
            )
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
            FavoritesScreen(
            )
        }

        composable(Screen.EditProfileScreen.route) {
            EditProfileScreen(
                navController = navController
            )
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

        // ============= PAYMENT SCREEN (Optional) =============

        // Uncomment when ready to implement payment
        // composable(Screen.Payment.route) {
        //     PaymentScreen(
        //         onPaymentSuccess = {
        //             navController.navigate(Screen.Home.route) {
        //                 popUpTo(Screen.Home.route) { inclusive = false }
        //             }
        //         },
        //         onBackClick = { navController.popBackStack() }
        //     )
        // }
    }
}