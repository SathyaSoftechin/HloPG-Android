package com.hlopg.data.api

import com.hlopg.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface HostelApi {

    // These 4 endpoints return a JSON ARRAY: [...]
    @GET("api/hostel/gethostels")
    suspend fun getPopularHostels(): List<HostelDto>

    @GET("api/hostel/hydhostels")
    suspend fun getHyderabadHostels(): List<HostelDto>

    @GET("api/hostel/chehostels")
    suspend fun getChennaiHostels(): List<HostelDto>

    @GET("api/hostel/benhostels")
    suspend fun getBangaloreHostels(): List<HostelDto>

    // Below ones you can keep as Response<ApiResponse<...>>
    // IF your backend really wraps them in { success, data, message }
    // (adjust later if needed based on your server response)

    @GET("api/hostel/{hostel_id}")
    suspend fun getHostelById(
        @Path("hostel_id") hostelId: Int
    ): Response<ApiResponse<Hostel>>

    @POST("api/hostel/addhostel")
    suspend fun addHostel(
        @Body request: AddHostelRequest
    ): Response<ApiResponse<Hostel>>

    @PUT("api/hostel/{hostel_id}")
    suspend fun updateHostel(
        @Path("hostel_id") hostelId: Int,
        @Body request: UpdateHostelRequest
    ): Response<ApiResponse<Hostel>>
}
