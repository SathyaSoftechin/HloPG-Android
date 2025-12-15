package com.hlopg.data.model


data class Booking(
    val id: Int,
    val userId: Int,
    val hostelId: Int,
    val sharing: Int,
    val totalAmount: Double,
    val priceType: String?,
    val numDays: Int?,
    val date: String?,
    val rentAmount: Double?,
    val deposit: Double?,
    val status: String
)

data class NewBookingRequest(
    val userId: Int,
    val hostelId: Int,
    val sharing: Int,
    val totalAmount: Double,
    val priceType: String? = null,
    val numDays: Int? = null,
    val date: String? = null,
    val rentAmount: Double? = null,
    val deposit: Double? = null
)

data class ConfirmBookingPayment(
    val razorpay_order_id: String,
    val razorpay_payment_id: String,
    val razorpay_signature: String
)

data class ConfirmBookingData(
    val bookingId: Int
)

data class ConfirmBookingRequest(
    val bookingData: ConfirmBookingData,
    val payment: ConfirmBookingPayment
)
