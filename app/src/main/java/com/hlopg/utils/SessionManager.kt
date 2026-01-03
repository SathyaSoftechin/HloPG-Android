package com.hlopg.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "hlopg_prefs"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_TYPE = "user_type"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PHONE = "user_phone"
        private const val KEY_AUTH_TOKEN = "auth_token"
    }

    // Save login session
    fun saveLoginSession(
        userId: String,  // ← Changed from Any to String
        userName: String,
        userEmail: String,
        userPhone: String,
        userType: String,
        authToken: String
    ) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_TYPE, userType)
            putString(KEY_USER_ID, userId)  // ← No cast needed now
            putString(KEY_USER_NAME, userName)
            putString(KEY_USER_EMAIL, userEmail)
            putString(KEY_USER_PHONE, userPhone)
            putString(KEY_AUTH_TOKEN, authToken)
            apply()
        }
    }

    // Check if user is logged in
    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    // Get user type
    fun getUserType(): String = prefs.getString(KEY_USER_TYPE, "user") ?: "user"

    // Check if user is admin
    fun isAdmin(): Boolean = getUserType() == "admin"

    // Get user ID
    fun getUserId(): String = prefs.getString(KEY_USER_ID, "") ?: ""

    // Get user name
    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "") ?: ""

    // Get user email
    fun getUserEmail(): String = prefs.getString(KEY_USER_EMAIL, "") ?: ""

    // Get user phone
    fun getUserPhone(): String = prefs.getString(KEY_USER_PHONE, "") ?: ""

    // Get auth token
    fun getAuthToken(): String = prefs.getString(KEY_AUTH_TOKEN, "") ?: ""

    // Clear session (logout)
    fun logout() {
        prefs.edit().clear().apply()
    }

    // Update user info
    fun updateUserInfo(
        userName: String? = null,
        userEmail: String? = null,
        userPhone: String? = null
    ) {
        prefs.edit().apply {
            userName?.let { putString(KEY_USER_NAME, it) }
            userEmail?.let { putString(KEY_USER_EMAIL, it) }
            userPhone?.let { putString(KEY_USER_PHONE, it) }
            apply()
        }
    }
}