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
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hlopg.R
import com.hlopg.presentation.components.EmptyStateCard
import com.hlopg.presentation.components.items

data class BookingData(
    val id: String,
    val pgName: String,
    val location: String,
    val bookingDate: String,
    val checkInDate: String,
    val checkOutDate: String,
    val roomType: String,
    val price: String,
    val status: BookingStatus,
    val imageRes: Int
)

enum class BookingStatus {
    CONFIRMED, PENDING, COMPLETED, CANCELLED
}

@Composable
fun BookingsScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Active", "Completed", "Cancelled")

    val sampleBookings = listOf(
        BookingData(
            id = "BK001",
            pgName = "Universe PG Deluxe",
            location = "Madhapur, Hyderabad",
            bookingDate = "Oct 1, 2025",
            checkInDate = "Oct 15, 2025",
            checkOutDate = "Apr 15, 2026",
            roomType = "Single AC Room",
            price = "₹85,000",
            status = BookingStatus.CONFIRMED,
            imageRes = R.drawable.image
        ),
        BookingData(
            id = "BK002",
            pgName = "Tech Hub PG",
            location = "Gachibowli, Hyderabad",
            bookingDate = "Sep 28, 2025",
            checkInDate = "Nov 1, 2025",
            checkOutDate = "May 1, 2026",
            roomType = "Double Sharing",
            price = "₹65,000",
            status = BookingStatus.PENDING,
            imageRes = R.drawable.image
        ),
        BookingData(
            id = "BK003",
            pgName = "Premium PG Elite",
            location = "Kondapur, Hyderabad",
            bookingDate = "Mar 15, 2025",
            checkInDate = "Apr 1, 2025",
            checkOutDate = "Oct 1, 2025",
            roomType = "Single Room",
            price = "₹78,000",
            status = BookingStatus.COMPLETED,
            imageRes = R.drawable.image
        )
    )

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
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Bookings",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        }

        // Content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val filteredBookings = when (selectedTab) {
                0 -> sampleBookings.filter { it.status == BookingStatus.CONFIRMED || it.status == BookingStatus.PENDING }
                1 -> sampleBookings.filter { it.status == BookingStatus.COMPLETED }
                2 -> sampleBookings.filter { it.status == BookingStatus.CANCELLED }
                else -> sampleBookings
            }

            items(filteredBookings) { booking ->
                BookingCard(booking)
            }

            if (filteredBookings.isEmpty()) {
                item {
                    EmptyStateCard(
                        icon = Icons.Outlined.EventBusy,
                        message = "No ${tabs[selectedTab].lowercase()} bookings"
                    )
                }
            }
        }
    }
}

@Composable
fun BookingCard(booking: BookingData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Image and Status Badge
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(booking.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentScale = ContentScale.Crop
                )

                // Status Badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    color = when (booking.status) {
                        BookingStatus.CONFIRMED -> Color(0xFF4CAF50)
                        BookingStatus.PENDING -> Color(0xFFFF9800)
                        BookingStatus.COMPLETED -> Color(0xFF2196F3)
                        BookingStatus.CANCELLED -> Color(0xFFF44336)
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = booking.status.name,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = booking.pgName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = booking.location,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BookingInfoItem("Check-in", booking.checkInDate)
                    BookingInfoItem("Check-out", booking.checkOutDate)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BookingInfoItem("Room Type", booking.roomType)
                    Text(
                        text = booking.price,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("View Details")
                    }

                    if (booking.status == BookingStatus.CONFIRMED) {
                        Button(
                            onClick = {},
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Contact")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookingInfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BookingsScreenPreview() {
    MaterialTheme {
        BookingsScreen()
    }
}