package com.hlopg.domain.mapper

import com.hlopg.data.model.Hostel
import com.hlopg.data.model.HostelDto
import com.hlopg.presentation.components.PGDetails

object HostelMapper {

    fun Hostel.toPGDetails(): PGDetails {
        return PGDetails(
            id = id.toString(),
            name = pgName,
            // e.g. "Madhapur, Hyderabad"
            location = listOfNotNull(area, city).joinToString(", "),
            // You don't have rating in Hostel model -> default for now
            rating = 0.0,
            // Use pgType as the badge text (Boys/Girls/Co-living/etc.)
            badge = pgType,
            // No price in Hostel -> you can change this once you add price
            price = 0,
            // Example: count how many optional features are present
            amenitiesCount = listOf(pgInfo, rules, furnish, sharing, foodMenu)
                .count { !it.isNullOrBlank() },
            // No imageUrl in Hostel -> null for now
            imageUrl = null,
            imageRes = null,
            // No favorite info in Hostel -> default
            isFavorite = false,
            // Color based on pgType
            badgeColor = getBadgeColor(pgType)
        )
    }

    fun HostelDto.toDomain(): Hostel =
        Hostel(
            id = hostel_id.toInt(),
            pgName = hostel_name,
            pgInfo = null, // or from another field
            pgType = pg_type,
            address = address,
            area = area,
            city = city,
            state = "",    // backend doesn’t send state in the snippet
            pincode = "",
            rules = rules?.joinToString(", "),
            ownerId = owner_id.toInt(),
            furnish = null,
            sharing = sharing?.entries
                ?.joinToString(", ") { (k, v) -> "$k:$v" },
            foodMenu = null
        )


    private fun getBadgeColor(pgType: String): Long {
        return when (pgType.lowercase()) {
            "boys", "men" -> 0xFF3508FF // Blue
            "girls", "women" -> 0xFFFF06C1 // Pink
            "co-living", "coliving" -> 0xFF08B64F // Purple
            "premium" -> 0xFFFFD700 // Gold
            "executive" -> 0xFFFF9800 // Orange
            else -> 0xFF4CAF50 // Green
        }
    }

    fun List<Hostel>.toPGDetailsList(): List<PGDetails> {
        return this.map { it.toPGDetails() }
    }
}
