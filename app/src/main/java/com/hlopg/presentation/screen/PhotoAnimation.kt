package com.hlopg.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PhotoCarouselScreen() {


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        CardSlideAnimation()
    }
}