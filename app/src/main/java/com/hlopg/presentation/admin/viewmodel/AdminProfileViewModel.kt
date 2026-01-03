package com.hlopg.presentation.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Import the existing UserProfile from your project
// Add this import based on where UserProfile is defined in your project
// For example: import com.hlopg.domain.model.UserProfile
// or: import com.hlopg.data.model.UserProfile

// ==================== UI STATE ====================

data class AdminProfileViewState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: UserProfile? = null,
    val pgCount: Int = 0,
    val totalRevenue: Int = 0,
    val activeMembers: Int = 0,
    val showLogoutDialog: Boolean = false
)

// ==================== NAVIGATION EVENTS ====================

sealed class ProfileNavEvent {
    object NavigateToLogin : ProfileNavEvent()
    data class NavigateTo(val route: String) : ProfileNavEvent()
}

// ==================== VIEWMODEL ====================

@HiltViewModel
class AdminProfileViewModel @Inject constructor(
    // TODO: Inject your use cases here
    // private val getAdminProfileUseCase: GetAdminProfileUseCase,
    // private val getAdminStatsUseCase: GetAdminStatsUseCase,
    // private val logoutUseCase: LogoutUseCase,
    // private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminProfileViewState())
    val uiState: StateFlow<AdminProfileViewState> = _uiState.asStateFlow()

    private val _navEvents = MutableSharedFlow<ProfileNavEvent>()
    val navEvents: SharedFlow<ProfileNavEvent> = _navEvents.asSharedFlow()

    init {
        loadProfile()
        loadAdminStats()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // TODO: Replace with actual API call
                // val userId = sessionManager.getUserId()
                // val user = getAdminProfileUseCase.execute(userId)

                // Mock data for demonstration
                // Adjust the UserProfile constructor parameters based on your existing UserProfile class
                val mockUser = UserProfile(
                    id = "admin_1",
                    name = "John Doe",
                    email = "admin@hlopg.com",
                    avatarUrl = null,
                    phone = "+91 9876543210"
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = mockUser
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load profile"
                    )
                }
            }
        }
    }

    private fun loadAdminStats() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual API call
                // val stats = getAdminStatsUseCase.execute()

                _uiState.update {
                    it.copy(
                        pgCount = 3,
                        totalRevenue = 245000,
                        activeMembers = 45
                    )
                }
            } catch (e: Exception) {
                // Handle error silently or update state
                // Don't block profile loading if stats fail
            }
        }
    }

    fun showLogoutDialog() {
        _uiState.update { it.copy(showLogoutDialog = true) }
    }

    fun dismissLogoutDialog() {
        _uiState.update { it.copy(showLogoutDialog = false) }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                // Dismiss the dialog first
                _uiState.update { it.copy(showLogoutDialog = false, isLoading = true) }

                // TODO: Clear session and logout
                // logoutUseCase.execute()
                // sessionManager.clearSession()

                // Small delay to ensure state is cleared
                kotlinx.coroutines.delay(300)

                // Navigate to login
                _navEvents.emit(ProfileNavEvent.NavigateToLogin)

                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        showLogoutDialog = false,
                        error = "Failed to logout. Please try again."
                    )
                }
            }
        }
    }

    fun navigateTo(route: String) {
        viewModelScope.launch {
            _navEvents.emit(ProfileNavEvent.NavigateTo(route))
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun retry() {
        loadProfile()
        loadAdminStats()
    }

    fun refreshStats() {
        loadAdminStats()
    }
}