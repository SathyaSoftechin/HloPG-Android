package com.hlopg.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OtpInput(
    otpLength: Int = 6,
    onOtpComplete: (String) -> Unit
) {
    val otpValues = remember { mutableStateListOf(*Array(otpLength) { "" }) }
    val focusRequesters = List(otpLength) { FocusRequester() }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        otpValues.forEachIndexed { index, value ->
            OutlinedTextField(
                value = value,
                onValueChange = { newValue ->
                    if (newValue.length <= 1) {
                        otpValues[index] = newValue

                        // Move focus to next field if a digit is entered
                        if (newValue.isNotEmpty() && index < otpLength - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }

                        // If backspace on empty -> go back
                        if (newValue.isEmpty() && index > 0) {
                            focusRequesters[index - 1].requestFocus()
                        }

                        // If all filled -> send OTP
                        if (otpValues.all { it.isNotEmpty() }) {
                            onOtpComplete(otpValues.joinToString(""))
                        }
                    }
                },
                modifier = Modifier
                    .width(48.dp)
                    .focusRequester(focusRequesters[index]),
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                ),
                singleLine = true,
                maxLines = 1
            )
        }
    }

    // Request focus on first box when opened
    LaunchedEffect(Unit) {
        focusRequesters.first().requestFocus()
    }


}


@Composable
@Preview
fun OtpInputPreview() {
    OtpInput { 1;2 }
}