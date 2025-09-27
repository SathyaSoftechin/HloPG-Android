package com.hlopg.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OrDivider() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Or",
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 14.sp,
            color = Color.Gray
        )
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
@Preview
fun OrDividerPreview() {
    OrDivider()
}
