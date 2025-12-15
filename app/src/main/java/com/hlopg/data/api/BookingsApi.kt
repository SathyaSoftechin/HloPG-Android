package com.hlopg.data.api

import com.hlopg.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface BookingApi {

    @POST("api/booking/newbooking")
    suspend fun createNewBooking(
        @Body request: NewBookingRequest
    ): Response<ApiResponse<Booking>>

    @POST("api/booking/confirm-booking")
    suspend fun confirmBooking(
        @Body request: ConfirmBookingRequest
    ): Response<ApiResponse<Booking>>

    @GET("api/booking/getUserBookings")
    suspend fun getUserBookings(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<Booking>>>

    @GET("api/booking/pg/{hostelId}")
    suspend fun getBookingsForHostel(
        @Path("hostelId") hostelId: Int
    ): Response<ApiResponse<List<Booking>>>
}
