package com.hlopg.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hlopg.R
import com.hlopg.presentation.components.*

@Composable
fun HomeScreenCompactWithLightBg() {
    // Sample slideshow data
    val slides = listOf(
        PGHostelAdData(
            imageRes = R.drawable.image,
            title = "PG\nHOSTEL",
            subtitle = "COMFORTABLE ACCOMMODATION\nFOR STUDENTS & PROFESSIONALS",
            features = listOf("FREE Wi-Fi", "MEALS INCLUDED", "24/7 ACCESS"),
            phoneNumber = "+91 98765 43210",
            website = "www.hlopg.com"
        ),
        PGHostelAdData(
            imageRes = R.drawable.image,
            title = "PREMIUM\nPG",
            subtitle = "LUXURY LIVING SPACES\nFOR WORKING PROFESSIONALS",
            features = listOf("AC ROOMS", "HOUSEKEEPING", "SECURITY"),
            phoneNumber = "+91 98765 43211",
            website = "www.hlopg.com"
        ),
        PGHostelAdData(
            imageRes = R.drawable.image,
            title = "STUDENT\nHUB",
            subtitle = "AFFORDABLE ACCOMMODATION\nNEAR COLLEGES & UNIVERSITIES",
            features = listOf("STUDY ROOM", "WIFI", "LAUNDRY"),
            phoneNumber = "+91 98765 43212",
            website = "www.hlopg.com"
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        item {
            HelloPGHeader()
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            PGHostelSlideshow(
                slides = slides,
                autoSlideInterval = 4000L,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text(
                text = "Popular PGs in Madhapur",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(6) { index ->
                    CompactHostelCard(
                        data = HostelCardData(
                            imageRes = R.drawable.image,
                            name = "Universe PG ${index + 1}",
                            location = "Madhapur",
                            rating = (4.3f + (index * 0.1f)).coerceAtMost(5.0f),
                            startingPrice = "${80000 + (index * 5000)}",
                            hasFood = index % 2 == 0,
                            hasCleanService = true,
                            hasWashService = index % 3 != 0
                        ),
                        modifier = Modifier
                            .width(200.dp)
                            .height(250.dp),
                        onCardClick = {
                            println("Popular Madhapur Card $index clicked")
                        },
                        onKnowMoreClick = {
                            println("Popular Madhapur - Know more clicked for card $index")
                        }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text(
                text = "Budget Friendly PGs",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(5) { index ->
                    CompactHostelCard(
                        data = HostelCardData(
                            imageRes = R.drawable.image,
                            name = "Budget PG ${index + 1}",
                            location = "Madhapur",
                            rating = (4.0f + (index * 0.1f)).coerceAtMost(4.5f),
                            startingPrice = "${60000 + (index * 3000)}",
                            hasFood = index % 3 == 0,
                            hasCleanService = true,
                            hasWashService = index % 2 == 0
                        ),
                        modifier = Modifier
                            .width(200.dp)
                            .height(250.dp),
                        onCardClick = {
                            println("Budget PG Card $index clicked")
                        },
                        onKnowMoreClick = {
                            println("Budget PG - Know more clicked for card $index")
                        }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text(
                text = "Premium PGs",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(4) { index ->
                    CompactHostelCard(
                        data = HostelCardData(
                            imageRes = R.drawable.image,
                            name = "Premium PG ${index + 1}",
                            location = "Madhapur",
                            rating = (4.6f + (index * 0.1f)).coerceAtMost(5.0f),
                            startingPrice = "${120000 + (index * 8000)}",
                            hasFood = true,
                            hasCleanService = true,
                            hasWashService = true
                        ),
                        modifier = Modifier
                            .width(200.dp)
                            .height(250.dp),
                        onCardClick = {
                            println("Premium PG Card $index clicked")
                        },
                        onKnowMoreClick = {
                            println("Premium PG - Know more clicked for card $index")
                        }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text(
                text = "Near IT Companies",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(5) { index ->
                    CompactHostelCard(
                        data = HostelCardData(
                            imageRes = R.drawable.image,
                            name = "Tech Hub PG ${index + 1}",
                            location = "Gachibowli",
                            rating = (4.4f + (index * 0.1f)).coerceAtMost(4.9f),
                            startingPrice = "${95000 + (index * 6000)}",
                            hasFood = index % 2 == 0,
                            hasCleanService = true,
                            hasWashService = index % 3 != 0
                        ),
                        modifier = Modifier
                            .width(200.dp)
                            .height(250.dp),
                        onCardClick = {
                            println("Tech Hub PG Card $index clicked")
                        },
                        onKnowMoreClick = {
                            println("Tech Hub PG - Know more clicked for card $index")
                        }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text(
                text = "Perfect for Students",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(6) { index ->
                    CompactHostelCard(
                        data = HostelCardData(
                            imageRes = R.drawable.image,
                            name = "Student PG ${index + 1}",
                            location = "Kondapur",
                            rating = (4.1f + (index * 0.1f)).coerceAtMost(4.7f),
                            startingPrice = "${70000 + (index * 4000)}",
                            hasFood = true,
                            hasCleanService = index % 2 == 0,
                            hasWashService = index % 3 == 0
                        ),
                        modifier = Modifier
                            .width(200.dp)
                            .height(250.dp),
                        onCardClick = {
                            println("Student PG Card $index clicked")
                        },
                        onKnowMoreClick = {
                            println("Student PG - Know more clicked for card $index")
                        }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}