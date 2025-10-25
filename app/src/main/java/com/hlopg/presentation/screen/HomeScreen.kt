package com.hlopg

import com.hlopg.presentation.components.ModernPGCard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hlopg.presentation.components.CompactPGCard
import com.hlopg.presentation.components.QuickFilters
import com.hlopg.presentation.components.home.FeaturedBanner
import com.hlopg.presentation.components.home.FloatingHeader

@Composable
fun HomeScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val listState = rememberLazyListState()


    Scaffold(
        containerColor = Color(0xFFFAFAFA),
        //bottomBar = { BottomNavBar( selectedTab = selectedTab, onTabSelected = { selectedTab = it } ) }
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {

            // Main scrollable content
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Top spacing for header
                item {
                    Spacer(modifier = Modifier.height(165.dp))
                }

                // Featured Banner
                item {
                    FeaturedBanner()
                }

                // Quick Filters
                item {
                    QuickFilters()
                }

                // Recommended Section
                item {
                    SectionHeader("Recommended for you", showSeeAll = true)
                }

                item {
                    RecommendedPGRow()
                }

                // Popular in Madhapur
                item {
                    SectionHeader("Popular in Madhapur", showSeeAll = true)
                }

                item {
                    PopularPGGrid()
                }

                // Newly Added
                item {
                    SectionHeader("Newly Added", showSeeAll = true)
                }

                item {
                    NewlyAddedRow()
                }

                // Premium PGs
                item {
                    SectionHeader("Premium PGs", showSeeAll = true)
                }

                item {
                    PremiumPGRow()
                }

                item {
                    Spacer(modifier = Modifier.height(78.dp))
                }
            }

            // Floating Header with Search
            FloatingHeader()
        }
    }
}




@Composable
fun SectionHeader(title: String, showSeeAll: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 19.sp,
            color = Color.Black
        )
        if (showSeeAll) {
            TextButton(onClick = { }) {
                Text(
                    text = "See All",
                    color = Color(0xFF7556FF),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun RecommendedPGRow() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(5) {
            ModernPGCard(
                name = "Universal Residency",
                location = "Madhapur",
                rating = 4.8,
                reviews = 124,
                price = 8000,
                badge = "Women",
                badgeColor = MaterialTheme.colorScheme.primary,
                discount = 10
            )
        }
    }
}


@Composable
fun PopularPGGrid() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        androidx.compose.foundation.lazy.LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(end = 16.dp)
        ) {
            items(
                listOf(
                    Triple("Siva Kumar PG", "Madhapur", Pair("Men", Color(0xFF2196F3))),
                    Triple("Green Villa PG", "Gachibowli", Pair("Women", Color(0xFFE91E63))),
                    Triple("Royal Heights", "Hitec City", Pair("Co-living", Color(0xFF9C27B0))),
                    Triple("Urban Nest", "Kondapur", Pair("Men", Color(0xFF2196F3)))
                )
            ) { (name, location, badgeInfo) ->
                CompactPGCard(
                    name = name,
                    location = location,
                    rating = when (name) {
                        "Siva Kumar PG" -> 4.5
                        "Green Villa PG" -> 4.7
                        "Royal Heights" -> 4.6
                        else -> 4.4
                    },
                    price = when (name) {
                        "Siva Kumar PG" -> 7000
                        "Green Villa PG" -> 9500
                        "Royal Heights" -> 8500
                        else -> 7500
                    },
                    badge = badgeInfo.first,
                    badgeColor = badgeInfo.second,
                    modifier = Modifier
                        .width(180.dp) // fixed width to make scroll natural
                )
            }
        }
    }
}

@Composable
fun NewlyAddedRow() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(5) {
            ModernPGCard(
                name = "Harmony Living",
                location = "Gachibowli",
                rating = 4.9,
                reviews = 45,
                price = 9000,
                badge = "Co-living",
                badgeColor = Color(0xFF9C27B0),
                isNew = true
            )
        }
    }
}

@Composable
fun PremiumPGRow() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(5) {
            ModernPGCard(
                name = "Elite Residency",
                location = "Jubilee Hills",
                rating = 5.0,
                reviews = 89,
                price = 15000,
                badge = "Premium",
                badgeColor = Color(0xFFFFD700),
                isPremium = true
            )
        }
    }
}




@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}