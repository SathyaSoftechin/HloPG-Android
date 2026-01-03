package com.hlopg.presentation.user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hlopg.app.Screen


@Composable
fun BottomNavBar(
    selectedTab: Screen,
    onTabSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        Screen.Home to Pair(Icons.Filled.Home, Icons.Outlined.Home),
        Screen.Search to Pair(Icons.Filled.Search, Icons.Outlined.Search),
        Screen.Favorites to Pair(Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
        Screen.Profile to Pair(Icons.Filled.Person, Icons.Outlined.Person)
    )

    Surface(
        modifier = modifier
            .padding(vertical = 6.dp, horizontal = 22.dp)
            .shadow(8.dp, RoundedCornerShape(48.dp)),
        shape = RoundedCornerShape(48.dp),
        color = Color.White.copy(alpha = 0.2f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF212121))
                .padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { (screen, icons) ->
                val (filled, outlined) = icons
                val selected = screen == selectedTab

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (selected) Color.White else Color.Transparent)
                        .clickable { onTabSelected(screen) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (selected) filled else outlined,
                        contentDescription = screen.route,
                        tint = if (selected) MaterialTheme.colorScheme.primary else Color.White.copy(0.5f),
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}
