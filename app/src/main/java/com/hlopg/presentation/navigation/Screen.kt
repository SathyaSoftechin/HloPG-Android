package com.hlopg.presentation.navigation


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Otpverification : Screen("otpverification")
    object Role : Screen("role")
    object Setnewpass : Screen("setnewpass")
    object Forgotpass : Screen("forgotpass")
    object Payment : Screen("payment")
    object Home : Screen("home")
    object Search : Screen("search")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
}
