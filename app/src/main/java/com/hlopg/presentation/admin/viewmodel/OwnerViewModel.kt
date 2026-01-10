package com.hlopg.presentation.admin.viewmodel


import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hlopg.data.model.LoginRequest
import com.hlopg.data.model.Owner
import com.hlopg.data.repository.OwnerAuthRepository
import com.hlopg.domain.repository.Resource
import com.hlopg.utils.SessionManager
import com.hlopg.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerLoginViewModel @Inject constructor(
    private val repository: OwnerAuthRepository,
    private val sessionManager: SessionManager,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<Owner>>()
    val loginResult: LiveData<Result<Owner>> = _loginResult

    fun login(
        emailOrPhone: String,
        password: String,
        onSuccess: (String) -> Unit,  // Returns user_type ("owner")
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                Log.d("OwnerLoginViewModel", "Starting owner login request")

                val loginRequest = LoginRequest(
                    identifier = emailOrPhone,
                    password = password
                )

                val result = repository.loginOwner(loginRequest)

                when (result) {
                    is Resource.Success -> {
                        val ownerData = result.data
                        val token = result.token ?: ownerData?.token ?: ""

                        Log.d("OwnerLoginViewModel", "Owner login success")
                        Log.d("OwnerLoginViewModel", "Owner ID: ${ownerData?.id}")
                        Log.d("OwnerLoginViewModel", "Owner Name: ${ownerData?.name}")
                        Log.d("OwnerLoginViewModel", "User Type: ${ownerData?.userType}")
                        Log.d("OwnerLoginViewModel", "Token: ${token.take(20)}...")

                        // Save session
                        if (ownerData != null) {
                            sessionManager.saveLoginSession(
                                userId = ownerData.id.toString(),
                                userName = ownerData.name,
                                userEmail = ownerData.email,
                                userPhone = ownerData.phone,
                                userType = ownerData.userType,
                                authToken = token
                            )

                            Log.d("OwnerLoginViewModel", "Session saved successfully")
                            Log.d("OwnerLoginViewModel", "SessionManager.isOwner(): ${sessionManager.isOwner()}")
                        }

                        _loginResult.postValue(Result.success(ownerData!!))
                        onSuccess(ownerData.userType)

                    }
                    is Resource.Error -> {
                        Log.e("OwnerLoginViewModel", "Owner login failed: ${result.message}")
                        _loginResult.postValue(Result.failure(Exception(result.message)))
                        onError(result.message ?: "Login failed")
                    }
                    is Resource.Loading -> {
                        Log.d("OwnerLoginViewModel", "Owner login loading...")
                    }
                }

            } catch (e: Exception) {
                Log.e("OwnerLoginViewModel", "Exception during owner login", e)
                _loginResult.postValue(Result.failure(e))
                onError(e.message ?: "An error occurred")
            }
        }
    }
}

// Legacy ViewModel for backward compatibility (without Hilt)
class OwnerLoginViewModelLegacy : ViewModel() {

    private val _loginResult = MutableLiveData<Result<Owner>>()
    val loginResult: LiveData<Result<Owner>> = _loginResult

    fun login(emailOrPhone: String, password: String, context: Context) {
        viewModelScope.launch {
            try {
                Log.d("OwnerLoginViewModel", "Starting owner login request")

                val tokenManager = TokenManager(context)
                val sessionManager = SessionManager(context)
                val repository = OwnerAuthRepository(
                    api = com.hlopg.data.api.RetrofitInstance.authApi,
                    tokenManager = tokenManager
                )

                val loginRequest = LoginRequest(
                    identifier = emailOrPhone,
                    password = password
                )

                val result = repository.loginOwner(loginRequest)

                when (result) {
                    is Resource.Success -> {
                        val ownerData = result.data
                        val token = result.token ?: ownerData?.token ?: ""

                        Log.d("OwnerLoginViewModel", "Owner login success")
                        Log.d("OwnerLoginViewModel", "Owner ID: ${ownerData?.id}")
                        Log.d("OwnerLoginViewModel", "Owner Name: ${ownerData?.name}")
                        Log.d("OwnerLoginViewModel", "User Type: ${ownerData?.userType}")

                        if (ownerData != null) {
                            sessionManager.saveLoginSession(
                                userId = ownerData.id.toString(),
                                userName = ownerData.name,
                                userEmail = ownerData.email,
                                userPhone = ownerData.phone,
                                userType = ownerData.userType,
                                authToken = token
                            )

                            Log.d("OwnerLoginViewModel", "Session saved successfully")
                            Log.d("OwnerLoginViewModel", "SessionManager.isOwner(): ${sessionManager.isOwner()}")
                        }

                        _loginResult.postValue(Result.success(ownerData!!))

                    }
                    is Resource.Error -> {
                        Log.e("OwnerLoginViewModel", "Owner login failed: ${result.message}")
                        _loginResult.postValue(Result.failure(Exception(result.message)))
                    }
                    is Resource.Loading -> {
                        Log.d("OwnerLoginViewModel", "Owner login loading...")
                    }
                }

            } catch (e: Exception) {
                Log.e("OwnerLoginViewModel", "Exception during owner login", e)
                _loginResult.postValue(Result.failure(e))
            }
        }
    }
}