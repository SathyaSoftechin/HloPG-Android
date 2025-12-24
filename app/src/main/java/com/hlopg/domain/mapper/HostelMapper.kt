package com.hlopg.domain.mapper

import com.hlopg.data.model.Hostel
import com.hlopg.data.model.HostelDto
import com.hlopg.presentation.components.PGDetails

object HostelMapper {

    fun Hostel.toPGDetails(): PGDetails {
        val amenitiesCount =
            listOf(pgInfo, furnish, foodMenu)
                .count { !it.isNullOrBlank() } +
                    (sharing?.size ?: 0) +
                    if (!rules.isNullOrEmpty()) 1 else 0

        return PGDetails(
            id = id.toString(),
            name = pgName,
            location = listOfNotNull(area, city).joinToString(", "),
            rating = rating,
            badge = pgType,
            price = price.toInt(),
            amenitiesCount = amenitiesCount,
            imageUrl = null,
            imageRes = null,
            isFavorite = false,
            badgeColor = getBadgeColor(pgType)
        )
    }

    fun HostelDto.toDomain(): Hostel =
        Hostel(
            id = hostel_id.toInt(),
            pgName = hostel_name,
            pgInfo = null,
            pgType = pg_type,
            address = address,
            area = area,
            city = city,
            state = "",
            pincode = "",
            rules = rules,
            ownerId = owner_id.toInt(),
            furnish = null,
            sharing = sharing,
            foodMenu = null,
            rating = rating.toDoubleOrNull() ?: 0.0,
            price = price.toDoubleOrNull() ?: 0.0,
            amenities = amenities,
            popular = popular
        )

    private fun getBadgeColor(pgType: String): Long {
        return when (pgType.lowercase()) {
            "boys", "men" -> 0xFF3508FF
            "girls", "women" -> 0xFFFF06C1
            "co-living", "coliving" -> 0xFF08B64F
            "premium" -> 0xFFFFD700
            "executive" -> 0xFFFF9800
            else -> 0xFF4CAF50
        }
    }

    fun List<Hostel>.toPGDetailsList(): List<PGDetails> {
        return this.map { it.toPGDetails() }
    }
}