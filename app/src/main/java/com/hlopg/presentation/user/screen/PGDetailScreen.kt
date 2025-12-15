package com.hlopg.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hlopg.R
import com.hlopg.presentation.components.PGDetails

@Composable
fun PGDetailScreen(
    pgDetails: PGDetails?,          // ✅ data from API via ViewModel
    onBackClick: () -> Unit = {},
    onBookClick: () -> Unit = {}
) {
    // 🔹 Loading state while ViewModel is fetching data
    if (pgDetails == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    color = Color(0xFF6C3CE3)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Loading PG details...",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Custom Top Bar
            item {
                PGDetailTopBar(
                    onBackClick = onBackClick,
                    onFavoriteClick = { /* TODO favorite */ },
                    onShareClick = { /* TODO share */ }
                )
            }

            // Title + basic info
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = pgDetails.name,              // ✅ from API
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    pgDetails.location?.let {
                        Text(
                            text = it,          // ✅ from API
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (pgDetails.rating > 0.0) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = pgDetails.rating.toString(),
                                fontSize = 13.sp,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = pgDetails.badge,         // ✅ Boys/Girls/Co-Living etc.
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(pgDetails.badgeColor)
                        )
                    }
                }
            }

            // Location Header card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    ),
                    shape = RoundedCornerShape(3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFF6C3CE3),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        pgDetails.location?.let {
                            Text(
                                text = it,      // ✅ API location
                                fontSize = 13.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Main Image with rounded corners (still static drawable for now)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.hostel2),
                            contentDescription = "PG Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(5) { index ->
                        Box(
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (index == 0) Color(0xFF6C3CE3)
                                        else Color.LightGray
                                    )
                            )
                        }
                    }
                }
            }

            // Description (simple derived text using API data)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF6F4FF)
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    val description = buildString {
                        append(pgDetails.name)
                        append(" in ")
                        append(pgDetails.location)
                        append(" is a ")
                        append(pgDetails.badge.lowercase())
                        append(" PG. ")
                        if (pgDetails.price > 0) {
                            append("Rooms start from ₹${pgDetails.price}. ")
                        }
                        append("It offers ")
                        append(pgDetails.amenitiesCount)
                        append(" key amenities for a comfortable stay.")
                    }

                    Text(
                        text = description,                // ✅ fully based on API data
                        fontSize = 13.sp,
                        color = Color.Black.copy(alpha = 0.8f),
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Badge using API pgType/badge
            item {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(pgDetails.badgeColor))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = pgDetails.badge,           // ✅ API type: Men/Women/Co-Living
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            // Sharing Type Section (limited by what we know: price)
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Select Sharing Type",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (pgDetails.price > 0) {
                            // For now, show a single "From" price chip based on API
                            SharingTypeChip(
                                type = "1",
                                price = "From ₹${pgDetails.price}"
                            )
                        } else {
                            Text(
                                text = "Pricing details coming soon",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            // Amenities Section – using amenitiesCount from API
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Amenities",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = if (pgDetails.amenitiesCount > 0)
                            "${pgDetails.amenitiesCount} amenities available at this PG."
                        else
                            "Amenities information will be updated soon.",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

            // PG Rules Section (still static for now – your API model doesn't carry per-rule list yet)
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "PG Rules",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        RuleIcon(Icons.Default.LocalBar, "No Alcohol")
                        RuleIcon(Icons.Default.SmokeFree, "No smoking")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF5F5F5)
                        ),
                        shape = RoundedCornerShape(3.dp)
                    ) {
                        Text(
                            text = "If you plan to leave the PG, please inform us at least 15 days in advance to receive your advance amount. Failure to do so will result in forfeiture of the advance.",
                            fontSize = 12.sp,
                            color = Color.Black.copy(alpha = 0.6f),
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            // Food Menu Section (UI static, but you can later wire to foodMenu from API)
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Food Menu :- Day To day menu",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF6C3CE3)
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color(0xFF6C3CE3)
                        )
                    }
                }
            }

            // PG Location Section (map still static)
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "PG Location",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color(0xFFE8EAF6))
                    ) {
                        Image(
                            painter = painterResource(R.drawable.hos2),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // Ratings Section (currently static)
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Ratings and Reviews",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Overall Rating
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Very Good",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Row {
                                repeat(5) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Total 19 ratings and\n10 reviews",
                                fontSize = 11.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        // Rating Bars
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            RatingBar(5, 8)
                            RatingBar(4, 3)
                            RatingBar(3, 4)
                            RatingBar(2, 2)
                            RatingBar(1, 2)
                        }
                    }
                }
            }

            // Review Card (also static demo text)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5FF)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color(0xFF6C3CE3),
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.CenterVertically)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Image(
                                painter = painterResource(R.drawable.hostel2),
                                contentDescription = "Reviewer",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .align(Alignment.CenterHorizontally)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Absolutely loved my stay at this PG! The rooms were clean, spacious, and well-maintained. The staff was friendly and always ready to help — felt just like home!",
                                fontSize = 13.sp,
                                color = Color.Black.copy(alpha = 0.8f),
                                lineHeight = 18.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = Color(0xFF6C3CE3),
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Bottom Button (Fixed at bottom)
        Button(
            onClick = onBookClick,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .height(48.dp),
            shape = RoundedCornerShape(3.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6C3CE3),
                contentColor = Color.White
            )
        ) {
            val buttonText =
                if (pgDetails.price > 0) "Book Now • From ₹${pgDetails.price}"
                else "Book Now"

            Text(
                text = buttonText,      // ✅ uses API price if available
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun PGDetailTopBar(
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xFFEDF5FF))
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            Row {
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.Black
                    )
                }
                IconButton(onClick = onShareClick) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun SharingTypeChip(type: String, price: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(3.dp))
                .border(1.dp, Color.LightGray, RoundedCornerShape(3.dp))
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = type,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = price,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF6C3CE3)
        )
    }
}

@Composable
fun RuleIcon(icon: ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF6C3CE3),
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.Black.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RatingBar(stars: Int, count: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$stars",
            fontSize = 12.sp,
            color = Color.Black,
            modifier = Modifier.width(20.dp)
        )
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFFFFD700),
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(count / 10f)
                    .background(Color(0xFF6C3CE3))
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = count.toString(),
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.width(20.dp)
        )
    }
}

fun getAmenityIcon(name: String): ImageVector {
    return when (name.lowercase()) {
        "free wifi" -> Icons.Default.Wifi
        "fan" -> Icons.Default.Air
        "bed" -> Icons.Default.Bed
        "washing" -> Icons.Default.LocalLaundryService
        "lights" -> Icons.Default.Lightbulb
        "colboard" -> Icons.Default.Weekend
        "geyser" -> Icons.Default.WaterDrop
        "water" -> Icons.Default.Water
        "fridge" -> Icons.Default.Kitchen
        "gym" -> Icons.Default.FitnessCenter
        "tv" -> Icons.Default.Tv
        "ac" -> Icons.Default.AcUnit
        "parking" -> Icons.Default.LocalParking
        "food" -> Icons.Default.Restaurant
        "lift" -> Icons.Default.Elevator
        "cam's" -> Icons.Default.Videocam
        "self cook..." -> Icons.Default.SoupKitchen
        else -> Icons.Default.CheckCircle
    }
}
