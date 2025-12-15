package com.hlopg.presentation.components

import android.R.attr.fontWeight
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hlopg.R

/**
 * Data class representing PG accommodation details
 * Suitable for API response mapping
 */

data class PGDetails(
    val id: String,
    val name: String,
    val location: String? = null,
    val rating: Double,
    val badge: String,
    val price: Int,
    val amenitiesCount: Int = 0,
    val imageUrl: String? = null,
    val imageRes: Int? = null,
    val isFavorite: Boolean = false,
    val badgeColor: Long = 0xFF7556FF // Default blue color
)

@Composable
fun ModernPGCard(
    pgDetails: PGDetails,
    onCardClick: (String) -> Unit = {},
    onFavoriteClick: (String, Boolean) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    var isFavorite by remember { mutableStateOf(pgDetails.isFavorite) }

    Card(
        modifier = modifier
            .width(185.dp)
            .clickable { onCardClick(pgDetails.id) },
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(10.dp)
            ) {
                // Handle both URL and Resource images
                if (pgDetails.imageUrl != null) {
                    AsyncImage(
                        model = pgDetails.imageUrl,
                        contentDescription = pgDetails.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(5.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else if (pgDetails.imageRes != null) {
                    Image(
                        painter = painterResource(pgDetails.imageRes),
                        contentDescription = pgDetails.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(5.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Placeholder if no image
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Home,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                // Favorite button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0x80000000))
                        .clickable {
                            isFavorite = !isFavorite
                            onFavoriteClick(pgDetails.id, isFavorite)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color(0xFFFF5252) else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 2.dp)
            ) {
                // Name
                Text(
                    text = pgDetails.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(5.dp))

                // Badge and Rating Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(pgDetails.badgeColor))
                            .padding(horizontal = 10.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = pgDetails.badge,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = String.format("%.1f", pgDetails.rating),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }

                Spacer(Modifier.height(5.dp))

                // Amenities Icons - Compact
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Show up to 3 amenity icons
                    CompactAmenityIcon(Icons.Outlined.Wifi, 0xFF7556FF)
                    CompactAmenityIcon(Icons.Outlined.AcUnit, 0xFF7556FF)
                    CompactAmenityIcon(Icons.Outlined.Kitchen, 0xFF7556FF)

                    // Show +X for remaining amenities
                    if (pgDetails.amenitiesCount > 3) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .background(
                                    Color(pgDetails.badgeColor).copy(alpha = 0.1f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+${pgDetails.amenitiesCount - 3}",
                                color = Color(pgDetails.badgeColor),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(Modifier.height(5.dp))

                Row {
                    // Price
                    Text(
                        text = "₹${formatPrice(pgDetails.price)}/Month",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Text(
                        text = "Starts from",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF0F710F),
                        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp,start = 2.dp)
                    )
                }

            }
        }
    }
}

@Composable
private fun CompactAmenityIcon(icon: ImageVector, color: Long = 0xFF7556FF) {
    Box(
        modifier = Modifier
            .size(25.dp)
            .background(
                Color(color).copy(alpha = 0.1f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(color),
            modifier = Modifier.size(15.dp)
        )
    }
}

@Composable
private fun AmenityIcon(icon: ImageVector, color: Long = 0xFF5D4FFF) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .background(
                Color(color).copy(alpha = 0.1f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(color),
            modifier = Modifier.size(28.dp)
        )
    }
}

// Helper function to format price with commas
private fun formatPrice(price: Int): String {
    return String.format("%,d", price)
}

// Preview with multiple test cases
@Preview(name = "Standard PG")
@Composable
fun StandardPGPreview() {
    ModernPGCard(
        pgDetails = PGDetails(
            id = "1",
            name = "Siva Kumar PG (Madhapur)",
            location = "Madhapur",
            rating = 4.5,
            badge = "Boys",
            price = 7000,
            amenitiesCount = 8,
            imageRes = R.drawable.hos2,
            badgeColor = 0xFF5D4FFF
        )
    )
}

@Preview(name = "Premium PG")
@Composable
fun PremiumPGPreview() {
    ModernPGCard(
        pgDetails = PGDetails(
            id = "2",
            name = "Luxury Haven Girls Hostel",
            location = "Gachibowli",
            rating = 4.8,
            badge = "Girls",
            price = 12500,
            amenitiesCount = 15,
            imageRes = R.drawable.hos2,
            isFavorite = true,
            badgeColor = 0xFFFF1493 // Pink
        )
    )
}

@Preview(name = "Budget PG")
@Composable
fun BudgetPGPreview() {
    ModernPGCard(
        pgDetails = PGDetails(
            id = "3",
            name = "Budget Friendly Co-living Space",
            location = "Kukatpally",
            rating = 3.9,
            badge = "Co-living",
            price = 4500,
            amenitiesCount = 5,
            imageRes = R.drawable.hos2,
            badgeColor = 0xFF4CAF50 // Green
        )
    )
}