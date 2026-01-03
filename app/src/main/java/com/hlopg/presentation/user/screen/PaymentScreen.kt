package com.hlopg.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(onBackClick: () -> Boolean, onPaymentSuccess: () -> Unit) {

    var checkInDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedSharingType by remember { mutableStateOf("Single(1)") }
    var showSharingDropdown by remember { mutableStateOf(false) }

    val sharingOptions = listOf("Single(1)", "Double(2)", "Triple(3)", "Four(4)")

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis?.let {
                        checkInDate = SimpleDateFormat(
                            "dd MMM yyyy",
                            Locale.getDefault()
                        ).format(Date(it))
                    }
                }) { Text("OK", color = Color(0xFF2979FF)) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            colors = androidx.compose.material3.DatePickerDefaults.colors(
                containerColor = Color.White,
                titleContentColor = Color.Black,
                headlineContentColor = Color.Black,
                weekdayContentColor = Color.Black,
                subheadContentColor = Color.Black,
                yearContentColor = Color.Black,
                currentYearContentColor = Color(0xFF2979FF),
                selectedYearContentColor = Color.White,
                selectedYearContainerColor = Color(0xFF2979FF),
                dayContentColor = Color.Black,
                disabledDayContentColor = Color.Gray,
                selectedDayContentColor = Color.White,
                disabledSelectedDayContentColor = Color.White,
                selectedDayContainerColor = Color(0xFF2979FF),
                disabledSelectedDayContainerColor = Color.Gray,
                todayContentColor = Color(0xFF2979FF),
                todayDateBorderColor = Color(0xFF2979FF),
                dayInSelectionRangeContentColor = Color.Black,
                dayInSelectionRangeContainerColor = Color(0xFF2979FF).copy(alpha = 0.2f)
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = androidx.compose.material3.DatePickerDefaults.colors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    headlineContentColor = Color.Black,
                    weekdayContentColor = Color.Black,
                    subheadContentColor = Color.Black,
                    yearContentColor = Color.Black,
                    currentYearContentColor = Color(0xFF2979FF),
                    selectedYearContentColor = Color.White,
                    selectedYearContainerColor = Color(0xFF2979FF),
                    dayContentColor = Color.Black,
                    disabledDayContentColor = Color.Gray,
                    selectedDayContentColor = Color.White,
                    disabledSelectedDayContentColor = Color.White,
                    selectedDayContainerColor = Color(0xFF2979FF),
                    disabledSelectedDayContainerColor = Color.Gray,
                    todayContentColor = Color(0xFF2979FF),
                    todayDateBorderColor = Color(0xFF2979FF),
                    dayInSelectionRangeContentColor = Color.Black,
                    dayInSelectionRangeContainerColor = Color(0xFF2979FF).copy(alpha = 0.2f)
                )
            )
        }
    }

    // Force white background for entire screen
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Scaffold(
            containerColor = Color.White,
            topBar = {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 4.dp,
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { /* Handle back */ }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Black
                            )
                        }
                        Text(
                            "Payment",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
                        Spacer(Modifier.width(48.dp))
                    }
                }
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .background(Color.White)
            ) {

                // PG CARD
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray)
                        )

                        Column(modifier = Modifier.weight(1f)) {

                            Text(
                                "Siva Kumar PG (Madhapur)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.Black
                            )

                            Spacer(Modifier.height(6.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF7556FF))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text("Boys", color = Color.White, fontSize = 12.sp)
                                }

                                Spacer(Modifier.width(8.dp))

                                Icon(
                                    Icons.Filled.Star,
                                    null,
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text("4.5/5", fontSize = 12.sp, color = Color.Black)
                            }

                            Spacer(Modifier.height(6.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                AmenityChip(Icons.Outlined.Wifi)
                                AmenityChip(Icons.Outlined.Restaurant)
                                AmenityChip(Icons.Outlined.AcUnit)
                                AmenityChip(Icons.Outlined.Tv)

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFEDE7FF))
                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text("+8", fontSize = 12.sp, color = Color(0xFF7556FF))
                                }
                            }

                            Spacer(Modifier.height(8.dp))

                            Text(
                                "₹7,000 / Month",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // SHARING TYPE
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Select sharing Type",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )

                    Box {
                        SharingDropdown(selectedSharingType) {
                            showSharingDropdown = true
                        }

                        DropdownMenu(
                            expanded = showSharingDropdown,
                            onDismissRequest = { showSharingDropdown = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            sharingOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            option,
                                            color = Color.Black
                                        )
                                    },
                                    onClick = {
                                        selectedSharingType = option
                                        showSharingDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // CHECK IN DATE
                OutlinedTextField(
                    value = checkInDate,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
                    placeholder = { Text("Check - In", color = Color.Gray) },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                Icons.Filled.CalendarToday,
                                contentDescription = "Select date",
                                tint = Color(0xFF7556FF)
                            )
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.Black,
                        disabledBorderColor = Color.Gray,
                        disabledContainerColor = Color.White,
                        disabledPlaceholderColor = Color.Gray,
                        disabledTrailingIconColor = Color(0xFF7556FF)
                    )
                )

                Spacer(Modifier.height(24.dp))

                // PRICE DETAILS
                Text(
                    "Price Details",
                    color = Color(0xFF2979FF),
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(12.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF5F5F5)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PriceRow("Total Amount", "₹17,500")
                        PriceRow("Pg Price", "₹15,000")
                        PriceRow("Security Deposit", "₹2,000")
                        PriceRow("Discount", "₹500", discount = true)
                        HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))
                        PriceRow("Grand Total", "₹17,000", bold = true)
                    }
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2979FF)
                    )
                ) {
                    Text("Pay Now", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

/* ---------------- COMPONENTS ---------------- */

@Composable
fun AmenityChip(icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF7556FF).copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = Color(0xFF7556FF), modifier = Modifier.size(16.dp))
    }
}

@Composable
fun SharingDropdown(selected: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF2979FF))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(selected, color = Color.White, fontSize = 13.sp)
            Spacer(Modifier.width(6.dp))
            Text("▼", color = Color.White, fontSize = 10.sp)
        }
    }
}

@Composable
fun PriceRow(
    label: String,
    amount: String,
    bold: Boolean = false,
    discount: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray)
        Text(
            amount,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Medium,
            color = when {
                discount -> Color(0xFF4CAF50)
                bold -> Color(0xFF2979FF)
                else -> Color.Black
            }
        )
    }
}

