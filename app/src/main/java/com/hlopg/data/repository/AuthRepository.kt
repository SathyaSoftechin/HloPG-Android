package com.hlopg.data.repository

import com.hlopg.data.api.AuthApi
import com.hlopg.data.model.ApiResponse
import com.hlopg.data.model.LoginRequest
import com.hlopg.data.model.OtpRequest
import com.hlopg.data.model.RegisterRequest
import com.hlopg.data.model.User
import com.hlopg.utils.TokenManager
import kotlinx.coroutines.flow.first
import retrofit2.Response

class AuthRepository(private val api: AuthApi, private val tokenManager: TokenManager) {

    suspend fun registerUser(req: RegisterRequest) = api.registerUser(req)
    suspend fun loginUser(req: LoginRequest) = api.loginUser(req)
    suspend fun verifyOtp(req: OtpRequest) = api.verifyOtp(req)
    suspend fun resendOtp(identifier: String, purpose: String) =
        api.resendOtp(mapOf("identifier" to identifier, "purpose" to purpose))

    suspend fun getUser() : Response<ApiResponse<User>> {
        val token = tokenManager.tokenFlow.first()
        return api.getUser("Bearer $token")
    }

    suspend fun saveToken(token: String) {
        tokenManager.saveToken(token)
    }
}
