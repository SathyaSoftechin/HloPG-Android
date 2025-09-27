package com.hlopg.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MobileNumberField(modifier: Modifier = Modifier) {
    val mobileNumber = remember { mutableStateOf("") }

    Column(modifier = modifier) {
        Box(modifier = Modifier.padding(0.dp,5.dp)) {
            Text(text = "Mobile Number")

        }

        OutlinedTextField(
            value = mobileNumber.value,
            onValueChange = { mobileNumber.value = it },
            label = { Text("Enter your mobile number") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp),
            contentAlignment = Alignment.Center

        ) {
            Text(text = "Code will be sent to given Number")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MobileNumberFieldPreview() {
    MobileNumberField()
}
