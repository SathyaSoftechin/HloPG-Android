package com.hlopg.presentation.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hlopg.utils.getCurrentMonth
import com.hlopg.utils.getPreviousMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
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

    // Month based
    val selectedMonth: MonthOption = MonthOption("This Month", getCurrentMonth()),
    val availableMonths: List<MonthOption> = emptyList(),
    val showMonthPicker: Boolean = false,

    // Date range based
    val selectedDateRange: String = "This Month",
    val startDateMillis: Long? = null,
    val endDateMillis: Long? = null,
    val showDateRangePicker: Boolean = false,

    val totalAmount: Int = 0,
    val successCount: Int = 0,
    val pendingCount: Int = 0
)


// ==================== VIEWMODEL ====================
@HiltViewModel
class PaymentsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentsUiState())
    val uiState: StateFlow<PaymentsUiState> = _uiState.asStateFlow()

    private val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    init {
        loadAvailableMonths()
        loadPayments()
    }

    // ==================== LOADERS ====================

    private fun loadAvailableMonths() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(availableMonths = generateMonthOptions())
            }
        }
    }

    fun loadPayments(
        startDate: Long? = null,
        endDate: Long? = null,
        month: String? = null
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val allPayments = generateMockPayments()

                val filteredPayments = when {
                    startDate != null && endDate != null -> {
                        allPayments.filter {
                            val paymentMillis = parseDate(it.paymentDate)
                            paymentMillis in startDate..endDate
                        }
                    }

                    month != null -> {
                        allPayments // TODO: filter by month when API ready
                    }

                    else -> allPayments
                }

                val totalAmount = filteredPayments
                    .filter { it.status == PaymentStatus.SUCCESS }
                    .sumOf { it.amount }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        payments = filteredPayments,
                        totalAmount = totalAmount,
                        successCount = filteredPayments.count { p -> p.status == PaymentStatus.SUCCESS },
                        pendingCount = filteredPayments.count { p -> p.status == PaymentStatus.PENDING }
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

    // ==================== DATE RANGE ====================

    fun toggleDateRangePicker() {
        _uiState.update { it.copy(showDateRangePicker = !it.showDateRangePicker) }
    }

    fun dismissDateRangePicker() {
        _uiState.update { it.copy(showDateRangePicker = false) }
    }

    fun setDateRange(startDate: Long, endDate: Long) {
        val startText = dateFormatter.format(startDate)
        val endText = dateFormatter.format(endDate)

        _uiState.update {
            it.copy(
                selectedDateRange = "$startText - $endText",
                startDateMillis = startDate,
                endDateMillis = endDate,
                showDateRangePicker = false
            )
        }

        loadPayments(startDate = startDate, endDate = endDate)
    }

    fun resetDateFilter() {
        _uiState.update {
            it.copy(
                selectedDateRange = "This Month",
                startDateMillis = null,
                endDateMillis = null
            )
        }
        loadPayments()
    }

    // ==================== MONTH PICKER ====================

    fun setSelectedMonth(month: MonthOption) {
        _uiState.update {
            it.copy(
                selectedMonth = month,
                selectedDateRange = month.displayName,
                showMonthPicker = false,
                startDateMillis = null,
                endDateMillis = null
            )
        }
        loadPayments(month = month.value)
    }

    fun toggleMonthPicker() {
        _uiState.update { it.copy(showMonthPicker = !it.showMonthPicker) }
    }

    fun dismissMonthPicker() {
        _uiState.update { it.copy(showMonthPicker = false) }
    }

    // ==================== HELPERS ====================

    private fun parseDate(date: String): Long {
        return try {
            SimpleDateFormat("d-M-yyyy", Locale.getDefault())
                .parse(date)?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    fun retry() {
        loadPayments(
            startDate = uiState.value.startDateMillis,
            endDate = uiState.value.endDateMillis
        )
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    // ==================== MOCK DATA ====================

    private fun generateMockPayments(): List<PaymentDetails> {
        return listOf(
            PaymentDetails("1", "Chaitanya Thota", "M001", 3, "3-10-2025", 9000, "3-10-2025", "UPI"),
            PaymentDetails("2", "Rajesh Kumar", "M002", 2, "5-10-2025", 8000, "5-10-2025", "Card"),
            PaymentDetails("3", "Suresh Reddy", "M003", 3, "8-10-2025", 9000, "8-10-2025", "Cash"),
            PaymentDetails("4", "Ramesh Verma", "M004", 2, "10-10-2025", 8000, "10-10-2025", "UPI"),
            PaymentDetails("5", "Mahesh Singh", "M005", 3, "12-10-2025", 9000, "12-10-2025", "UPI")
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
