package com.hlopg.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hlopg.presentation.components.EmptyStateCard
import com.hlopg.presentation.components.items

data class NotificationData(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val type: NotificationType,
    val isRead: Boolean = false
)

enum class NotificationType {
    BOOKING, PAYMENT, OFFER, GENERAL
}

@Composable
fun NotificationsScreen() {
    val notifications = remember {
        mutableStateListOf(
            NotificationData(
                id = "1",
                title = "Booking Confirmed! 🎉",
                message = "Your booking for Universe PG Deluxe has been confirmed. Check-in date: Oct 15, 2025",
                timestamp = "2 hours ago",
                type = NotificationType.BOOKING,
                isRead = false
            ),
            NotificationData(
                id = "2",
                title = "Payment Successful",
                message = "Payment of ₹85,000 received successfully for booking BK001",
                timestamp = "5 hours ago",
                type = NotificationType.PAYMENT,
                isRead = false
            ),
            NotificationData(
                id = "3",
                title = "Special Offer! 🎁",
                message = "Get 10% off on your next booking. Use code: HELLO10",
                timestamp = "1 day ago",
                type = NotificationType.OFFER,
                isRead = true
            ),
            NotificationData(
                id = "4",
                title = "New PG Added",
                message = "Check out Premium PG Elite in your area with amazing amenities!",
                timestamp = "2 days ago",
                type = NotificationType.GENERAL,
                isRead = true
            ),
            NotificationData(
                id = "5",
                title = "Booking Reminder",
                message = "Your check-in date is approaching. Make sure to complete KYC verification.",
                timestamp = "3 days ago",
                type = NotificationType.BOOKING,
                isRead = true
            )
        )
    }

    val unreadCount = notifications.count { !it.isRead }

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Notifications",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (unreadCount > 0) {
                        Text(
                            text = "$unreadCount unread",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                if (unreadCount > 0) {
                    TextButton(
                        onClick = {
                            notifications.forEachIndexed { index, notification ->
                                notifications[index] = notification.copy(isRead = true)
                            }
                        }
                    ) {
                        Text(
                            "Mark all read",
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                EmptyStateCard(
                    icon = Icons.Outlined.Notifications,
                    message = "No notifications",
                    description = "You're all caught up!"
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(notifications, key = { it.id }) { notification ->
                    NotificationCard(notification)
                    Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
fun NotificationCard(notification: NotificationData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (!notification.isRead) Color(0xFFF0F7FF) else Color.White)
            .clickable { }
            .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(44.dp),
            shape = CircleShape,
            color = when (notification.type) {
                NotificationType.BOOKING -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                NotificationType.PAYMENT -> Color(0xFF2196F3).copy(alpha = 0.1f)
                NotificationType.OFFER -> Color(0xFFFF9800).copy(alpha = 0.1f)
                NotificationType.GENERAL -> Color(0xFF9C27B0).copy(alpha = 0.1f)
            }
        ) {
            Box(contentAlignment = Alignment.Center) {   // ✅ proper centering
                Icon(
                    imageVector = when (notification.type) {
                        NotificationType.BOOKING -> Icons.Outlined.EventAvailable
                        NotificationType.PAYMENT -> Icons.Outlined.Payment
                        NotificationType.OFFER -> Icons.Outlined.LocalOffer
                        NotificationType.GENERAL -> Icons.Outlined.Info
                    },
                    contentDescription = null,
                    tint = when (notification.type) {
                        NotificationType.BOOKING -> Color(0xFF4CAF50)
                        NotificationType.PAYMENT -> Color(0xFF2196F3)
                        NotificationType.OFFER -> Color(0xFFFF9800)
                        NotificationType.GENERAL -> Color(0xFF9C27B0)
                    },
                    modifier = Modifier.size(24.dp)
                )
            }
        }


        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = notification.title,
                    fontSize = 15.sp,
                    fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )

                if (!notification.isRead) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }

            Text(
                text = notification.message,
                fontSize = 13.sp,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = notification.timestamp,
                fontSize = 11.sp,
                color = Color.Gray.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    MaterialTheme {
        NotificationsScreen()
    }
}