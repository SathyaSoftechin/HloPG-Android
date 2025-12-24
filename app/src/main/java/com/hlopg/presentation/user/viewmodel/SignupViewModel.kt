package com.hlopg.presentation.user.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hlopg.data.model.ApiResponse
import com.hlopg.data.model.RegisterRequest
import com.hlopg.data.model.User
import com.hlopg.data.repository.AuthRepository
import com.hlopg.domain.repository.Resource
import com.hlopg.utils.TokenManager
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
                // Validation
                val validationResult = validateSignupInput(
                    fullName = fullName,
                    mobileNumber = mobileNumber,
                    emailAddress = emailAddress,
                    password = password,
                    confirmPassword = confirmPassword,
                    gender = gender,
                    agreeTerms = agreeTerms
                )

                if (validationResult != null) {
                    _validationError.postValue(validationResult)
                    return@launch
                }

                _validationError.postValue(null)
                Log.d("SignupViewModel", "Starting signup request")
                _signupResult.postValue(Resource.Loading)

                val registerRequest = RegisterRequest(
                    name = fullName.trim(),
                    email = emailAddress.trim(),
                    phone = mobileNumber.trim(),
                    password = password,
                    gender = gender,
                    user_type = userType
                )

                val result = repository.registerUser(registerRequest)

                when (result) {
                    is Resource.Success -> {
                        Log.d("SignupViewModel", "Signup success")
                        _signupResult.postValue(result)
                    }
                    is Resource.Error -> {
                        Log.e("SignupViewModel", "Signup failed: ${result.message}")
                        _signupResult.postValue(result)
                    }
                    is Resource.Loading -> {
                        // Already handled above
                    }
                }

            } catch (e: Exception) {
                Log.e("SignupViewModel", "Exception during signup", e)
                _signupResult.postValue(Resource.Error(e.message ?: "Signup failed"))
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

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhone(phone: String): Boolean {
        // Remove any spaces, dashes, or parentheses
        val cleanedPhone = phone.replace(Regex("[\\s\\-()]"), "")
        // Check if it's a valid Indian phone number (10 digits)
        return cleanedPhone.matches(Regex("^[6-9]\\d{9}$"))
    }

    fun clearValidationError() {
        _validationError.value = null
    }
}

// Alternative constructor for backward compatibility (if not using DI)
class SignupViewModelLegacy : ViewModel() {

    private val _signupResult = MutableLiveData<Result<ApiResponse<User>>>()
    val signupResult: LiveData<Result<ApiResponse<User>>> = _signupResult

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
        userType: String = "user",
        context: Context
    ) {
        viewModelScope.launch {
            try {
                // Validation
                val validationResult = validateSignupInput(
                    fullName = fullName,
                    mobileNumber = mobileNumber,
                    emailAddress = emailAddress,
                    password = password,
                    confirmPassword = confirmPassword,
                    gender = gender,
                    agreeTerms = agreeTerms
                )

                if (validationResult != null) {
                    _validationError.postValue(validationResult)
                    return@launch
                }

                _validationError.postValue(null)
                Log.d("SignupViewModel", "Starting signup request")

                // Create repository instance (not ideal, use DI in production)
                val tokenManager = TokenManager(context)
                val repository = AuthRepository(
                    api = com.hlopg.data.api.RetrofitInstance.authApi,
                    tokenManager = tokenManager
                )

                val registerRequest = RegisterRequest(
                    name = fullName.trim(),
                    email = emailAddress.trim(),
                    phone = mobileNumber.trim(),
                    password = password,
                    gender = gender,
                    user_type = userType
                )

                val result = repository.registerUser(registerRequest)

                when (result) {
                    is Resource.Success -> {
                        Log.d("SignupViewModel", "Signup success")

                        // Create ApiResponse for backward compatibility
                        val apiResponse = ApiResponse(
                            message = "Registration successful",
                            data = result.data,
                            token = null,
                            user = result.data
                        )

                        _signupResult.postValue(Result.success(apiResponse))
                    }
                    is Resource.Error -> {
                        Log.e("SignupViewModel", "Signup failed: ${result.message}")
                        _signupResult.postValue(Result.failure(Exception(result.message)))
                    }
                    is Resource.Loading -> {
                        // Loading state
                    }
                }

            } catch (e: Exception) {
                Log.e("SignupViewModel", "Exception during signup", e)
                _signupResult.postValue(Result.failure(e))
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

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhone(phone: String): Boolean {
        val cleanedPhone = phone.replace(Regex("[\\s\\-()]"), "")
        return cleanedPhone.matches(Regex("^[6-9]\\d{9}$"))
    }

    fun clearValidationError() {
        _validationError.value = null
    }
}