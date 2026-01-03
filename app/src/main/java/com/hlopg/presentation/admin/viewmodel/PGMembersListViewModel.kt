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

// ==================== DATA MODELS ====================

enum class MemberStatus {
    STAYING,
    LEAVING,
    LEFT
}

data class PGMemberDetails(
    val id: String,
    val name: String,
    val sharing: Int,
    val joiningDate: String,
    val leavingDate: String? = null,
    val status: MemberStatus,
    val phoneNumber: String? = null,
    val email: String? = null
)

// ==================== UI STATE ====================

data class PGMembersUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val members: List<PGMemberDetails> = emptyList(),
    val selectedMonth: String = "This Month",
    val currentCount: Int = 0,
    val totalCapacity: Int = 15,
    val stayingCount: Int = 0,
    val leavingCount: Int = 0,
    val leftCount: Int = 0
)

// ==================== VIEWMODEL ====================

@HiltViewModel
class PGMembersViewModel @Inject constructor(
    // TODO: Inject your use cases here
    // private val getMembersUseCase: GetMembersUseCase,
    // private val getPGCapacityUseCase: GetPGCapacityUseCase,
    // private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PGMembersUiState())
    val uiState: StateFlow<PGMembersUiState> = _uiState.asStateFlow()

    init {
        loadMembers()
    }

    fun loadMembers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // TODO: Replace with actual API call
                // val pgId = sessionManager.getCurrentPGId()
                // val members = getMembersUseCase.execute(pgId)

                val mockMembers = generateMockMembers()

                val stayingCount = mockMembers.count { it.status == MemberStatus.STAYING }
                val leavingCount = mockMembers.count { it.status == MemberStatus.LEAVING }
                val leftCount = mockMembers.count { it.status == MemberStatus.LEFT }
                val currentCount = stayingCount + leavingCount

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        members = mockMembers,
                        currentCount = currentCount,
                        stayingCount = stayingCount,
                        leavingCount = leavingCount,
                        leftCount = leftCount
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load members"
                    )
                }
            }
        }
    }

    fun setSelectedMonth(month: String) {
        _uiState.update { it.copy(selectedMonth = month) }
        // Reload members for the selected month
        loadMembers()
    }

    fun retry() {
        loadMembers()
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun filterByStatus(status: MemberStatus) {
        viewModelScope.launch {
            // TODO: Implement filtering logic
            // val filteredMembers = getMembersUseCase.execute(status = status)
        }
    }

    // ==================== MOCK DATA GENERATOR ====================

    private fun generateMockMembers(): List<PGMemberDetails> {
        return listOf(
            PGMemberDetails(
                id = "1",
                name = "Chaitanya Thota",
                sharing = 3,
                joiningDate = "3-10-2025",
                leavingDate = null,
                status = MemberStatus.STAYING,
                phoneNumber = "+91 9876543210",
                email = "chaitanya@example.com"
            ),
            PGMemberDetails(
                id = "2",
                name = "Rajesh Kumar",
                sharing = 2,
                joiningDate = "5-10-2025",
                leavingDate = "14-12-2025",
                status = MemberStatus.LEAVING,
                phoneNumber = "+91 9876543211",
                email = "rajesh@example.com"
            ),
            PGMemberDetails(
                id = "3",
                name = "Suresh Reddy",
                sharing = 3,
                joiningDate = "8-10-2025",
                leavingDate = "15-12-2025",
                status = MemberStatus.LEAVING,
                phoneNumber = "+91 9876543212",
                email = "suresh@example.com"
            ),
            PGMemberDetails(
                id = "4",
                name = "Ramesh Verma",
                sharing = 2,
                joiningDate = "10-09-2025",
                leavingDate = "10-11-2025",
                status = MemberStatus.LEFT,
                phoneNumber = "+91 9876543213",
                email = "ramesh@example.com"
            ),
            PGMemberDetails(
                id = "5",
                name = "Mahesh Singh",
                sharing = 3,
                joiningDate = "15-09-2025",
                leavingDate = "15-11-2025",
                status = MemberStatus.LEFT,
                phoneNumber = "+91 9876543214",
                email = "mahesh@example.com"
            ),
            PGMemberDetails(
                id = "6",
                name = "Dinesh Patel",
                sharing = 3,
                joiningDate = "1-11-2025",
                leavingDate = null,
                status = MemberStatus.STAYING,
                phoneNumber = "+91 9876543215",
                email = "dinesh@example.com"
            )
        )
    }
}