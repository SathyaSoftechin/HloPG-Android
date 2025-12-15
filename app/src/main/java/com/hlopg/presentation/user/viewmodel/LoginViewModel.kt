package com.hlopg.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hlopg.data.model.ApiResponse
import com.hlopg.data.model.LoginRequest
import com.hlopg.data.model.User
import com.hlopg.data.repository.AuthRepository
import com.hlopg.domain.repository.Resource
import com.hlopg.utils.TokenManager
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginResult = MutableLiveData<Resource<User>>()
    val loginResult: LiveData<Resource<User>> = _loginResult

    fun login(emailOrPhone: String, password: String) {
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Starting login request")
                _loginResult.postValue(Resource.Loading)

                val loginRequest = LoginRequest(
                    email = emailOrPhone,
                    password = password
                )

                val result = repository.loginUser(loginRequest)

                when (result) {
                    is Resource.Success -> {
                        Log.d("LoginViewModel", "Login success")
                        _loginResult.postValue(result)
                    }
                    is Resource.Error -> {
                        Log.e("LoginViewModel", "Login failed: ${result.message}")
                        _loginResult.postValue(result)
                    }
                    is Resource.Loading -> {
                        // Already handled above
                    }
                }

            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception during login", e)
                _loginResult.postValue(Resource.Error(e.message ?: "Login failed"))
            }
        }
    }
}

// Alternative constructor for backward compatibility (if not using DI)
class LoginViewModelLegacy : ViewModel() {

    private val _loginResult = MutableLiveData<Result<ApiResponse<User>>>()
    val loginResult: LiveData<Result<ApiResponse<User>>> = _loginResult

    fun login(emailOrPhone: String, password: String, context: Context) {
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Starting login request")

                // Create repository instance (not ideal, use DI in production)
                val tokenManager = TokenManager(context)
                val repository = AuthRepository(
                    api = com.hlopg.data.api.RetrofitInstance.authApi,
                    tokenManager = tokenManager
                )

                val loginRequest = LoginRequest(
                    email = emailOrPhone,
                    password = password
                )

                val result = repository.loginUser(loginRequest)

                when (result) {
                    is Resource.Success -> {
                        Log.d("LoginViewModel", "Login success")

                        // Create ApiResponse for backward compatibility
                        val apiResponse = ApiResponse(
                            success = true,
                            message = "Login successful",
                            data = result.data,
                            token = null // Token is handled in repository
                        )

                        _loginResult.postValue(Result.success(apiResponse))
                    }
                    is Resource.Error -> {
                        Log.e("LoginViewModel", "Login failed: ${result.message}")
                        _loginResult.postValue(Result.failure(Exception(result.message)))
                    }
                    is Resource.Loading -> {
                        // Loading state
                    }
                }

            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception during login", e)
                _loginResult.postValue(Result.failure(e))
            }
        }
    }
}