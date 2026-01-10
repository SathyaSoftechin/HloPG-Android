package com.hlopg.data.repository


import com.hlopg.data.api.AuthApi
import com.hlopg.data.model.ApiResponse
import com.hlopg.data.model.LoginRequest
import com.hlopg.data.model.Owner
import com.hlopg.domain.repository.Resource
import com.hlopg.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class OwnerAuthRepository @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) {

//    suspend fun registerOwner(req: RegisterOwnerRequest): Resource<Owner> =
//        safeApiCall {
//            api.registerOwner(req)
//        }

    suspend fun loginOwner(req: LoginRequest): Resource<Owner> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.loginOwner(req)
                handleApiResponse(response)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Owner login failed")
            }
        }
    }

//    suspend fun getOwner(): Resource<Owner> =
//        withContext(Dispatchers.IO) {
//            val token = tokenManager.tokenFlow.first()
//                ?: return@withContext Resource.Error("No authentication token found")
//
//            safeApiCall {
//                api.getOwner("Bearer $token")
//            }
//        }

    // ================= TOKEN =================

    suspend fun saveToken(token: String) = tokenManager.saveToken(token)
    suspend fun clearToken() = tokenManager.clearToken()
    fun getTokenFlow(): Flow<String?> = tokenManager.tokenFlow

    suspend fun isOwnerLoggedIn(): Boolean =
        !tokenManager.tokenFlow.first().isNullOrEmpty()

    // ================= SAFE API CALL =================
//
//    private suspend fun <T> safeApiCall(
//        apiCall: suspend () -> Response<ApiResponse<T>>
//    ): Resource<T> {
//        return withContext(Dispatchers.IO) {
//            try {
//                handleApiResponse(apiCall())
//            } catch (e: Exception) {
//                Resource.Error(e.message ?: "Network error")
//            }
//        }
//    }

    private suspend fun handleApiResponse(
        response: Response<ApiResponse<Owner>>
    ): Resource<Owner> {
        return if (response.isSuccessful) {
            val body = response.body()

            // Check if response contains owner data in 'user' field (some APIs use this)
            val ownerData = body?.user

            if (ownerData != null) {
                // Save token to TokenManager
                body.token?.let { tokenManager.saveToken(it) }

                // Return success with BOTH owner data AND token
                Resource.Success(
                    data = ownerData,
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