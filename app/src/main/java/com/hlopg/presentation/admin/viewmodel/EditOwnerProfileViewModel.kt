package com.hlopg.presentation.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI state for the edit admin profile screen
data class EditOwnerProfileUiState(
    val avatarUrl: String? = null,
    val username: String = "",
    val email: String = "",
    val gender: String? = "Male",
    val password: String? = null,
    val isSaving: Boolean = false
)

@HiltViewModel
class EditOwnerProfileViewModel @Inject constructor(
    // inject your repo if you want to persist changes
) : ViewModel() {

    // backing state
    private val _uiState = MutableStateFlow(EditOwnerProfileUiState())
    val uiState: StateFlow<EditOwnerProfileUiState> = _uiState.asStateFlow()

    init {
        // Load initial admin profile — replace with real repo call if available
        loadLocalOwnerProfile()
    }

    private fun loadLocalOwnerProfile() {
        // Replace with repository/data store load
        viewModelScope.launch {
            _uiState.value = EditOwnerProfileUiState(
                avatarUrl = null, // or some URL
                username = "Owner",
                email = "admin@hlopg.com",
                gender = "Male",
                password = "admin123"
            )
        }
    }

    // UI event handlers
    fun onOwnerAvatarClick() {
        // TODO: implement image picker and update avatarUrl
        // For now just toggles placeholder / no-op
    }

    fun onOwnerUsernameChange(new: String) {
        _uiState.update { it.copy(username = new) }
    }

    fun onOwnerEmailChange(new: String) {
        _uiState.update { it.copy(email = new) }
    }

    fun onOwnerGenderChange(new: String) {
        _uiState.update { it.copy(gender = new) }
    }

    fun onOwnerPasswordChange(new: String) {
        _uiState.update { it.copy(password = new) }
    }

    fun saveOwnerChanges() {
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