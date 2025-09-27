package com.hlopg.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class PGHostelAdData(
    val imageRes: Int? = null,
    val title: String = "PG\nHOSTEL",
    val subtitle: String = "COMFORTABLE ACCOMMODATION\nFOR STUDENTS & PROFESSIONALS",
    val features: List<String> = listOf(
        "FREE Wi-Fi",
        "MEALS INCLUDED",
        "24/7 ACCESS"
    ),
    val phoneNumber: String = "+91 98765 43210",
    val website: String = "www.hlopg.com"
)

@Composable
fun PGHostelSlideshow(
    slides: List<PGHostelAdData>,
    modifier: Modifier = Modifier,
    autoSlideInterval: Long = 3000L // 3 seconds
) {
    val pagerState = rememberPagerState(pageCount = { slides.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        while (true) {
            delay(autoSlideInterval)
            val nextPage = (pagerState.currentPage + 1) % slides.size

            pagerState.animateScrollToPage(
                page = nextPage,
                animationSpec = tween(
                    durationMillis = 1000, // Longer duration = smoother
                    easing = FastOutSlowInEasing // Smooth easing curve
                )
            )
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Slideshow
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            PGHostelAdCard(
                data = slides[page],
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Dots Indicator
        DotsIndicator(
            totalDots = slides.size,
            selectedIndex = pagerState.currentPage,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

@Composable
fun PGHostelAdCard(
    data: PGHostelAdData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            // Green gradient background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF5A3FD8), // Darker purple
                                Color(0xFF7656FF), // Your base purple
                                Color(0xFF9A7FFF)  // Lighter green
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                // Left side - Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Title
                    Text(
                        text = data.title,
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 34.sp
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Subtitle
                        Text(
                            text = data.subtitle,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 12.sp
                        )

                        // Features
                        data.features.forEach { feature ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Bullet point
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .background(
                                            Color.White,
                                            CircleShape
                                        )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = feature,
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Contact info with icon
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // House icon
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Contact",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = data.phoneNumber,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = data.website,
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Right side - Image
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(8.dp)
                ) {
                    if (data.imageRes != null) {
                        Image(
                            painter = painterResource(id = data.imageRes),
                            contentDescription = "PG Room",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Placeholder for image
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Room\nImage",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(totalDots) { index ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = if (index == selectedIndex) {
                            Color.Black
                        } else {
                            Color.Gray.copy(alpha = 0.5f)
                        },
                        shape = CircleShape
                    )
            )
        }
    }
}