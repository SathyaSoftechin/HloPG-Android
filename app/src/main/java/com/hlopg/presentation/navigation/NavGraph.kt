package com.hlopg.presentation.navigation

import PGSearchScreen
import PaymentScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hlopg.HomeScreen
import com.hlopg.presentation.screen.*

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Role.route) {
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
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
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
                    popUpTo(Screen.Signup.route) { inclusive = true }
                }
            },
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                },
            )
        }

        composable(Screen.Otpverification.route) {
            OTPVerificationScreen(
                phoneNumber = "+91 9012345678",
                onOTPComplete = { otp ->
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onResendClick = { /* resend OTP API call */ },
                onBackClick = { navController.popBackStack() }
            )
        }


        composable(Screen.Forgotpass.route) {
            ForgotPasswordScreen(
                onOtpSent = { phoneNumber ->
                    // Navigate to Set New Password screen
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
                        // Optionally show error (Toast/Snackbar)
                    } else if (newPassword != confirmPassword) {
                        // Show mismatch error
                    } else {
                        // Navigate back to Login after successful password reset
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

//        composable(Screen.Payment.route) {
//            PaymentScreen(
//                onPaymentSuccess = {
//                    navController.popBackStack() // go back after payment
//                },
//                onBackClick = { navController.popBackStack() }
//            )
//        }

        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Search.route) { PGSearchScreen() }
        composable(Screen.Favorites.route) { FavoritesScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }
    }
}
