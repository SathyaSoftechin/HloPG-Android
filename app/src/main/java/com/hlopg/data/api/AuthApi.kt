package com.hlopg.data.api

import com.hlopg.data.model.ApiResponse
import com.hlopg.data.model.LoginRequest
import com.hlopg.data.model.OtpRequest
import com.hlopg.data.model.Owner
import com.hlopg.data.model.RegisterOwnerRequest
import com.hlopg.data.model.RegisterRequest
import com.hlopg.data.model.ResendOtpRequest
import com.hlopg.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    // ================= USER =================

    @POST("api/auth/registeruser")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<ApiResponse<User>>


    @POST("api/auth/apploginuser")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): Response<ApiResponse<User>>

    @POST("api/auth/verify-otp")
    suspend fun verifyOtp(
        @Body request: OtpRequest
    ): Response<ApiResponse<User>>

    @POST("api/auth/resend-otp")
    suspend fun resendOtp(
        @Body request: ResendOtpRequest
    ): Response<ApiResponse<Any>>

    @GET("api/auth/user")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): Response<ApiResponse<User>>

    @GET("api/auth/userid")
    suspend fun getUserDashboard(
        @Header("Authorization") token: String
    ): Response<ApiResponse<Any>>

    // ================= OWNER =================

    @POST("api/auth/registerowner")
    suspend fun registerOwner(
        @Body request: RegisterOwnerRequest
    ): Response<ApiResponse<Owner>>

    @POST("api/auth/loginowner")
    suspend fun loginOwner(
        @Body request: LoginRequest
    ): Response<ApiResponse<Owner>>

    @GET("api/auth/owner")
    suspend fun getOwner(
        @Header("Authorization") token: String
    ): Response<ApiResponse<Owner>>

    @GET("api/auth/ownerid")
    suspend fun getOwnerById(
        @Header("Authorization") token: String
    ): Response<ApiResponse<Any>>
}
