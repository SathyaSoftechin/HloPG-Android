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
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

// ==================== DATA MODELS ====================

data class UserProfile(
    val id: String,
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val avatarUrl: String? = null,
    val userType: String = "user", // "user" or "admin"
    val pgCount: Int? = null, // For admin
    val memberSince: String? = null
)

data class PaymentDetails(
    val id: String,
    val memberName: String,
    val memberId: String,
    val sharing: Int,
    val joiningDate: String,
    val amount: Int,
    val paymentDate: String,
    val paymentMethod: String? = null,
    val transactionId: String? = null,
    val status: PaymentStatus = PaymentStatus.SUCCESS
)

enum class PaymentStatus {
    SUCCESS,
    PENDING,
    FAILED
}

data class MonthOption(
    val displayName: String,
    val value: String // Format: "2025-01"
)

// ==================== UI STATE ====================

data class PaymentsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val payments: List<PaymentDetails> = emptyList(),
    val selectedMonth: MonthOption = MonthOption("This Month", getCurrentMonth()),
    val availableMonths: List<MonthOption> = emptyList(),
    val totalAmount: Int = 0,
    val successCount: Int = 0,
    val pendingCount: Int = 0,
    val showMonthPicker: Boolean = false
)

// ==================== VIEWMODEL ====================

@HiltViewModel
class PaymentsViewModel @Inject constructor(
    // TODO: Inject your use cases here
    // private val getPaymentsUseCase: GetPaymentsUseCase,
    // private val getAvailableMonthsUseCase: GetAvailableMonthsUseCase,
    // private val exportPaymentsUseCase: ExportPaymentsUseCase,
    // private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentsUiState())
    val uiState: StateFlow<PaymentsUiState> = _uiState.asStateFlow()

    init {
        loadAvailableMonths()
        loadPayments()
    }

    private fun loadAvailableMonths() {
        viewModelScope.launch {
            try {
                // TODO: Replace with actual API call
                // val months = getAvailableMonthsUseCase.execute()

                val months = generateMonthOptions()
                _uiState.update { it.copy(availableMonths = months) }
            } catch (e: Exception) {
                // Handle error silently or log
            }
        }
    }

    fun loadPayments(month: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // TODO: Replace with actual API call
                // val payments = getPaymentsUseCase.execute(
                //     month = month ?: uiState.value.selectedMonth.value
                // )

                // Mock data for demonstration
                val mockPayments = generateMockPayments()

                val totalAmount = mockPayments
                    .filter { it.status == PaymentStatus.SUCCESS }
                    .sumOf { it.amount }

                val successCount = mockPayments.count { it.status == PaymentStatus.SUCCESS }
                val pendingCount = mockPayments.count { it.status == PaymentStatus.PENDING }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        payments = mockPayments,
                        totalAmount = totalAmount,
                        successCount = successCount,
                        pendingCount = pendingCount
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load payments"
                    )
                }
            }
        }
    }

    fun setSelectedMonth(month: MonthOption) {
        _uiState.update {
            it.copy(
                selectedMonth = month,
                showMonthPicker = false
            )
        }
        loadPayments(month.value)
    }

    fun toggleMonthPicker() {
        _uiState.update { it.copy(showMonthPicker = !it.showMonthPicker) }
    }

    fun dismissMonthPicker() {
        _uiState.update { it.copy(showMonthPicker = false) }
    }

    fun retry() {
        loadPayments()
    }

    fun exportPayments() {
        viewModelScope.launch {
            try {
                // TODO: Implement export functionality
                // exportPaymentsUseCase.execute(uiState.value.payments)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Failed to export payments")
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    // ==================== MOCK DATA GENERATORS ====================

    private fun generateMockPayments(): List<PaymentDetails> {
        return listOf(
            PaymentDetails(
                id = "1",
                memberName = "Chaitanya Thota",
                memberId = "M001",
                sharing = 3,
                joiningDate = "3-10-2025",
                amount = 9000,
                paymentDate = "3-10-2025",
                paymentMethod = "UPI",
                transactionId = "TXN001",
                status = PaymentStatus.SUCCESS
            ),
            PaymentDetails(
                id = "2",
                memberName = "Rajesh Kumar",
                memberId = "M002",
                sharing = 2,
                joiningDate = "5-10-2025",
                amount = 8000,
                paymentDate = "5-10-2025",
                paymentMethod = "Card",
                transactionId = "TXN002",
                status = PaymentStatus.SUCCESS
            ),
            PaymentDetails(
                id = "3",
                memberName = "Suresh Reddy",
                memberId = "M003",
                sharing = 3,
                joiningDate = "8-10-2025",
                amount = 9000,
                paymentDate = "8-10-2025",
                paymentMethod = "Cash",
                status = PaymentStatus.SUCCESS
            ),
            PaymentDetails(
                id = "4",
                memberName = "Ramesh Verma",
                memberId = "M004",
                sharing = 2,
                joiningDate = "10-10-2025",
                amount = 8000,
                paymentDate = "10-10-2025",
                paymentMethod = "UPI",
                transactionId = "TXN004",
                status = PaymentStatus.SUCCESS
            ),
            PaymentDetails(
                id = "5",
                memberName = "Mahesh Singh",
                memberId = "M005",
                sharing = 3,
                joiningDate = "12-10-2025",
                amount = 9000,
                paymentDate = "12-10-2025",
                paymentMethod = "UPI",
                transactionId = "TXN005",
                status = PaymentStatus.SUCCESS
            )
        )
    }

    private fun generateMonthOptions(): List<MonthOption> {
        return listOf(
            MonthOption("This Month", getCurrentMonth()),
            MonthOption("Last Month", getPreviousMonth(1)),
            MonthOption("2 Months Ago", getPreviousMonth(2)),
            MonthOption("3 Months Ago", getPreviousMonth(3))
        )
    }
}

// ==================== HELPER FUNCTIONS ====================

fun getCurrentMonth(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    return dateFormat.format(calendar.time)
}

fun getPreviousMonth(monthsAgo: Int): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MONTH, -monthsAgo)
    val dateFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    return dateFormat.format(calendar.time)
}