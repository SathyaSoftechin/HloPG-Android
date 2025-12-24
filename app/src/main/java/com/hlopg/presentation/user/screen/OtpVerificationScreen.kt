package com.hlopg.presentation.user.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hlopg.domain.repository.Resource
import com.hlopg.presentation.user.viewmodel.OTPViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPVerificationScreen(
    phoneNumber: String = "+91 9012345678",
    purpose: String = "register", // "register", "login", "reset_password"
    onBackClick: () -> Unit = {},
    onOTPVerified: () -> Unit = {},
    viewModel: OTPViewModel = viewModel()
) {
    val context = LocalContext.current
    var otpValues by remember { mutableStateOf(List(4) { "" }) }
    val focusRequesters = remember { List(4) { FocusRequester() } }
    var timeLeft by remember { mutableStateOf(59) }
    var isResending by remember { mutableStateOf(false) }

    // Observe ViewModel states
    val otpResult by viewModel.otpResult.observeAsState()
    val resendResult by viewModel.resendResult.observeAsState()

    // Set phone number in ViewModel
    LaunchedEffect(phoneNumber) {
        viewModel.setPhoneNumber(phoneNumber)
    }

    // Handle OTP verification result
    LaunchedEffect(otpResult) {
        when (otpResult) {
            is Resource.Success -> {
                Toast.makeText(context, "OTP verified successfully!", Toast.LENGTH_SHORT).show()
                onOTPVerified()
            }
            is Resource.Error -> {
                val errorMessage = (otpResult as Resource.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                // Clear OTP fields on error
                otpValues = List(4) { "" }
                focusRequesters[0].requestFocus()
            }
            is Resource.Loading -> {
                // Show loading indicator (handled by CircularProgressIndicator)
            }
            null -> {
                // Initial state
            }
        }
    }

    // Handle resend OTP result
    LaunchedEffect(resendResult) {
        when (resendResult) {
            is Resource.Success -> {
                Toast.makeText(context, "OTP sent successfully!", Toast.LENGTH_SHORT).show()
                isResending = false
                timeLeft = 59 // Reset timer
            }
            is Resource.Error -> {
                val errorMessage = (resendResult as Resource.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                isResending = false
            }
            is Resource.Loading -> {
                isResending = true
            }
            null -> {
                // Initial state
            }
        }
    }

    // Timer countdown
    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Top AppBar
            TopAppBar(
                title = {
                    Text(
                        text = "OTP Verification",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            // OTP Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "We have sent a verification code to $phoneNumber",
                        fontSize = 13.sp,
                        color = Color.Black.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // OTP Input Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                    ) {
                        otpValues.forEachIndexed { index, value ->
                            OTPInputBox(
                                value = value,
                                onValueChange = { newValue ->
                                    if (newValue.length <= 1 && (newValue.isEmpty() || newValue.all { it.isDigit() })) {
                                        val newOtpValues = otpValues.toMutableList()
                                        newOtpValues[index] = newValue
                                        otpValues = newOtpValues

                                        // Move to next box
                                        if (newValue.isNotEmpty() && index < 3) {
                                            focusRequesters[index + 1].requestFocus()
                                        }

                                        // Complete OTP and verify
                                        if (newOtpValues.all { it.isNotEmpty() }) {
                                            val otp = newOtpValues.joinToString("")
                                            viewModel.verifyOTP(phoneNumber, otp, purpose)
                                        }
                                    }
                                },
                                focusRequester = focusRequesters[index],
                                onBackspace = {
                                    if (value.isEmpty() && index > 0) {
                                        focusRequesters[index - 1].requestFocus()
                                    }
                                },
                                enabled = otpResult !is Resource.Loading
                            )
                        }
                    }

                    // Loading indicator
                    if (otpResult is Resource.Loading) {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Timer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = if (timeLeft > 0) "0:${timeLeft.toString().padStart(2, '0')}" else "0:00",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Resend OTP
                    TextButton(
                        onClick = {
                            if (timeLeft == 0) {
                                viewModel.resendOTP(phoneNumber, purpose)
                            }
                        },
                        enabled = timeLeft == 0 && !isResending
                    ) {
                        if (isResending) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Text(
                                text = if (timeLeft == 0) "Resend OTP" else "Resend OTP (${timeLeft}s)",
                                fontSize = 13.sp,
                                color = if (timeLeft == 0)
                                    MaterialTheme.colorScheme.primary
                                else
                                    Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Instructions
                    Text(
                        text = "Enter the 4-digit code sent to your phone",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }

    // Auto-focus first OTP input
    LaunchedEffect(Unit) {
        focusRequesters[0].requestFocus()
    }
}

@Composable
fun OTPInputBox(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    onBackspace: () -> Unit,
    enabled: Boolean = true
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .border(
                width = 1.5.dp,
                color = if (value.isEmpty()) Color(0xFFE0E0E0) else MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.White, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.isEmpty()) onBackspace()
                onValueChange(newValue)
            },
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .padding(4.dp),
            enabled = enabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                textAlign = TextAlign.Center
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    innerTextField()
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewOTPVerificationScreen() {
    OTPVerificationScreen()
}