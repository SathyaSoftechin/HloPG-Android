package com.hlopg.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.common.util.CollectionUtils.listOf
import com.hlopg.presentation.components.CompactPGCard

@Composable
fun FavoritesScreen() {
    val pgList = listOf(
        PGItem("Universal PG", "Madhapur", 4.5, 8000, "Women", Color(0xFFFF4CAF)),
        PGItem("Siva Kumar PG", "Madhapur", 4.5, 7000, "Boys", Color(0xFF4DA3FF)),
        PGItem("Universal PG", "Madhapur", 4.5, 8000, "Women", Color(0xFFFF4CAF)),
        PGItem("Siva Kumar PG", "Madhapur", 4.5, 7000, "Boys", Color(0xFF4DA3FF))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = "Favourite PG’s List",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            textAlign = TextAlign.Center
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 90.dp) // leave space for BottomNavBar
        ) {
            items(pgList) { pg ->
                CompactPGCard(
                    name = pg.name,
                    location = pg.location,
                    rating = pg.rating,
                    price = pg.price,
                    badge = pg.badge,
                    badgeColor = pg.badgeColor,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// simple model class
data class PGItem(
    val name: String,
    val location: String,
    val rating: Double,
    val price: Int,
    val badge: String,
    val badgeColor: Color
)
