package com.hlopg.data.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

// Data Models for OwnerProfileScreen
data class OwnerProfileData(
    val id: String,
    val name: String,
    val email: String? = null,
    val avatarUrl: String? = null,
    val phone: String? = null
)

data class OwnerMenuItem(
    val id: String,
    val icon: ImageVector,
    val title: String,
    val iconTint: Color = Color(0xFF7556FF),
    val showArrow: Boolean = true,
    val enabled: Boolean = true,
    val onClick: () -> Unit = {}
)