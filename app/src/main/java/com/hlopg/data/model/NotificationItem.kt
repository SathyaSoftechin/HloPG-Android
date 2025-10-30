package com.hlopg.data.model


data class NotificationItem(
    val id: String,
    val title: String,
    val description: String,
    val icon: NotificationIcon,
    val isRead: Boolean = false
)

enum class NotificationIcon {
    BOOKING, PAYMENT, OFFER, NEW_PG
}