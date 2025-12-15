package com.hlopg.data.api

import com.hlopg.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface RoomsApi {

    @POST("api/rooms/bulkCreate")
    suspend fun bulkCreateRooms(
        @Body request: BulkCreateRoomsRequest
    ): Response<ApiResponse<Any>>

    @GET("api/rooms/{hostel_id}")
    suspend fun getRooms(
        @Path("hostel_id") hostelId: Int
    ): Response<ApiResponse<List<Room>>>

    @PUT("api/rooms/{hostel_id}")
    suspend fun updateRooms(
        @Path("hostel_id") hostelId: Int,
        @Body request: UpdateRoomsRequest
    ): Response<ApiResponse<Any>>

    @PUT("api/rooms/save/{hostel_id}")
    suspend fun smartSaveRooms(
        @Path("hostel_id") hostelId: Int,
        @Body request: SmartSaveRoomsRequest
    ): Response<ApiResponse<Any>>
}
