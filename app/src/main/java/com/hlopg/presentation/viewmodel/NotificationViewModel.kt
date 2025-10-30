package com.hlopg.presentation.viewmodel


import androidx.lifecycle.ViewModel
import com.hlopg.data.model.NotificationIcon
import com.hlopg.data.model.NotificationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.collections.map

data class NotificationsUiState(
    val notifications: List<NotificationItem> = emptyList(),
    val unreadCount: Int = 0,
    val notificationsEnabled: Boolean = true
)

class NotificationsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationsUiState())
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        val notifications = listOf(
            NotificationItem(
                id = "1",
                title = "Booking Confirmed",
                description = "Your booking for Universe PG(Co-Living)...",
                icon = NotificationIcon.BOOKING,
                isRead = false
            ),
            NotificationItem(
                id = "2",
                title = "Payment Successful",
                description = "Your payment has been successful...",
                icon = NotificationIcon.PAYMENT,
                isRead = false
            ),
            NotificationItem(
                id = "3",
                title = "Special offer",
                description = "Get 10% on Next Booking using code...",
                icon = NotificationIcon.OFFER,
                isRead = true
            ),
            NotificationItem(
                id = "4",
                title = "New Pg Added",
                description = "A new Pg has been added at your location...",
                icon = NotificationIcon.NEW_PG,
                isRead = true
            )
        )

        _uiState.update {
            it.copy(
                notifications = notifications,
                unreadCount = notifications.count { !it.isRead }
            )
        }
    }

    fun markAllAsRead() {
        _uiState.update { state ->
            state.copy(
                notifications = state.notifications.map { it.copy(isRead = true) },
                unreadCount = 0
            )
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        _uiState.update { it.copy(notificationsEnabled = enabled) }
    }

    fun markAsRead(notificationId: String) {
        _uiState.update { state ->
            val updatedNotifications = state.notifications.map { notification ->
                if (notification.id == notificationId) {
                    notification.copy(isRead = true)
                } else {
                    notification
                }
            }
            state.copy(
                notifications = updatedNotifications,
                unreadCount = updatedNotifications.count { !it.isRead }
            )
        }
    }
}
