package com.hlopg.presentation.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hlopg.presentation.admin.components.AdminBottomNavBar
import com.hlopg.presentation.admin.components.AdminHeader
import com.hlopg.presentation.admin.components.AdminPGCard
import com.hlopg.presentation.admin.components.BookingAnalytics
import com.hlopg.presentation.admin.components.ComplaintsSection
import com.hlopg.presentation.admin.components.MembersSection
import com.hlopg.presentation.admin.components.PGUpdatesSection
import com.hlopg.presentation.admin.components.ReviewsSection
import com.hlopg.presentation.admin.viewmodel.AdminDashboardViewModel
import com.hlopg.presentation.admin.viewmodel.AdminNavEvent

@Composable
fun AdminDashboardScreen(
    navController: NavHostController,
    viewModel: AdminDashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.navEvents.collect { event ->
            when (event) {
                is AdminNavEvent.OpenPGDetails -> {
                    navController.navigate("admin/pg_details/${event.pgId}")
                }
                is AdminNavEvent.OpenMemberDetails -> {
                    navController.navigate("admin/member/${event.memberId}")
                }
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFFFAFAFA),
        bottomBar = {
            AdminBottomNavBar(
                currentRoute = "admin/home",
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading && uiState.myPGs.isEmpty() -> {
                    LoadingScreen()
                }
                uiState.error != null && uiState.myPGs.isEmpty() -> {
                    ErrorScreen(
                        message = uiState.error ?: "Unknown error",
                        onRetry = { viewModel.retry() }
                    )
                }
                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        // Top spacing for header
                        item {
                            Spacer(modifier = Modifier.height(140.dp))
                        }

                        // My PG's Section
                        if (uiState.myPGs.isNotEmpty()) {
                            item {
                                SectionHeader("My PG's")
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            items(uiState.myPGs) { pg ->
                                AdminPGCard(
                                    pgDetails = pg,
                                    onCardClick = { viewModel.onPGCardClick(it) },
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }

                            item {
                                Text(
                                    text = "By clicking on the PG you see the complete details of the selected PG",
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }

                        // PG Updates Section
                        item {
                            SectionHeader("PG Updates")
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        item {
                            PGUpdatesSection(
                                updateText = uiState.updateText,
                                onUpdateChange = { viewModel.updatePGUpdateText(it) }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Booking Analytics
                        item {
                            BookingAnalytics(
                                selectedDate = uiState.selectedAnalyticsDate,
                                onDateChange = { viewModel.setAnalyticsDate(it) },
                                bookingCount = uiState.bookingCount,
                                amountReceived = uiState.amountReceived,
                                chartData = uiState.chartData
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Members-In List
                        if (uiState.membersIn.isNotEmpty()) {
                            item {
                                SectionHeader("Members-in List")
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            item {
                                MembersSection(
                                    members = uiState.membersIn,
                                    onMemberClick = { viewModel.onMemberClick(it) }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }

                        // Members-Out List
                        if (uiState.membersOut.isNotEmpty()) {
                            item {
                                SectionHeader("Members-Out List")
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            item {
                                MembersSection(
                                    members = uiState.membersOut,
                                    onMemberClick = { viewModel.onMemberClick(it) }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }

                        // Complaints
                        if (uiState.complaints.isNotEmpty()) {
                            item {
                                SectionHeader("Complaints")
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            item {
                                ComplaintsSection(
                                    complaints = uiState.complaints
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }

                        // Reviews
                        if (uiState.reviews.isNotEmpty()) {
                            item {
                                SectionHeader("Reviews")
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            item {
                                ReviewsSection(
                                    reviews = uiState.reviews
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }

            // Admin Header - Always visible
            AdminHeader(
                location = uiState.location,
                onNotificationClick = {
                    navController.navigate("admin/notifications")
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
                text = "Loading Dashboard...",
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
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Color(0xFF1A1A1A),
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DashBoardPreview() {
    AdminDashboardScreen(navController = rememberNavController())
}