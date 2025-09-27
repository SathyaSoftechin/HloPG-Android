package com.hlopg.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hlopg.presentation.components.LoginTopBar

@Composable
fun OTPInputBox(
    value: String,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        ),
        modifier = Modifier
            .size(52.dp)
            .border(
                width = 2.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
    ) { innerTextField ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            innerTextField()
        }
    }
}

@Composable
fun VerifyOtpButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFBDBDBD)
        )
    ) {
        Text(
            text = "Verify",
            fontSize = 18.sp,
            color = Color.White
        )
    }
}

@Composable
fun OTPVerificationScreen(modifier: Modifier = Modifier) {
    var otpValue by remember { mutableStateOf("") }
    var timeRemaining by remember { mutableStateOf(300) } // 5 minutes in seconds

    // Convert seconds to MM:SS format
    val minutes = timeRemaining / 60
    val seconds = timeRemaining % 60
    val timeString = String.format("%02d:%02d", minutes, seconds)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LoginTopBar(
                title = "OTP Verification",
                onBackClick = {},
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Enter the code from the SMS\nwe sent you",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // OTP Input boxes
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(vertical = 24.dp)
                ) {
                    repeat(6) { index ->
                        OTPInputBox(
                            value = if (index < otpValue.length) otpValue[index].toString() else "",
                            onValueChange = { newValue ->
                                if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                    val newOtp = otpValue.toMutableList()
                                    while (newOtp.size <= index) {
                                        newOtp.add('0')
                                    }
                                    if (newValue.isNotEmpty()) {
                                        if (index < newOtp.size) {
                                            newOtp[index] = newValue[0]
                                        }
                                    } else if (index < newOtp.size) {
                                        newOtp.removeAt(index)
                                    }
                                    otpValue = newOtp.take(6).joinToString("")
                                }
                            }
                        )
                    }
                }

                // Timer
                Text(
                    text = timeString,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Resend text
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "I didn't receive the code! ",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Resend",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }

                VerifyOtpButton { /* handle verification */ }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "By verifying, you agree to HloPG\nPrivacy Policy and Terms and Conditions.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OTPVerificationScreenPreview() {
    OTPVerificationScreen()
}