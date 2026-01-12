package com.hlopg.presentation.admin.viewmodel

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerLoginViewModel @Inject constructor(
    private val repository: OwnerAuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<Owner>>()
    val loginResult: LiveData<Result<Owner>> = _loginResult

    fun login(
        emailOrPhone: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                Log.d("OwnerLoginViewModel", "Starting owner login request")

                val result = repository.loginOwner(
                    LoginRequest(
                        identifier = emailOrPhone,
                        password = password
                    )
                )

                when (result) {
                    is Resource.Success -> {
                        val owner = result.data
                        val token = result.token ?: ""

                        if (owner == null) {
                            _loginResult.postValue(Result.failure(Exception("Owner data missing")))
                            return@launch
                        }

                        Log.d("OwnerLoginViewModel", "Owner login success")
                        Log.d("OwnerLoginViewModel", "Owner ID: ${owner.id}")
                        Log.d("OwnerLoginViewModel", "Owner Name: ${owner.name}")
                        Log.d("OwnerLoginViewModel", "User Type: ${owner.userType}")
                        Log.d("OwnerLoginViewModel", "Token: ${token.take(20)}...")

                        // Save session
                        sessionManager.saveLoginSession(
                            userId = owner.id,
                            userName = owner.name,
                            userEmail = owner.email,
                            userPhone = null,
                            userType = owner.userType,
                            authToken = token
                        )

                        Log.d("OwnerLoginViewModel", "Session saved successfully")
                        Log.d(
                            "OwnerLoginViewModel",
                            "SessionManager.isOwner(): ${sessionManager.isOwner()}"
                        )

                        _loginResult.postValue(Result.success(owner))
                    }

                    is Resource.Error -> {
                        Log.e(
                            "OwnerLoginViewModel",
                            "Owner login failed: ${result.message}"
                        )
                        _loginResult.postValue(Result.failure(Exception(result.message ?: "Login failed")))
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