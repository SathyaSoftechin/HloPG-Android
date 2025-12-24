package com.hlopg

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hlopg.presentation.components.ModernPGCard
import com.hlopg.presentation.components.QuickFilters
import com.hlopg.presentation.components.home.FeaturedBanner
import com.hlopg.presentation.components.home.FloatingHeader
import com.hlopg.presentation.navigation.Screen
import com.hlopg.presentation.user.viewmodel.HomeNavEvent
import com.hlopg.presentation.user.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedIndex by viewModel.selectedIndex.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.navEvents.collect { event ->
            when (event) {
                is HomeNavEvent.OpenPGDetails -> {
                    navController.navigate("${Screen.PGDetails.route}/${event.pgId}")
                }
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFFFAFAFA)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {

            if (uiState.isLoading && uiState.recommendedPGs.isEmpty()) {
                // Initial loading state
                LoadingScreen()
            } else if (uiState.error != null && uiState.recommendedPGs.isEmpty()) {
                // Error state
                ErrorScreen(
                    message = uiState.error ?: "Unknown error",
                    onRetry = { viewModel.retry() }
                )
            } else {
                // Content
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    // Top spacing for header
                    item {
                        Spacer(modifier = Modifier.height(180.dp))
                    }

                    // Featured Banner
                    item {
                        FeaturedBanner()
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Quick Filters
                    item {
                        QuickFilters(
                            selectedIndex = selectedIndex,
                            onFilterClick = { viewModel.setFilter(it) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Recommended Section
                    if (uiState.recommendedPGs.isNotEmpty()) {
                        item {
                            SectionHeader("Recommended for you", showSeeAll = true)
                        }
                        item {
                            RecommendedPGRow(
                                pgList = uiState.recommendedPGs,
                                onCardClick = viewModel::onCardClick,
                                onFavoriteClick = viewModel::onFavoriteClick
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    // Popular in Your City
                    if (uiState.popularPGs.isNotEmpty()) {
                        item {
                            SectionHeader("Popular in Your City", showSeeAll = true)
                        }
                        item {
                            PopularPGRow(
                                pgList = uiState.popularPGs,
                                onCardClick = viewModel::onCardClick,
                                onFavoriteClick = viewModel::onFavoriteClick
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    // Newly Added
                    if (uiState.newlyAddedPGs.isNotEmpty()) {
                        item {
                            SectionHeader("Newly Added", showSeeAll = true)
                        }
                        item {
                            NewlyAddedRow(
                                pgList = uiState.newlyAddedPGs,
                                onCardClick = viewModel::onCardClick,
                                onFavoriteClick = viewModel::onFavoriteClick
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    // Premium PGs
                    if (uiState.premiumPGs.isNotEmpty()) {
                        item {
                            SectionHeader("Premium PGs", showSeeAll = true)
                        }
                        item {
                            PremiumPGRow(
                                pgList = uiState.premiumPGs,
                                onCardClick = viewModel::onCardClick,
                                onFavoriteClick = viewModel::onFavoriteClick
                            )
                        }
                    }
                }
            }

            // Floating Header
            FloatingHeader(
                navController = navController,
                onNotificationClick = {
                    navController.navigate(Screen.Notifications.route)
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                }
            )
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = Color(0xFF7556FF)
            )
            Text(
                text = "Loading PGs...",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "Oops! Something went wrong",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = message,
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7556FF)
                )
            ) {
                Text("Retry")
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, showSeeAll: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF1A1A1A)
        )
        if (showSeeAll) {
            TextButton(
                onClick = { },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "See All",
                    color = Color(0xFF7556FF),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun RecommendedPGRow(
    pgList: List<com.hlopg.presentation.components.PGDetails>,
    onCardClick: (String) -> Unit,
    onFavoriteClick: (String, Boolean) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pgList) { pg ->
            ModernPGCard(
                pgDetails = pg,
                onCardClick = onCardClick,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@Composable
fun PopularPGRow(
    pgList: List<com.hlopg.presentation.components.PGDetails>,
    onCardClick: (String) -> Unit,
    onFavoriteClick: (String, Boolean) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pgList) { pg ->
            ModernPGCard(
                pgDetails = pg,
                onCardClick = onCardClick,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@Composable
fun NewlyAddedRow(
    pgList: List<com.hlopg.presentation.components.PGDetails>,
    onCardClick: (String) -> Unit,
    onFavoriteClick: (String, Boolean) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pgList) { pg ->
            ModernPGCard(
                pgDetails = pg,
                onCardClick = onCardClick,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@Composable
fun PremiumPGRow(
    pgList: List<com.hlopg.presentation.components.PGDetails>,
    onCardClick: (String) -> Unit,
    onFavoriteClick: (String, Boolean) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pgList) { pg ->
            ModernPGCard(
                pgDetails = pg,
                onCardClick = onCardClick,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}
