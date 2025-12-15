package com.hlopg.presentation.user.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI state for the profile screen
data class ProfileUiState(
    val avatarUrl: String? = null,
    val username: String = "",
    val email: String = "",
    val gender: String? = "Male",
    val password: String? = null,
    val isSaving: Boolean = false
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    // inject your repo if you want to persist changes
) : ViewModel() {

    // backing state
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        // Load initial profile — replace with real repo call if available
        loadLocalProfile()
    }

    private fun loadLocalProfile() {
        // Replace with repository/data store load
        viewModelScope.launch {
            _uiState.value = ProfileUiState(
                avatarUrl = null, // or some URL
                username = "User name",
                email = "Mahi@123gmail.com",
                gender = "Male",
                password = "password123"
            )
        }
    }

    // UI event handlers
    fun onAvatarClick() {
        // TODO: implement image picker and update avatarUrl
        // For now just toggles placeholder / no-op
    }

    fun onUsernameChange(new: String) {
        _uiState.update { it.copy(username = new) }
    }

    fun onEmailChange(new: String) {
        _uiState.update { it.copy(email = new) }
    }

    fun onGenderChange(new: String) {
        _uiState.update { it.copy(gender = new) }
    }

    fun onPasswordChange(new: String) {
        _uiState.update { it.copy(password = new) }
    }

    fun saveChanges() {
        // Update saving state, persist to repository or DataStore (not implemented here)
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            // TODO: persist to repository / DataStore / network
            // Simulate delay
            kotlinx.coroutines.delay(400)

            // done
            _uiState.update { it.copy(isSaving = false) }
        }
    }
}
