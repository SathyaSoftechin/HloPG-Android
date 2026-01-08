package com.hlopg.app


sealed class Screen(val route: String) {
    // ============= AUTH SCREENS =============
    object Role : Screen("role_selection")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Otpverification : Screen("otp_verification")
    object Forgotpass : Screen("forgot_password")
    object Setnewpass : Screen("set_new_password")

    // ============= USER SCREENS =============
    object Home : Screen("user_home")
    object PGDetails : Screen("pg_details")
    object Favorites : Screen("favorites")
    object BookedList : Screen("booked_list")
    object Payment : Screen("payment")
    object Search : Screen("search")

    // ============= ADMIN SCREENS =============
    object AdminHome : Screen("admin_home")
    object AdminProfile : Screen("admin_profile")
    object PaymentList : Screen("payment_list")
    object PGMembersList : Screen("pg_members_list")
    object UploadPG : Screen("upload_pg")
    object RoomManagement : Screen("room_management")
    object EditAdminProfileScreen : Screen("edit_admin_profile")

    // ============= SHARED SCREENS =============
    object Profile : Screen("profile")
    object EditProfileScreen : Screen("edit_profile")
    object Notifications : Screen("notifications")
    object PaymentDetails : Screen("payment_details")
    object Terms : Screen("terms_and_conditions")
    object Help : Screen("help_and_support")
}

// User type constants
object UserType {
    const val USER = "user"
    const val ADMIN = "admin"
}

// Navigation arguments
object NavArgs {
    const val USER_TYPE = "userType"
    const val PHONE_NUMBER = "phoneNumber"
    const val PURPOSE = "purpose"
    const val PG_ID = "pgId"
}