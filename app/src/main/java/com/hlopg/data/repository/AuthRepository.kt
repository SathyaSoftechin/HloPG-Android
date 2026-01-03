package com.hlopg.data.repository

import com.hlopg.data.api.AuthApi
import com.hlopg.data.model.ApiResponse
import com.hlopg.data.model.LoginRequest
import com.hlopg.data.model.OtpRequest
import com.hlopg.data.model.RegisterRequest
import com.hlopg.data.model.ResendOtpRequest
import com.hlopg.data.model.User
import com.hlopg.domain.repository.Resource
import com.hlopg.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

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
                val response = api.loginUser(req)
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

    suspend fun resendOtp(req: ResendOtpRequest): Resource<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.resendOtp(req)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Resource.Success(body.message ?: "OTP sent successfully")
                    } else {
                        Resource.Error("Unknown error")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Resource.Error(errorBody ?: "Error ${response.code()}")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Failed to resend OTP")
            }
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

    private suspend fun <T> handleApiResponse(
        response: Response<ApiResponse<T>>
    ): Resource<T> {
        return if (response.isSuccessful) {
            val body = response.body()

            if (body?.user != null) {
                // Save token to TokenManager
                body.token?.let { tokenManager.saveToken(it) }

                // Return success with BOTH user data AND token
                Resource.Success(
                    data = body.user,
                    token = body.token  // ← Include token in Resource
                )
            } else {
                Resource.Error(body?.message ?: "Unknown error")
            }

        } else {
            val errorBody = response.errorBody()?.string()
            Resource.Error(errorBody ?: "Error ${response.code()}")
        }
    }
}