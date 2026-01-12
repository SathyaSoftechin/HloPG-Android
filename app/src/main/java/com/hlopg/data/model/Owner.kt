package com.hlopg.data.model

import com.google.gson.annotations.SerializedName


//data class Owner(
//    val id: Int,
//    val name: String,
//    val email: String,
//    val phone: String,
//    val userType: String,
//    val token: String? = null
//)

data class OwnerLoginResponse(
    val token: String,
    val owner: Owner
)


data class Owner(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    @SerializedName("user_type")
    val userType: String
)


data class OwnerPg(
    val id: Int,
    val pgName: String,
    val city: String,
    val area: String,
    val ownerId: Int
)