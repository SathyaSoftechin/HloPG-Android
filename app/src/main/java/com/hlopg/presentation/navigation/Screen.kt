package com.hlopg.presentation.navigation


sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Bookings : Screen("bookings")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
}
