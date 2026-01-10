package com.hlopg.app

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hlopg.presentation.admin.navigation.adminNavGraph
import com.hlopg.presentation.admin.screen.OwnerLoginScreen
import com.hlopg.presentation.screen.ForgotPasswordScreen
import com.hlopg.presentation.screen.LoginScreen
import com.hlopg.presentation.screen.RoleSelectionScreen
import com.hlopg.presentation.screen.SetNewPasswordScreen
import com.hlopg.presentation.screen.SignupScreen
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
                    // Navigate to signup with userType
                    navController.navigate("${Screen.Signup.route}/$userType") {
                        popUpTo(Screen.Role.route) { inclusive = false }
                    }
                },
                onLoginClick = { userType ->
                    // Navigate to appropriate login screen based on userType
                    //val userTypeEnum = UserType.valueOf(userType)
                    val loginRoute = if (userType == UserType.OWNER) {
                        Screen.OwnerLogin.route  // ← FIXED: Navigate to owner login for owners
                    } else {
                        Screen.Login.route  // ← Navigate to user login for users
                    }

                    navController.navigate(loginRoute) {
                        popUpTo(Screen.Role.route) { inclusive = false }
                    }
                }
            )
        }

        // ============= USER LOGIN =============
        composable(Screen.Login.route) {
            BackHandler(enabled = false) {
                // Prevent back to role selection during login
            }

            LoginScreen(
                onLoginSuccess = { userType ->
                    // Navigate to user home
                    navController.navigate(Screen.Home.route) {
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

        // ============= OWNER LOGIN =============
        composable(Screen.OwnerLogin.route) {
            BackHandler(enabled = false) {
                // Prevent back to role selection during login
            }

            OwnerLoginScreen(
                onLoginSuccess = { userType ->
                    // Navigate to owner home
                    navController.navigate(Screen.OwnerHome.route) {
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

        // ============= SIGNUP (Dynamic based on userType) =============
        composable(
            route = "${Screen.Signup.route}/{${NavArgs.USER_TYPE}}",
            arguments = listOf(
                navArgument(NavArgs.USER_TYPE) {
                    type = NavType.StringType
                    defaultValue = UserType.USER
                }
            )
        ) { backStackEntry ->
            val userType = backStackEntry.arguments?.getString(NavArgs.USER_TYPE) ?: UserType.USER

            SignupScreen(
                userType = userType,
                onSignupSuccess = { phoneNumber ->
                    // Navigate to OTP verification
                    navController.navigate(
                        "${Screen.Otpverification.route}/$phoneNumber/signup/$userType"
                    )
                },
                onLoginClick = {
                    // Go back to role selection
                    navController.navigate(Screen.Role.route) {
                        popUpTo(Screen.Role.route) { inclusive = true }
                    }
                }
            )
        }

        // ============= OTP VERIFICATION =============
        composable(
            route = "${Screen.Otpverification.route}/{${NavArgs.PHONE_NUMBER}}/{${NavArgs.PURPOSE}}/{${NavArgs.USER_TYPE}}",
            arguments = listOf(
                navArgument(NavArgs.PHONE_NUMBER) { type = NavType.StringType },
                navArgument(NavArgs.PURPOSE) { type = NavType.StringType },
                navArgument(NavArgs.USER_TYPE) {
                    type = NavType.StringType
                    defaultValue = UserType.USER
                }
            )
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString(NavArgs.PHONE_NUMBER) ?: ""
            val purpose = backStackEntry.arguments?.getString(NavArgs.PURPOSE) ?: "signup"
            val userType = backStackEntry.arguments?.getString(NavArgs.USER_TYPE) ?: UserType.USER

            // TODO: When you create OTPVerificationScreen, uncomment this
            /*
            OTPVerificationScreen(
                phoneNumber = phoneNumber,
                purpose = purpose,
                userType = userType,
                onVerificationSuccess = {
                    if (purpose == "signup") {
                        // Navigate to appropriate login screen after OTP verification
                        val loginRoute = if (userType == UserType.OWNER) {
                            Screen.OwnerLogin.route
                        } else {
                            Screen.Login.route
                        }
                        navController.navigate(loginRoute) {
                            popUpTo(0) { inclusive = true }
                        }
                    } else if (purpose == "reset_password") {
                        navController.navigate(Screen.Setnewpass.route)
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
            */
        }

        // ============= FORGOT PASSWORD =============
        composable(Screen.Forgotpass.route) {
            ForgotPasswordScreen(
                onOtpSent = { phoneNumber ->
                    navController.navigate(
                        "${Screen.Otpverification.route}/$phoneNumber/reset_password/${UserType.USER}"
                    )
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

        // ============= ADMIN/OWNER NAV GRAPH =============
        adminNavGraph(navController)
    }
}