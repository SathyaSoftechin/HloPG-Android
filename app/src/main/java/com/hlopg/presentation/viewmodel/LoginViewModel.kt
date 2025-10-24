package com.hlopg.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hlopg.data.api.RetrofitInstance
import com.hlopg.data.model.ApiResponse
import com.hlopg.data.model.LoginRequest
import com.hlopg.data.model.User
import com.hlopg.utils.TokenManager
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<Result<ApiResponse<User>>>()
    val loginResult: LiveData<Result<ApiResponse<User>>> = _loginResult

    fun login(emailOrPhone: String, password: String, context: Context) {
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Starting login request")
                val loginRequest = LoginRequest(
                    email = emailOrPhone,
                    password = password
                )

                val response: Response<ApiResponse<User>> = RetrofitInstance.api.loginUser(loginRequest)

                Log.d("LoginViewModel", "Response code: ${response.code()}")

                if (response.isSuccessful) {
                    response.body()?.let { apiResponse ->
                        Log.d("LoginViewModel", "Login success: ${apiResponse.token}")

                        // Save token if it exists
                        apiResponse.token?.let { token ->
                            val tokenManager = TokenManager(context)
                            tokenManager.saveToken(token)
                        }

                        _loginResult.postValue(Result.success(apiResponse))
                    } ?: run {
                        Log.e("LoginViewModel", "Response body null")
                        _loginResult.postValue(Result.failure(Exception("Empty response")))
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("LoginViewModel", "Login failed: $error")
                    _loginResult.postValue(Result.failure(Exception("Error ${response.code()}: $error")))
                }

            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception during login", e)
                _loginResult.postValue(Result.failure(e))
            }
        }
    }
}