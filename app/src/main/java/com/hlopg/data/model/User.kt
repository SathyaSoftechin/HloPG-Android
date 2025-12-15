package com.hlopg.data.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val gender: String?,
    val userType: String,
    val token: String? = null
)