package com.hlopg.presentation.user.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hlopg.data.model.OtpRequest
import com.hlopg.data.model.User
import com.hlopg.data.repository.AuthRepository
import com.hlopg.domain.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OTPViewModel @Inject constructor(
    private val repository: AuthRepository
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

    fun verifyOTP(phoneNumber: String, otp: String, purpose: String = "register") {
        viewModelScope.launch {
            try {
                Log.d("OTPViewModel", "Starting OTP verification for $phoneNumber with purpose: $purpose")
                _otpResult.postValue(Resource.Loading)

                val otpRequest = OtpRequest(
                    identifier = phoneNumber,
                    otp_code = otp,
                    purpose = purpose
                )

                Log.d("OTPViewModel", "OTP Request: identifier=$phoneNumber, otp=$otp, purpose=$purpose")

                val result = repository.verifyOtp(otpRequest)

                when (result) {
                    is Resource.Success -> {
                        Log.d("OTPViewModel", "OTP verification success")
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
                _resendResult.postValue(Resource.Loading)

                // TODO: Implement resendOtp API call when available
                // For now, simulate success
                _resendResult.postValue(Resource.Success("OTP sent successfully"))

                // Uncomment when API is ready:
                // val result = repository.resendOtp(ResendOtpRequest(
                //     identifier = phoneNumber,
                //     purpose = purpose
                // ))
                // _resendResult.postValue(result)

            } catch (e: Exception) {
                Log.e("OTPViewModel", "Exception during OTP resend", e)
                _resendResult.postValue(Resource.Error(e.message ?: "Failed to resend OTP"))
            }
        }
    }

    fun clearOtpResult() {
        _otpResult.value = null
    }
}