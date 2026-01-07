package com.hlopg.presentation.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
    val email: String? = null,
    val roomNumber: String? = null,
    val monthlyRent: Double? = null
)

// ==================== UI STATE ====================

data class PGMembersUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val members: List<PGMemberDetails> = emptyList(),
    val selectedMonth: String = "This Month",
    val selectedDateRange: String = "This Month",
    val showMonthPicker: Boolean = false,
    val showDateRangePicker: Boolean = false,
    val startDateMillis: Long? = null,
    val endDateMillis: Long? = null,
    val currentCount: Int = 0,
    val totalCapacity: Int = 15,
    val stayingCount: Int = 0,
    val leavingCount: Int = 0,
    val leftCount: Int = 0
)

// ==================== VIEWMODEL ====================

@HiltViewModel
class PGMembersViewModel @Inject constructor(
    // TODO: Inject your use cases here when implementing API
    // private val getMembersUseCase: GetMembersUseCase,
    // private val getPGCapacityUseCase: GetPGCapacityUseCase,
    // private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PGMembersUiState())
    val uiState: StateFlow<PGMembersUiState> = _uiState.asStateFlow()

    private val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    init {
        loadMembers()
    }

    // ==================== PUBLIC FUNCTIONS ====================

    /**
     * Loads members data - Currently using mock data
     * TODO: Replace with actual API call when backend is ready
     */
    fun loadMembers(startDate: Long? = null, endDate: Long? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // TODO: Replace with actual API call
                // Example API implementation:
                // val pgId = sessionManager.getCurrentPGId()
                // val response = getMembersUseCase.execute(
                //     pgId = pgId,
                //     startDate = startDate,
                //     endDate = endDate
                // )
                // val members = response.data
                // val capacity = getPGCapacityUseCase.execute(pgId)

                // Using mock data for now
                val mockMembers = generateMockMembers()

                // Filter by date range if provided
                val filteredMembers = if (startDate != null && endDate != null) {
                    filterMembersByDateRange(mockMembers, startDate, endDate)
                } else {
                    mockMembers
                }

                // Calculate counts
                val stayingCount = filteredMembers.count { it.status == MemberStatus.STAYING }
                val leavingCount = filteredMembers.count { it.status == MemberStatus.LEAVING }
                val leftCount = filteredMembers.count { it.status == MemberStatus.LEFT }
                val currentCount = stayingCount + leavingCount

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        members = filteredMembers,
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

    /**
     * Filters members based on date range
     */
    private fun filterMembersByDateRange(
        members: List<PGMemberDetails>,
        startDate: Long,
        endDate: Long
    ): List<PGMemberDetails> {
        // Convert date strings to millis for comparison
        return members.filter { member ->
            try {
                val joiningDateMillis = parseDateString(member.joiningDate)
                joiningDateMillis in startDate..endDate
            } catch (e: Exception) {
                true // Include if date parsing fails
            }
        }
    }

    /**
     * Parses date string to milliseconds
     */
    private fun parseDateString(dateString: String): Long {
        return try {
            val sdf = SimpleDateFormat("d-M-yyyy", Locale.getDefault())
            sdf.parse(dateString)?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * Sets the date range and reloads members
     * @param startDate Start date in milliseconds
     * @param endDate End date in milliseconds
     */
    fun setDateRange(startDate: Long, endDate: Long) {
        val startDateStr = dateFormatter.format(Date(startDate))
        val endDateStr = dateFormatter.format(Date(endDate))
        val dateRangeText = "$startDateStr - $endDateStr"

        _uiState.update {
            it.copy(
                selectedDateRange = dateRangeText,
                startDateMillis = startDate,
                endDateMillis = endDate,
                showDateRangePicker = false
            )
        }

        loadMembers(startDate, endDate)
    }

    /**
     * Toggles the date range picker visibility
     */
    fun toggleDateRangePicker() {
        _uiState.update { it.copy(showDateRangePicker = !it.showDateRangePicker) }
    }

    /**
     * Dismisses the date range picker dialog
     */
    fun dismissDateRangePicker() {
        _uiState.update { it.copy(showDateRangePicker = false) }
    }

    /**
     * Resets date filter to show all members
     */
    fun resetDateFilter() {
        _uiState.update {
            it.copy(
                selectedDateRange = "This Month",
                selectedMonth = "This Month",
                startDateMillis = null,
                endDateMillis = null
            )
        }
        loadMembers()
    }

    /**
     * Toggles the month picker visibility
     */
    fun toggleMonthPicker() {
        _uiState.update { it.copy(showMonthPicker = !it.showMonthPicker) }
    }

    /**
     * Dismisses the month picker dialog
     */
    fun dismissMonthPicker() {
        _uiState.update { it.copy(showMonthPicker = false) }
    }

    /**
     * Updates the selected month and reloads members
     * @param month The month string to filter by (e.g. "January 2026")
     */
    fun setSelectedMonth(month: String) {
        _uiState.update {
            it.copy(
                selectedMonth = month,
                selectedDateRange = month,
                showMonthPicker = false
            )
        }
        // TODO: When API is ready, pass month filter to loadMembers
        loadMembers()
    }

    /**
     * Retries loading members after an error
     */
    fun retry() {
        loadMembers(
            startDate = uiState.value.startDateMillis,
            endDate = uiState.value.endDateMillis
        )
    }

    /**
     * Clears the current error state
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    /**
     * Filters members by status
     * TODO: Implement when API supports filtering
     */
    fun filterByStatus(status: MemberStatus) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // TODO: Implement API call with status filter
                // val pgId = sessionManager.getCurrentPGId()
                // val filteredMembers = getMembersUseCase.execute(
                //     pgId = pgId,
                //     status = status,
                //     startDate = uiState.value.startDateMillis,
                //     endDate = uiState.value.endDateMillis
                // )

                // For now, filter from existing data
                val currentMembers = uiState.value.members
                val filtered = currentMembers.filter { it.status == status }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        members = filtered
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to filter members"
                    )
                }
            }
        }
    }

    /**
     * Searches members by name
     * TODO: Implement when API supports search
     */
    fun searchMembers(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                loadMembers(
                    startDate = uiState.value.startDateMillis,
                    endDate = uiState.value.endDateMillis
                )
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }

            try {
                // TODO: Implement API search
                // val pgId = sessionManager.getCurrentPGId()
                // val searchResults = getMembersUseCase.search(
                //     pgId = pgId,
                //     query = query,
                //     startDate = uiState.value.startDateMillis,
                //     endDate = uiState.value.endDateMillis
                // )

                // For now, filter from existing data
                val currentMembers = uiState.value.members
                val filtered = currentMembers.filter {
                    it.name.contains(query, ignoreCase = true) ||
                            it.phoneNumber?.contains(query) == true
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        members = filtered
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Search failed"
                    )
                }
            }
        }
    }

    /**
     * Exports members data
     * TODO: Implement when export functionality is needed
     */
    fun exportMembers() {
        viewModelScope.launch {
            try {
                // TODO: Implement export functionality
                // val pgId = sessionManager.getCurrentPGId()
                // exportUseCase.exportToPDF(
                //     pgId = pgId,
                //     members = uiState.value.members,
                //     dateRange = uiState.value.selectedDateRange
                // )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Export failed: ${e.message}")
                }
            }
        }
    }

    // ==================== MOCK DATA GENERATOR ====================
    // This section will be removed once API integration is complete

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
                email = "chaitanya@example.com",
                roomNumber = "101",
                monthlyRent = 8000.0
            ),
            PGMemberDetails(
                id = "2",
                name = "Rajesh Kumar Verma",
                sharing = 3,
                joiningDate = "3-10-2025",
                leavingDate = "14-11-2025",
                status = MemberStatus.LEAVING,
                phoneNumber = "+91 9876543211",
                email = "rajesh@example.com",
                roomNumber = "102",
                monthlyRent = 7500.0
            ),
            PGMemberDetails(
                id = "3",
                name = "Suresh Reddy",
                sharing = 3,
                joiningDate = "3-10-2025",
                leavingDate = "15-11-2025",
                status = MemberStatus.LEAVING,
                phoneNumber = "+91 9876543212",
                email = "suresh@example.com",
                roomNumber = "103",
                monthlyRent = 8000.0
            ),
            PGMemberDetails(
                id = "4",
                name = "Ramesh Verma",
                sharing = 3,
                joiningDate = "3-10-2025",
                leavingDate = "3-11-2025",
                status = MemberStatus.LEFT,
                phoneNumber = "+91 9876543213",
                email = "ramesh@example.com",
                roomNumber = "104",
                monthlyRent = 7000.0
            ),
            PGMemberDetails(
                id = "5",
                name = "Mahesh Singh",
                sharing = 3,
                joiningDate = "3-10-2025",
                leavingDate = "3-11-2025",
                status = MemberStatus.LEFT,
                phoneNumber = "+91 9876543214",
                email = "mahesh@example.com",
                roomNumber = "105",
                monthlyRent = 7000.0
            ),
            PGMemberDetails(
                id = "6",
                name = "Dinesh Patel",
                sharing = 3,
                joiningDate = "1-11-2025",
                leavingDate = null,
                status = MemberStatus.STAYING,
                phoneNumber = "+91 9876543215",
                email = "dinesh@example.com",
                roomNumber = "106",
                monthlyRent = 8500.0
            ),
            PGMemberDetails(
                id = "7",
                name = "Arun Kumar Sharma",
                sharing = 2,
                joiningDate = "5-10-2025",
                leavingDate = null,
                status = MemberStatus.STAYING,
                phoneNumber = "+91 9876543216",
                email = "arun@example.com",
                roomNumber = "107",
                monthlyRent = 9000.0
            ),
            PGMemberDetails(
                id = "8",
                name = "Vijay Singh Chauhan",
                sharing = 3,
                joiningDate = "8-10-2025",
                leavingDate = "20-11-2025",
                status = MemberStatus.LEAVING,
                phoneNumber = "+91 9876543217",
                email = "vijay@example.com",
                roomNumber = "108",
                monthlyRent = 7500.0
            ),
            PGMemberDetails(
                id = "9",
                name = "Prakash Reddy",
                sharing = 2,
                joiningDate = "12-10-2025",
                leavingDate = null,
                status = MemberStatus.STAYING,
                phoneNumber = "+91 9876543218",
                email = "prakash@example.com",
                roomNumber = "109",
                monthlyRent = 8800.0
            )
        )
    }

    // ==================== API INTEGRATION TEMPLATE ====================

    /*
    Example API implementation structure with date range:

    private suspend fun fetchMembersFromAPI(
        pgId: String,
        startDate: Long? = null,
        endDate: Long? = null,
        status: MemberStatus? = null
    ): Result<List<PGMemberDetails>> {
        return try {
            val response = getMembersUseCase.execute(
                GetMembersRequest(
                    pgId = pgId,
                    startDate = startDate?.let { convertMillisToDateString(it) },
                    endDate = endDate?.let { convertMillisToDateString(it) },
                    status = status?.name
                )
            )

            when (response) {
                is NetworkResult.Success -> Result.success(response.data)
                is NetworkResult.Error -> Result.failure(
                    Exception(response.message ?: "Unknown error")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun convertMillisToDateString(millis: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date(millis))
    }
    */
}