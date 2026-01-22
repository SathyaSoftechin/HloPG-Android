package com.hlopg.data.repository


import com.hlopg.data.api.AuthApi
import com.hlopg.data.model.LoginRequest
import com.hlopg.data.model.Owner
import com.hlopg.domain.repository.Resource
import com.hlopg.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OwnerAuthRepository @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) {


    suspend fun loginOwner(req: LoginRequest): Resource<Owner> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.loginOwner(req)

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body != null) {
                        tokenManager.saveToken(body.token)

                        Resource.Success(
                            data = body.owner,
                            token = body.token
                        )
                    } else {
                        Resource.Error("Empty response body")
                    }
                } else {
                    Resource.Error(response.errorBody()?.string())
                }

            } catch (e: Exception) {
                Resource.Error(e.message ?: "Owner login failed")
            }
        }
    }

}