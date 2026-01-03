package com.hlopg.presentation.user.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hlopg.data.model.RegisterRequest
import com.hlopg.data.model.User
import com.hlopg.data.repository.AuthRepository
import com.hlopg.domain.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _signupResult = MutableLiveData<Resource<User>>()
    val signupResult: LiveData<Resource<User>> = _signupResult

    private val _validationError = MutableLiveData<String?>()
    val validationError: LiveData<String?> = _validationError

    fun signup(
        fullName: String,
        mobileNumber: String,
        emailAddress: String,
        password: String,
        confirmPassword: String,
        gender: String,
        agreeTerms: Boolean,
        userType: String = "user"
    ) {
        viewModelScope.launch {
            try {
                val validationResult = validateSignupInput(
                    fullName,
                    mobileNumber,
                    emailAddress,
                    password,
                    confirmPassword,
                    gender,
                    agreeTerms
                )

                if (validationResult != null) {
                    _validationError.postValue(validationResult)
                    return@launch
                }

                _validationError.postValue(null)
                _signupResult.postValue(Resource.Loading())

                val registerRequest = RegisterRequest(
                    name = fullName.trim(),
                    email = emailAddress.trim(),
                    phone = mobileNumber.trim(),
                    password = password,
                    gender = gender,
                    user_type = userType
                )

                when (val result = repository.registerUser(registerRequest)) {
                    is Resource.Success -> {
                        Log.d("SignupViewModel", "Signup success")
                        _signupResult.postValue(result)
                    }

                    is Resource.Error -> {
                        Log.e("SignupViewModel", "Signup failed: ${result.message}")
                        _signupResult.postValue(result)
                    }

                    is Resource.Loading -> {
                        // No-op
                    }
                }

            } catch (e: Exception) {
                Log.e("SignupViewModel", "Exception during signup", e)
                _signupResult.postValue(
                    Resource.Error(e.message ?: "Signup failed")
                )
            }
        }
    }

    private fun validateSignupInput(
        fullName: String,
        mobileNumber: String,
        emailAddress: String,
        password: String,
        confirmPassword: String,
        gender: String,
        agreeTerms: Boolean
    ): String? {
        return when {
            fullName.isBlank() -> "Full name is required"
            fullName.length < 2 -> "Full name must be at least 2 characters"

            mobileNumber.isBlank() -> "Mobile number is required"
            !isValidPhone(mobileNumber) -> "Please enter a valid 10-digit mobile number"

            emailAddress.isBlank() -> "Email address is required"
            !isValidEmail(emailAddress) -> "Please enter a valid email address"

            gender.isBlank() -> "Please select your gender"

            password.isBlank() -> "Password is required"
            password.length < 6 -> "Password must be at least 6 characters"

            confirmPassword.isBlank() -> "Please confirm your password"
            password != confirmPassword -> "Passwords do not match"

            !agreeTerms -> "Please agree to terms and conditions"

            else -> null
        }
    }

    private fun isValidEmail(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPhone(phone: String): Boolean {
        val cleanedPhone = phone.replace(Regex("[\\s\\-()]"), "")
        return cleanedPhone.matches(Regex("^[6-9]\\d{9}$"))
    }

    fun clearValidationError() {
        _validationError.value = null
    }
}
