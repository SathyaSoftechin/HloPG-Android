package com.hlopg.data.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val gender: String,
    val user_type: String = "user"
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class OtpRequest(
    val identifier: String,
    val otp_code: String,
    val purpose: String
)

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val user: User? = null
)

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val user_type: String
)
