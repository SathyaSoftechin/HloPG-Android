package com.hlopg.data.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val gender: String,
    val user_type: String = "user"
)

data class OtpRequest(
    val identifier: String,
    val otp: String
)

data class ResendOtpRequest(
    val identifier: String,
    val purpose: String
)


data class RegisterOwnerRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val user_type: String
)

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null,
    val token: String? = null
)


