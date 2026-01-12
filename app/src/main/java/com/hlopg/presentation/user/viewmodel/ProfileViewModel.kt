package com.hlopg.presentation.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hlopg.presentation.screen.ProfileUiState
import com.hlopg.presentation.screen.UserProfile
import com.hlopg.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            try {
                _uiState.value = ProfileUiState.Loading

                // Validate session exists
                val userId = sessionManager.getUserId()
                if (userId.isEmpty()) {
                    _uiState.value = ProfileUiState.Error("No active session found")
                    return@launch
                }

                // Load user profile
                val userName = sessionManager.getUserName()
                if (userName.isEmpty()) {
                    _uiState.value = ProfileUiState.Error("User name not found")
                    return@launch
                }

                _uiState.value = ProfileUiState.Success(
                    UserProfile(
                        id = userId,
                        name = userName,
                        email = sessionManager.getUserEmail(),
                        avatarUrl = null,
                        phone = null
                    )
                )
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(
                    e.message ?: "Failed to load profile"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                sessionManager.logout()
            } catch (e: Exception) {
                // Log error but still allow logout to proceed
                // In production, you might want to show a toast or log to analytics
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Clean up any resources if needed
    }
}