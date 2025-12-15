package com.hlopg.data.repository

import com.hlopg.data.api.AuthApi
import com.hlopg.data.model.*
import com.hlopg.domain.repository.Resource
import com.hlopg.utils.TokenManager
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.Response

class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) {

    suspend fun registerUser(req: RegisterRequest): Resource<User> =
        safeApiCall {
            api.registerUser(req)
        }

    suspend fun loginUser(req: LoginRequest): Resource<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.loginUser(
                    email = req.email,
                    password = req.password
                )
                handleApiResponse(response)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Login failed")
            }
        }
    }


    suspend fun verifyOtp(req: OtpRequest): Resource<User> =
        safeApiCall {
            api.verifyOtp(req)
        }

    suspend fun resendOtp(req: ResendOtpRequest): Resource<String> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.resendOtp(req)
                if (response.isSuccessful && response.body()?.success == true) {
                    Resource.Success(response.body()?.message ?: "OTP sent")
                } else {
                    Resource.Error(response.body()?.message ?: "Failed to resend OTP")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Failed to resend OTP")
            }
        }

    suspend fun getUser(): Resource<User> =
        withContext(Dispatchers.IO) {
            val token = tokenManager.tokenFlow.first()
                ?: return@withContext Resource.Error("No authentication token found")

            safeApiCall {
                api.getUser("Bearer $token")
            }
        }

    // ================= TOKEN =================

    suspend fun saveToken(token: String) = tokenManager.saveToken(token)
    suspend fun clearToken() = tokenManager.clearToken()
    fun getTokenFlow(): Flow<String?> = tokenManager.tokenFlow

    suspend fun isUserLoggedIn(): Boolean =
        !tokenManager.tokenFlow.first().isNullOrEmpty()

    // ================= SAFE API CALL =================

    private suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<ApiResponse<T>>
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                handleApiResponse(apiCall())
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Network error")
            }
        }
    }

    private fun <T> handleApiResponse(
        response: Response<ApiResponse<T>>
    ): Resource<T> {
        return if (response.isSuccessful) {
            val body = response.body()
            if (body?.success == true && body.data != null) {
                Resource.Success(body.data)
            } else {
                Resource.Error(body?.message ?: "Unknown error occurred")
            }
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = when {
                !errorBody.isNullOrEmpty() -> errorBody
                response.code() == 400 -> "Bad request"
                response.code() == 401 -> "Invalid credentials"
                response.code() == 403 -> "Forbidden"
                response.code() == 404 -> "Not found"
                response.code() == 500 -> "Server error"
                else -> "Error ${response.code()}"
            }
            Resource.Error(errorMessage)
        }
    }
}
