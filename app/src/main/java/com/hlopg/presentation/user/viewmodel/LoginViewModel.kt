package com.hlopg.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hlopg.data.model.ApiResponse
import com.hlopg.data.model.LoginRequest
import com.hlopg.data.model.User
import com.hlopg.data.repository.AuthRepository
import com.hlopg.domain.repository.Resource
import com.hlopg.utils.SessionManager
import com.hlopg.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<ApiResponse<User>>>()
    val loginResult: LiveData<Result<ApiResponse<User>>> = _loginResult

    fun login(
        emailOrPhone: String,
        password: String,
        onSuccess: (String) -> Unit,  // Returns user_type
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Starting login request")

                val loginRequest = LoginRequest(
                    identifier = emailOrPhone,
                    password = password
                )

                val result = repository.loginUser(loginRequest)

                when (result) {
                    is Resource.Success -> {
                        val userData = result.data
                        val token = result.token ?: ""

                        Log.d("LoginViewModel", "Login success")
                        Log.d("LoginViewModel", "User ID: ${userData?.id}")
                        Log.d("LoginViewModel", "User Name: ${userData?.name}")
                        Log.d("LoginViewModel", "User Type: ${userData?.userType}")
                        Log.d("LoginViewModel", "Token: ${token.take(20)}...")

                        // Session is already saved in ViewModel, so we save it here too
                        if (userData != null) {
                            sessionManager.saveLoginSession(
                                userId = (userData.id ?: "").toString(),
                                userName = userData.name ?: "",
                                userEmail = userData.email ?: "",
                                userPhone = userData.phone ?: emailOrPhone,
                                userType = userData.userType ?: "user",  // "user" or "admin"
                                authToken = token
                            )

                            Log.d("LoginViewModel", "Session saved successfully")
                            Log.d("LoginViewModel", "SessionManager.isAdmin(): ${sessionManager.isAdmin()}")
                            Log.d("LoginViewModel", "SessionManager.getUserType(): ${sessionManager.getUserType()}")
                        }

                        // Create ApiResponse for backward compatibility
                        val apiResponse = ApiResponse(
                            message = "Login successful",
                            data = userData,
                            token = token,
                            user = userData
                        )

                        _loginResult.postValue(Result.success(apiResponse))

                        // Return user type for navigation
                        val userType = userData?.userType ?: "user"
                        onSuccess(userType)

                    }
                    is Resource.Error -> {
                        Log.e("LoginViewModel", "Login failed: ${result.message}")
                        _loginResult.postValue(Result.failure(Exception(result.message)))
                        onError(result.message ?: "Login failed")
                    }
                    is Resource.Loading -> {
                        Log.d("LoginViewModel", "Login loading...")
                    }
                }

            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception during login", e)
                _loginResult.postValue(Result.failure(e))
                onError(e.message ?: "An error occurred")
            }
        }
    }
}

// Legacy ViewModel for backward compatibility (without Hilt)
class LoginViewModelLegacy : ViewModel() {

    private val _loginResult = MutableLiveData<Result<ApiResponse<User>>>()
    val loginResult: LiveData<Result<ApiResponse<User>>> = _loginResult

    fun login(emailOrPhone: String, password: String, context: Context) {
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Starting login request")

                // Create instances (not ideal, use DI in production)
                val tokenManager = TokenManager(context)
                val sessionManager = SessionManager(context)
                val repository = AuthRepository(
                    api = com.hlopg.data.api.RetrofitInstance.authApi,
                    tokenManager = tokenManager
                )

                val loginRequest = LoginRequest(
                    identifier = emailOrPhone,
                    password = password
                )

                val result = repository.loginUser(loginRequest)

                when (result) {
                    is Resource.Success -> {
                        val userData = result.data
                        val token = result.token ?: ""

                        Log.d("LoginViewModel", "Login success")
                        Log.d("LoginViewModel", "User ID: ${userData?.id}")
                        Log.d("LoginViewModel", "User Name: ${userData?.name}")
                        Log.d("LoginViewModel", "User Type: ${userData?.userType}")
                        Log.d("LoginViewModel", "Token: ${token.take(20)}...")

                        // Save session to SessionManager
                        if (userData != null) {
                            sessionManager.saveLoginSession(
                                userId = (userData.id ?: "").toString(),
                                userName = userData.name ?: "",
                                userEmail = userData.email ?: "",
                                userPhone = userData.phone ?: emailOrPhone,
                                userType = userData.userType ?: "user",  // "user" or "admin"
                                authToken = token
                            )

                            Log.d("LoginViewModel", "Session saved successfully")
                            Log.d("LoginViewModel", "SessionManager.isAdmin(): ${sessionManager.isAdmin()}")
                            Log.d("LoginViewModel", "SessionManager.getUserType(): ${sessionManager.getUserType()}")
                        }

                        // Create ApiResponse for backward compatibility
                        val apiResponse = ApiResponse(
                            message = "Login successful",
                            data = userData,
                            token = token,
                            user = userData
                        )

                        _loginResult.postValue(Result.success(apiResponse))

                    }
                    is Resource.Error -> {
                        Log.e("LoginViewModel", "Login failed: ${result.message}")
                        _loginResult.postValue(Result.failure(Exception(result.message)))
                    }
                    is Resource.Loading -> {
                        Log.d("LoginViewModel", "Login loading...")
                    }
                }

            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception during login", e)
                _loginResult.postValue(Result.failure(e))
            }
        }
    }
}