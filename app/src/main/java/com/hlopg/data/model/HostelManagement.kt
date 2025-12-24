package com.hlopg.data.model

data class Hostel(
    val id: Int,
    val pgName: String,
    val pgInfo: String?,
    val pgType: String,
    val address: String,
    val area: String,
    val city: String,
    val state: String,
    val pincode: String,
    val rules: List<String>?,
    val ownerId: Int,
    val furnish: String?,
    val sharing: Map<String, Int>?,
    val foodMenu: String?,
    val rating: Double,
    val price: Double,
    val amenities: Map<String, Boolean>?,
    val popular: Int
)

// Network DTO (you can adapt field names / types)
data class HostelDto(
    val hostel_id: String,
    val hostel_name: String,
    val city: String,
    val area: String,
    val address: String,
    val rating: String,
    val amenities: Map<String, Boolean>?,
    val rules: List<String>?,
    val price: String,
    val owner_id: String,
    val popular: Int,
    val sharing: Map<String, Int>?,
    val pg_type: String,
    val created_at: String
)

// Add hostel
data class AddHostelRequest(
    val pgName: String,
    val pgInfo: String,
    val pgType: String,
    val address: String,
    val area: String,
    val city: String,
    val state: String,
    val pincode: String,
    val rules: String,
    val ownerId: Int,
    val furnish: String,
    val sharing: String,
    val foodMenu: String
)

data class PGDetailUiState(
    val id: String,
    val name: String,
    val address: String?,          // 🔹 nullable (UI uses ?.let)
    val description: String,
    val images: List<String>,
    val sharing: Map<String, Int>,
    val amenities: List<String>,
    val amenitiesCount: Int,       // 🔹 required by UI
    val rules: List<String>,
    val foodMenu: String,
    val rating: Double,
    val price: Int,
    val badge: String,
    val badgeColor: Long           // 🔹 required by UI
)





// Update hostel: dynamic keys, but we can still model as data class,
// or use Map<String, Any>. Here we use a flexible model.
data class UpdateHostelRequest(
    val pgName: String? = null,
    val pgInfo: String? = null,
    val pgType: String? = null,
    val address: String? = null,
    val area: String? = null,
    val city: String? = null,
    val state: String? = null,
    val pincode: String? = null,
    val rules: String? = null,
    val furnish: String? = null,
    val sharing: String? = null,
    val foodMenu: String? = null
)