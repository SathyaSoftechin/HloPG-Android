package com.hlopg.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hlopg.R
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.draw.scale

data class CarouselItem(
    val id: Int,
    @DrawableRes val imageResId: Int,
    val description: String = ""
)

val items = listOf(
    CarouselItem(0, R.drawable.hyderabad, "Hyderabad"),
    CarouselItem(1, R.drawable.mumbai, "Mumbai"),
    CarouselItem(2, R.drawable.chennai, "Chennai"),
    CarouselItem(3, R.drawable.bengaluru, "Bengaluru"),
    CarouselItem(4, R.drawable.delhi, "Delhi"),
)

@Composable
fun CityCarousel() {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { items.size })

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 48.dp),
        pageSpacing = 16.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(221.dp)
            .padding(horizontal = 16.dp)
    ) { page ->

        val item = items[page]

        // Animate scale to highlight the centered item
        val scale by animateFloatAsState(
            targetValue = if (pagerState.currentPage == page) 1.1f else 0.9f,
            animationSpec = tween(300),
            label = ""
        )

        Image(
            painter = painterResource(id = item.imageResId),
            contentDescription = item.description,
            modifier = Modifier
                .fillMaxWidth()
                .height(205.dp)
                .clip(RoundedCornerShape(24.dp))
                .scale(scale)
                .clickable { /* TODO: handle click */ },
            contentScale = ContentScale.Crop
        )
    }
}
