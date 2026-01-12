package com.hlopg.presentation.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hlopg.utils.SessionManager
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

// ==================== DATA MODELS ====================



// ==================== UI STATE ====================

data class OwnerProfileViewState(
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
    data object NavigateToLogin : ProfileNavEvent()
    data class NavigateTo(val route: String) : ProfileNavEvent()
}

// ==================== VIEWMODEL ====================

@HiltViewModel
class OwnerProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager
    // TODO: Inject your use cases here when ready
    // private val getOwnerProfileUseCase: GetOwnerProfileUseCase,
    // private val getOwnerStatsUseCase: GetOwnerStatsUseCase,
    // private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OwnerProfileViewState())
    val uiState: StateFlow<OwnerProfileViewState> = _uiState.asStateFlow()

    private val _navEvents = MutableSharedFlow<ProfileNavEvent>()
    val navEvents: SharedFlow<ProfileNavEvent> = _navEvents.asSharedFlow()

    init {
        loadProfile()
        loadOwnerStats()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // Validate session
                val userId = sessionManager.getUserId()
                if (userId.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "No active session found"
                        )
                    }
                    return@launch
                }

                val userName = sessionManager.getUserName()
                if (userName.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "User information not found"
                        )
                    }
                    return@launch
                }

                // TODO: Replace with actual API call
                // val user = getOwnerProfileUseCase.execute(userId)

                // For now, use session manager data
                val user = UserProfile(
                    id = userId,
                    name = userName,
                    email = sessionManager.getUserEmail(),
                    avatarUrl = null,
                    phone = sessionManager.getUserPhone()
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = user
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

    private fun loadOwnerStats() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual API call
                // val stats = getOwnerStatsUseCase.execute()

                // Mock data for demonstration
                // In production, this would fetch real data from backend
                _uiState.update {
                    it.copy(
                        pgCount = 3,
                        totalRevenue = 245000,
                        activeMembers = 45
                    )
                }
            } catch (e: Exception) {
                // Handle error silently for stats
                // Don't block profile loading if stats fail
                // Could log to analytics here
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
                // Dismiss dialog and show loading
                _uiState.update {
                    it.copy(
                        showLogoutDialog = false,
                        isLoading = true
                    )
                }

                // TODO: Clear session and logout via use case
                // logoutUseCase.execute()

                // Clear session
                sessionManager.logout()

                // Small delay to ensure state is persisted
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
        loadOwnerStats()
    }

    fun refreshStats() {
        loadOwnerStats()
    }

    override fun onCleared() {
        super.onCleared()
        // Clean up any resources if needed
        // Cancel any pending operations
    }
}