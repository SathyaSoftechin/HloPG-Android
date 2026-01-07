package com.hlopg.presentation.admin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hlopg.presentation.admin.viewmodel.PaymentDetails
import com.hlopg.presentation.admin.viewmodel.PaymentsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentsListScreen(
    viewModel: PaymentsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val horizontalScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        // Header
        Text(
            text = "Payments list",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        )

        // Legend Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4ADE80))
            )
            Text(
                text = "Successful Payments",
                fontSize = 14.sp,
                color = Color(0xFF6B7280)
            )
        }

        // Month Filter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .clickable { viewModel.toggleDateRangePicker() },
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.CalendarToday,
                contentDescription = null,
                tint = Color(0xFF7556FF),
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = uiState.selectedMonth.displayName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }

        // Payments Table with Horizontal Scroll
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 20.dp)
                .weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Table Content with Horizontal Scroll
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(horizontalScrollState)
                ) {
                    // Table Header
                    Row(
                        modifier = Modifier
                            .background(Color(0xFFF9FAFB))
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        HeaderText("S.NO", modifier = Modifier.width(50.dp))
                        HeaderText("Name", modifier = Modifier.width(120.dp))
                        HeaderText("Sharing", modifier = Modifier.width(70.dp))
                        HeaderText("Joining Date", modifier = Modifier.width(100.dp))
                        HeaderText("Amount", modifier = Modifier.width(90.dp))
                    }

                    HorizontalDivider(color = Color(0xFFE5E7EB))

                    // Payments List
                    if (uiState.isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF7556FF))
                        }
                    } else if (uiState.payments.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No payments found",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    } else {
                        LazyColumn {
                            itemsIndexed(uiState.payments) { index, payment ->
                                PaymentRow(
                                    serialNo = index + 1,
                                    payment = payment
                                )
                                if (index < uiState.payments.size - 1) {
                                    HorizontalDivider(
                                        color = Color(0xFFF3F4F6),
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFFE5E7EB))

                // Total Footer
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF7556FF))
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "Total Amount : ${formatAmount(uiState.totalAmount)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(Modifier.height(160.dp))
    }

    // Light Mode Date Picker Dialog
// Date Range Picker Dialog
    if (uiState.showDateRangePicker) {

        val dateRangePickerState = rememberDateRangePickerState()

        val dateFormatter = remember {
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        }

        val startMillis = dateRangePickerState.selectedStartDateMillis
        val endMillis = dateRangePickerState.selectedEndDateMillis

        val headlineText = when {
            startMillis != null && endMillis != null ->
                "${dateFormatter.format(Date(startMillis))} - ${dateFormatter.format(Date(endMillis))}"

            startMillis != null ->
                "${dateFormatter.format(Date(startMillis))} - End date"

            else ->
                "Start date - End date"
        }

        DatePickerDialog(
            onDismissRequest = { viewModel.dismissDateRangePicker() },

            confirmButton = {
                TextButton(
                    enabled = startMillis != null && endMillis != null,
                    onClick = {
                        viewModel.setDateRange(startMillis!!, endMillis!!)
                    }
                ) {
                    Text("OK", color = Color(0xFF7556FF))
                }
            },

            dismissButton = {
                TextButton(onClick = { viewModel.dismissDateRangePicker() }) {
                    Text("Cancel", color = Color(0xFF6B7280))
                }
            },

            colors = DatePickerDefaults.colors(
                containerColor = Color.White,
                titleContentColor = Color.Black,
                headlineContentColor = Color.Black,
                weekdayContentColor = Color(0xFF6B7280),
                subheadContentColor = Color.Black,
                yearContentColor = Color(0xFF7556FF),
                currentYearContentColor = Color(0xFF7556FF),
                selectedYearContentColor = Color.White,
                selectedYearContainerColor = Color(0xFF7556FF),
                dayContentColor = Color.Black,
                disabledDayContentColor = Color(0xFFE5E7EB),
                selectedDayContentColor = Color.White,
                disabledSelectedDayContentColor = Color.White,
                selectedDayContainerColor = Color(0xFF7556FF),
                disabledSelectedDayContainerColor = Color(0xFFBFB3FF),
                todayContentColor = Color(0xFF7556FF),
                todayDateBorderColor = Color(0xFF7556FF),
                dayInSelectionRangeContentColor = Color.Black,
                dayInSelectionRangeContainerColor = Color(0xFFE9E3FF)
            )
        ) {
            DateRangePicker(
                state = dateRangePickerState,

                title = {
                    Text(
                        text = "Select date",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp),
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                },

                headline = {
                    Text(
                        text = headlineText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                },

                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    headlineContentColor = Color.Black,
                    weekdayContentColor = Color(0xFF6B7280),
                    subheadContentColor = Color.Black,
                    yearContentColor = Color(0xFF7556FF),
                    currentYearContentColor = Color(0xFF7556FF),
                    selectedYearContentColor = Color.White,
                    selectedYearContainerColor = Color(0xFF7556FF),
                    dayContentColor = Color.Black,
                    disabledDayContentColor = Color(0xFFE5E7EB),
                    selectedDayContentColor = Color.White,
                    disabledSelectedDayContentColor = Color.White,
                    selectedDayContainerColor = Color(0xFF7556FF),
                    disabledSelectedDayContainerColor = Color(0xFFBFB3FF),
                    todayContentColor = Color(0xFF7556FF),
                    todayDateBorderColor = Color(0xFF7556FF),
                    dayInSelectionRangeContentColor = Color.Black,
                    dayInSelectionRangeContainerColor = Color(0xFFE9E3FF)
                )
            )
        }
    }

}

@Composable
fun HeaderText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF6B7280),
        modifier = modifier
    )
}

@Composable
fun PaymentRow(
    serialNo: Int,
    payment: PaymentDetails
) {
    Row(
        modifier = Modifier
            .background(Color(0xFFD1FAE5))
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$serialNo.",
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.width(50.dp)
        )
        Text(
            text = payment.memberName,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = payment.sharing.toString(),
            fontSize = 14.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(70.dp)
        )
        Text(
            text = payment.joiningDate,
            fontSize = 14.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = formatAmount(payment.amount),
            fontSize = 14.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(90.dp)
        )
    }
}

private fun formatAmount(amount: Int): String {
    return "₹ ${String.format("%,d", amount)}"
}