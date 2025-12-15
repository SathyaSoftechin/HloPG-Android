package com.hlopg.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hlopg.data.model.NotificationIcon
import com.hlopg.data.model.NotificationItem
import com.hlopg.presentation.viewmodel.NotificationsViewModel
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.isOn


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()




    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Notifications",
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    IosToggle(
                        isOn = uiState.notificationsEnabled,
                        onToggle = { viewModel.toggleNotifications(it) }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            item {
                NotificationHeaderCard(
                    unreadCount = uiState.unreadCount,
                    onMarkAllAsRead = { viewModel.markAllAsRead() }
                )
            }

            items(uiState.notifications) { notification ->
                NotificationItemCard(
                    notification = notification,
                    onClick = { viewModel.markAsRead(notification.id) }
                )
            }
        }
    }
}


@Composable
fun IosToggle(
    isOn: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val trackColor = if (isOn) Color(0xFF34C759) else Color(0xFFCCCCCC) // iOS green
    val thumbOffset = if (isOn) 20.dp else 0.dp
    val animatedOffset by animateDpAsState(
        targetValue = if (isOn) 24.dp else 2.dp,
        label = ""
    )

    Box(
        modifier = Modifier
            .width(50.dp)
            .height(28.dp)
            .clip(RoundedCornerShape(50))
            .background(trackColor)
            .clickable { onToggle(!isOn) },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = animatedOffset)
                .size(24.dp)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}


/**
 * Purple header card (Notifications + unread + Mark all as read)
 */
@Composable
fun NotificationHeaderCard(
    unreadCount: Int,
    onMarkAllAsRead: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF7C4DFF) // purple like screenshot
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Notifications",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                Text(
                    text = "$unreadCount unread messages",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 15.sp
                )
            }

            TextButton(
                onClick = onMarkAllAsRead,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text(text = "Mark all as read")
            }
        }
    }
}

/**
 * Single notification card row
 */
@Composable
fun NotificationItemCard(
    notification: NotificationItem,
    onClick: () -> Unit
) {
    val containerColor =
        if (!notification.isRead) Color(0xFFEDE7FF) // light purple for unread (first card style)
        else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NotificationIconBox(icon = notification.icon)

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.description,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            }

            if (!notification.isRead) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEEEFC))
                )
            }
        }
    }
}

/**
 * Colored icon box at left of each notification
 */
@Composable
fun NotificationIconBox(icon: NotificationIcon) {
    val (iconVector, backgroundColor, iconColor) = when (icon) {
        NotificationIcon.BOOKING ->
            Triple(Icons.Default.CalendarToday, Color(0xFFE8F5E9), Color(0xFF4CAF50))
        NotificationIcon.PAYMENT  ->
            Triple(Icons.Default.CreditCard, Color(0xFFE3F2FD), Color(0xFF2196F3))
        NotificationIcon.OFFER    ->
            Triple(Icons.Default.LocalOffer, Color(0xFFFFF9C4), Color(0xFFFFB300))
        NotificationIcon.NEW_PG   ->
            Triple(Icons.Default.LocationOn, Color(0xFFF3E5F5), Color(0xFF9C27B0))
    }

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = iconVector,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    // This will use default viewModel(), you can instead create a fake UI state preview if needed
    NotificationsScreen()
}
