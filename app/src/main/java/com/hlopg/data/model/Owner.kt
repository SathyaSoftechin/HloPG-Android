package com.hlopg.data.model


data class Owner(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val userType: String,
    val token: String? = null
)

data class OwnerPg(
    val id: Int,
    val pgName: String,
    val city: String,
    val area: String,
    val ownerId: Int
)