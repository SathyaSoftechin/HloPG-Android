package com.hlopg.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hlopg.R
import com.hlopg.presentation.components.EmptyStateCard
import com.hlopg.presentation.components.items

data class LikedPGData(
    val id: String,
    val name: String,
    val location: String,
    val rating: Float,
    val price: String,
    val imageRes: Int,
    val hasFood: Boolean,
    val hasCleanService: Boolean,
    val hasWashService: Boolean
)

@Composable
fun LikedScreen() {
    val likedPGs = remember {
        mutableStateListOf(
            LikedPGData(
                id = "1",
                name = "Universe PG Deluxe",
                location = "Madhapur",
                rating = 4.5f,
                price = "₹85,000",
                imageRes = R.drawable.image,
                hasFood = true,
                hasCleanService = true,
                hasWashService = true
            ),
            LikedPGData(
                id = "2",
                name = "Premium PG Elite",
                location = "Gachibowli",
                rating = 4.7f,
                price = "₹95,000",
                imageRes = R.drawable.image,
                hasFood = true,
                hasCleanService = true,
                hasWashService = false
            ),
            LikedPGData(
                id = "3",
                name = "Budget Comfort PG",
                location = "Kondapur",
                rating = 4.2f,
                price = "₹65,000",
                imageRes = R.drawable.image,
                hasFood = true,
                hasCleanService = false,
                hasWashService = true
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Liked PGs",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${likedPGs.size} saved",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        if (likedPGs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                EmptyStateCard(
                    icon = Icons.Outlined.FavoriteBorder,
                    message = "No liked PGs yet",
                    description = "Start exploring and save your favorite PGs"
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(likedPGs, key = { it.id }) { pg ->
                    LikedPGCard(
                        pg = pg,
                        onRemove = { likedPGs.remove(pg) }
                    )
                }
            }
        }
    }
}

@Composable
fun LikedPGCard(pg: LikedPGData, onRemove: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(pg.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .width(120.dp)
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = pg.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Remove",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = pg.location,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color(0xFFFFC107)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = pg.rating.toString(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (pg.hasFood) AmenityChip("🍽️")
                    if (pg.hasCleanService) AmenityChip("🧹")
                    if (pg.hasWashService) AmenityChip("👕")
                }

                Text(
                    text = "${pg.price}/6 months",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun AmenityChip(emoji: String) {
    Surface(
        color = Color(0xFFF0F0F0),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = emoji,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LikedScreenPreview() {
    MaterialTheme {
        LikedScreen()
    }
}