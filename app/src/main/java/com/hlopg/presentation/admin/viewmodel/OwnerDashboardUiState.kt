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

// ==================== UI STATE ====================

data class OwnerDashboardUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val location: String = "Hyderabad",
    val myPGs: List<OwnerPGDetails> = emptyList(),
    val updateText: String = "",
    val selectedAnalyticsDate: String = "Today",
    val bookingCount: Int = 0,
    val amountReceived: Int = 0,
    val chartData: List<ChartDataPoint> = emptyList(),
    val membersIn: List<MemberDetails> = emptyList(),
    val membersOut: List<MemberDetails> = emptyList(),
    val complaints: List<ComplaintDetails> = emptyList(),
    val reviews: List<ReviewDetails> = emptyList()
)

// ==================== DATA MODELS ====================

data class OwnerPGDetails(
    val id: String,
    val name: String,
    val location: String,
    val rating: Double,
    val badge: String,
    val price: Int,
    val amenitiesCount: Int,
    val imageUrl: String? = null,
    val imageRes: Int? = null,
    val isFavorite: Boolean = false,
    val badgeColor: Long = 0xFF7556FF
)

data class ChartDataPoint(
    val date: String,
    val value: Float
)

data class MemberDetails(
    val id: String,
    val name: String,
    val age: Int,
    val shareType: String
)

data class ComplaintDetails(
    val id: String,
    val userName: String,
    val location: String,
    val complaint: String,
    val userImageUrl: String? = null,
    val timestamp: String? = null
)

data class ReviewDetails(
    val id: String,
    val userName: String,
    val location: String,
    val review: String,
    val rating: Float,
    val userImageUrl: String? = null,
    val timestamp: String? = null
)

// ==================== NAVIGATION EVENTS ====================

sealed class OwnerNavEvent {
    data class OpenPGDetails(val pgId: String) : OwnerNavEvent()
    data class OpenMemberDetails(val memberId: String) : OwnerNavEvent()
}

// ==================== VIEWMODEL ====================

@HiltViewModel
class OwnerDashboardViewModel @Inject constructor(
    // TODO: Inject your use cases here
    // private val getOwnerPGsUseCase: GetOwnerPGsUseCase,
    // private val getBookingAnalyticsUseCase: GetBookingAnalyticsUseCase,
    // private val getMembersUseCase: GetMembersUseCase,
    // private val getComplaintsUseCase: GetComplaintsUseCase,
    // private val getReviewsUseCase: GetReviewsUseCase,
    // private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(OwnerDashboardUiState())
    val uiState: StateFlow<OwnerDashboardUiState> = _uiState.asStateFlow()

    private val _navEvents = MutableSharedFlow<OwnerNavEvent>()
    val navEvents: SharedFlow<OwnerNavEvent> = _navEvents.asSharedFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // Simulate API calls - Replace with actual use cases
                loadMyPGs()
                loadBookingAnalytics()
                loadMembers()
                loadComplaints()
                loadReviews()

                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load dashboard data"
                    )
                }
            }
        }
    }

    private suspend fun loadMyPGs() {
        // TODO: Replace with actual API call
        // val ownerId = sessionManager.getOwnerId()
        // val pgs = getOwnerPGsUseCase.execute(ownerId)

        val mockPGs = listOf(
            OwnerPGDetails(
                id = "1",
                name = "Siva Kumar PG (Madhapur)",
                location = "Madhapur",
                rating = 4.5,
                badge = "Boys",
                price = 7000,
                amenitiesCount = 8,
                badgeColor = 0xFF7556FF
            ),
            OwnerPGDetails(
                id = "2",
                name = "Sai Ram PG (Madhapur)",
                location = "Madhapur",
                rating = 4.5,
                badge = "Boys",
                price = 7000,
                amenitiesCount = 8,
                badgeColor = 0xFF7556FF
            )
        )
        _uiState.update { it.copy(myPGs = mockPGs) }
    }

    private suspend fun loadBookingAnalytics() {
        // TODO: Replace with actual API call
        // val analytics = getBookingAnalyticsUseCase.execute(date = uiState.value.selectedAnalyticsDate)

        val mockChartData = listOf(
            ChartDataPoint("27\nSEP", 40f),
            ChartDataPoint("28\nSEP", 60f),
            ChartDataPoint("29\nSEP", 45f),
            ChartDataPoint("30\nSEP", 75f),
            ChartDataPoint("1\nOCT", 55f),
            ChartDataPoint("2\nOCT", 80f),
            ChartDataPoint("3\nOCT", 65f)
        )

        _uiState.update {
            it.copy(
                bookingCount = 10,
                amountReceived = 24000,
                chartData = mockChartData
            )
        }
    }

    private suspend fun loadMembers() {
        // TODO: Replace with actual API call
        // val members = getMembersUseCase.execute()

        val mockMembersIn = listOf(
            MemberDetails("1", "Chaitanya", 22, "3"),
            MemberDetails("2", "Rajesh", 24, "2"),
            MemberDetails("3", "Suresh", 23, "3")
        )

        val mockMembersOut = listOf(
            MemberDetails("4", "Ramesh", 25, "3"),
            MemberDetails("5", "Mahesh", 22, "2"),
            MemberDetails("6", "Dinesh", 24, "3")
        )

        _uiState.update {
            it.copy(
                membersIn = mockMembersIn,
                membersOut = mockMembersOut
            )
        }
    }

    private suspend fun loadComplaints() {
        // TODO: Replace with actual API call
        // val complaints = getComplaintsUseCase.execute()

        val mockComplaints = listOf(
            ComplaintDetails(
                id = "1",
                userName = "Chaitanya Thota",
                location = "Hyderabad",
                complaint = "The WiFi connection is very slow in the evening hours. Please look into this issue.",
                timestamp = "2 hours ago"
            ),
            ComplaintDetails(
                id = "2",
                userName = "Rajesh Kumar",
                location = "Hyderabad",
                complaint = "The water supply is inconsistent. Sometimes there's no water in the morning.",
                timestamp = "5 hours ago"
            )
        )

        _uiState.update { it.copy(complaints = mockComplaints) }
    }

    private suspend fun loadReviews() {
        // TODO: Replace with actual API call
        // val reviews = getReviewsUseCase.execute()

        val mockReviews = listOf(
            ReviewDetails(
                id = "1",
                userName = "Chaitanya Thota",
                location = "Hyderabad",
                review = "Hlo PG made finding my perfect PG Hostel so easy! The verified listings gave me peace of mind, and the whole process was smooth from start to finish.",
                rating = 4.5f,
                timestamp = "1 day ago"
            ),
            ReviewDetails(
                id = "2",
                userName = "Suresh Reddy",
                location = "Hyderabad",
                review = "Great PG with excellent facilities and cooperative staff. Highly recommended!",
                rating = 4.0f,
                timestamp = "2 days ago"
            )
        )

        _uiState.update { it.copy(reviews = mockReviews) }
    }

    // ==================== USER ACTIONS ====================

    fun onPGCardClick(pgId: String) {
        viewModelScope.launch {
            _navEvents.emit(OwnerNavEvent.OpenPGDetails(pgId))
        }
    }

    fun onMemberClick(memberId: String) {
        viewModelScope.launch {
            _navEvents.emit(OwnerNavEvent.OpenMemberDetails(memberId))
        }
    }

    fun updatePGUpdateText(text: String) {
        _uiState.update { it.copy(updateText = text) }
    }

    fun setAnalyticsDate(date: String) {
        _uiState.update { it.copy(selectedAnalyticsDate = date) }
        // Reload analytics for the new date
        viewModelScope.launch {
            loadBookingAnalytics()
        }
    }

    fun retry() {
        loadDashboardData()
    }

    fun refresh() {
        loadDashboardData()
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}