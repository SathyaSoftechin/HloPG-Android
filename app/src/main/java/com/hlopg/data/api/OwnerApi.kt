package com.hlopg.data.api

import com.hlopg.data.model.ApiResponse
import com.hlopg.data.model.OwnerPg
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OwnerApi {

    @GET("api/owner/pgs/{ownerId}")
    suspend fun getOwnerPgs(
        @Path("ownerId") ownerId: Int
    ): Response<ApiResponse<List<OwnerPg>>>
}
