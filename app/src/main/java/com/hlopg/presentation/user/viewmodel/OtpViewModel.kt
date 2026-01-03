package com.hlopg.presentation.user.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hlopg.data.model.OtpRequest
import com.hlopg.data.model.ResendOtpRequest
import com.hlopg.data.model.User
import com.hlopg.data.repository.AuthRepository
import com.hlopg.domain.repository.Resource
import com.hlopg.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OTPViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _otpResult = MutableLiveData<Resource<User>>()
    val otpResult: LiveData<Resource<User>> = _otpResult

    private val _resendResult = MutableLiveData<Resource<String>>()
    val resendResult: LiveData<Resource<String>> = _resendResult

    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> = _phoneNumber

    fun setPhoneNumber(phone: String) {
        _phoneNumber.value = phone
    }

    fun verifyOTP(phoneNumber: String, otp: String, purpose: String = "register", userType: String = "user") {
        viewModelScope.launch {
            try {
                Log.d("OTPViewModel", "Starting OTP verification for $phoneNumber with purpose: $purpose")
                _otpResult.postValue(Resource.Loading())  // ← Added parentheses

                val otpRequest = OtpRequest(
                    identifier = phoneNumber,
                    otp_code = otp,
                    purpose = purpose
                )

                Log.d("OTPViewModel", "OTP Request: identifier=$phoneNumber, otp=$otp, purpose=$purpose")

                val result = repository.verifyOtp(otpRequest)

                when (result) {
                    is Resource.Success -> {
                        val userData = result.data
                        val token = result.token ?: ""

                        Log.d("OTPViewModel", "OTP verification success")
                        Log.d("OTPViewModel", "User ID: ${userData?.id}")
                        Log.d("OTPViewModel", "User Name: ${userData?.name}")
                        Log.d("OTPViewModel", "User Type: ${userData?.userType}")

                        // Save session after successful OTP verification
                        if (userData != null) {
                            sessionManager.saveLoginSession(
                                userId = userData.id?.toString() ?: "",
                                userName = userData.name ?: "",
                                userEmail = userData.email ?: "",
                                userPhone = userData.phone ?: phoneNumber,
                                userType = userData.userType ?: userType,
                                authToken = token
                            )

                            Log.d("OTPViewModel", "Session saved after OTP verification")
                            Log.d("OTPViewModel", "SessionManager.isAdmin(): ${sessionManager.isAdmin()}")
                        }

                        _otpResult.postValue(result)
                    }
                    is Resource.Error -> {
                        Log.e("OTPViewModel", "OTP verification failed: ${result.message}")
                        _otpResult.postValue(result)
                    }
                    is Resource.Loading -> {
                        // Already handled above
                    }
                }

            } catch (e: Exception) {
                Log.e("OTPViewModel", "Exception during OTP verification", e)
                _otpResult.postValue(Resource.Error(e.message ?: "OTP verification failed"))
            }
        }
    }

    fun resendOTP(phoneNumber: String, purpose: String = "register") {
        viewModelScope.launch {
            try {
                Log.d("OTPViewModel", "Resending OTP to $phoneNumber with purpose: $purpose")
                _resendResult.postValue(Resource.Loading())  // ← Added parentheses

                // Call the actual API
                val result = repository.resendOtp(ResendOtpRequest(
                    identifier = phoneNumber,
                    purpose = purpose
                ))

                when (result) {
                    is Resource.Success -> {
                        Log.d("OTPViewModel", "OTP resent successfully")
                        _resendResult.postValue(Resource.Success("OTP sent successfully"))
                    }
                    is Resource.Error -> {
                        Log.e("OTPViewModel", "Failed to resend OTP: ${result.message}")
                        _resendResult.postValue(Resource.Error(result.message ?: "Failed to resend OTP"))
                    }
                    is Resource.Loading -> {
                        // Already handled
                    }
                }

            } catch (e: Exception) {
                Log.e("OTPViewModel", "Exception during OTP resend", e)
                _resendResult.postValue(Resource.Error(e.message ?: "Failed to resend OTP"))
            }
        }
    }

    fun clearOtpResult() {
        _otpResult.value = null
    }

    fun clearResendResult() {
        _resendResult.value = null
    }
}