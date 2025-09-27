package com.hlopg.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hlopg.R

data class HostelCardData(
    val imageRes: Int? = null, // Use null for placeholder
    val name: String,
    val location: String,
    val rating: Float,
    val startingPrice: String,
    val currency: String = "₹",
    val hasWifi: Boolean = true,
    val hasFood: Boolean = true,
    val hasCleanService: Boolean = true,
    val hasWashService: Boolean = true
)

@Composable
fun CompactHostelCard(
    data: HostelCardData,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {},
    onKnowMoreClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.padding(4.dp), // Reduced padding from 8.dp
        shape = RoundedCornerShape(12.dp), // Slightly smaller corner radius
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp), // Reduced elevation
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onCardClick
    ) {
        Column {
            // Image Section with Heart Icon - Reduced height
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp) // Reduced from 200.dp
            ) {
                if (data.imageRes != null) {
                    Image(
                        painter = painterResource(id = data.imageRes),
                        contentDescription = "Hotel Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Placeholder for image
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Image Placeholder",
                            modifier = Modifier.size(32.dp), // Reduced from 48.dp
                            tint = Color.Gray
                        )
                    }
                }

                // Heart Icon - Smaller
                IconButton(
                    onClick = { /* Handle favorite */ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp) // Reduced padding
                        .size(32.dp) // Smaller button size
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Add to favorites",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp) // Reduced from 28.dp
                    )
                }
            }

            // Content Section - Reduced padding
            Column(
                modifier = Modifier.padding(10.dp) // Reduced from 16.dp
            ) {
                // Hotel Name and Rating Row - Smaller text sizes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = data.name,
                        fontSize = 16.sp, // Reduced from 24.sp
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(16.dp) // Reduced from 20.dp
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = data.rating.toString(),
                            fontSize = 14.sp, // Reduced from 18.sp
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp)) // Reduced spacing

                // Location - Smaller text
                Text(
                    text = data.location,
                    fontSize = 12.sp, // Reduced from 16.sp
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp)) // Reduced from 16.dp

                // Amenities Row - Smaller items
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CompactAmenityItem(
                        icon = Icons.Default.Bed,
                        label = "Beds",
                        isAvailable = true
                    )
                    CompactAmenityItem(
                        icon = Icons.Default.Restaurant,
                        label = "Food",
                        isAvailable = data.hasFood
                    )
                    CompactAmenityItem(
                        icon = Icons.Default.CleaningServices,
                        label = "Clean",
                        isAvailable = data.hasCleanService
                    )
                    CompactAmenityItem(
                        icon = Icons.Default.LocalLaundryService,
                        label = "Wash",
                        isAvailable = data.hasWashService
                    )
                }

                Spacer(modifier = Modifier.height(8.dp)) // Reduced from 16.dp

                // Price Section - Compact layout
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "Starts From",
                            fontSize = 10.sp, // Reduced from 14.sp
                            color = Color.Gray,
                            fontWeight = FontWeight.Normal
                        )
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "${data.currency}${data.startingPrice}",
                                fontSize = 18.sp, // Reduced from 28.sp
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = "Per person",
                            fontSize = 9.sp, // Reduced from 14.sp
                            color = Color.Gray
                        )
                    }

                    TextButton(
                        onClick = onKnowMoreClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.Gray
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "More...",
                            fontSize = 11.sp // Reduced and shortened text
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CompactAmenityItem(
    icon: ImageVector,
    label: String,
    isAvailable: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isAvailable) Color.Gray else Color.Gray.copy(alpha = 0.3f),
            modifier = Modifier.size(16.dp) // Reduced from 24.dp
        )
        Spacer(modifier = Modifier.height(2.dp)) // Reduced spacing
        Text(
            text = label,
            fontSize = 8.sp, // Reduced from 12.sp
            color = if (isAvailable) Color.Gray else Color.Gray.copy(alpha = 0.3f),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CompactHostelCardPreview() {
    MaterialTheme {
        CompactHostelCard(
            data = HostelCardData(
                imageRes = R.drawable.image,
                name = "Universe PG",
                location = "Madhapur",
                rating = 4.9f,
                startingPrice = "88,952",
                hasFood = true,
                hasCleanService = true,
                hasWashService = true
            ),
            modifier = Modifier.size(200.dp, 250.dp),
            onCardClick = {
                println("Card clicked")
            },
            onKnowMoreClick = {
                println("Know more clicked")
            }
        )
    }
}