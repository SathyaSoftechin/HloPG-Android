package com.hlopg.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hlopg.data.api.ApiClient
import com.hlopg.data.model.LoginRequest
// Remove this import: import com.hlopg.data.model.LoginRequest
import com.hlopg.data.model.LoginResponse
import com.hlopg.utils.SecureStorage
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun login(username: String, password: String, context: Context) {
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Starting login request")
                Log.d("LoginViewModel", "Username: $username")
                Log.d("LoginViewModel", "Password: $password")

                val loginRequest = LoginRequest(
                    username = username,
                    password = password,
                    expiresInMins = 30
                )
                val response: Response<LoginResponse> = ApiClient.instance.login(loginRequest)


                Log.d("LoginViewModel", "Response received")
                Log.d("LoginViewModel", "Response code: ${response.code()}")
                Log.d("LoginViewModel", "Response message: ${response.message()}")

                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        Log.d("LoginViewModel", "Login successful: $loginResponse")
                        Log.d("LoginViewModel", "Access token: ${loginResponse.accessToken}")

                        // Save tokens securely
                        SecureStorage.saveTokens(context, loginResponse.accessToken, loginResponse.refreshToken)
                        _loginResult.postValue(Result.success(loginResponse))
                    } ?: run {
                        Log.e("LoginViewModel", "Response body is null")
                        _loginResult.postValue(Result.failure(Exception("Empty response")))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("LoginViewModel", "Login failed - Code: ${response.code()}")
                    Log.e("LoginViewModel", "Error message: ${response.message()}")
                    Log.e("LoginViewModel", "Error body: $errorBody")
                    _loginResult.postValue(Result.failure(Exception("Error: ${response.code()} - $errorBody")))
                }

            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login exception:", e)
                _loginResult.postValue(Result.failure(e))
            }
        }
    }
}