package com.hlopg.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RoleSelectionScreen(
    onSignupClick: (UserType) -> Unit,
    onLoginClick: (UserType) -> Unit
) {
    var selectedType by remember { mutableStateOf<UserType?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 60.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HeaderSection()

            Spacer(modifier = Modifier.height(24.dp))

            UserTypeCard(
                icon = Icons.Default.Groups,
                title = "I'm a Student, looking for a",
                subtitle = "PG's / Hostels",
                isSelected = selectedType == UserType.STUDENT,
                onClick = { selectedType = UserType.STUDENT }
            )

            Spacer(modifier = Modifier.height(20.dp))

            UserTypeCard(
                icon = Icons.Default.Home,
                title = "I'm a Owner, hosting PG's /",
                subtitle = "Hostels",
                isSelected = selectedType == UserType.OWNER,
                onClick = { selectedType = UserType.OWNER }
            )

            Spacer(modifier = Modifier.height(36.dp))

            CreateAccountButton(
                enabled = selectedType != null,
                onClick = {
                    selectedType?.let { onSignupClick(it) }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LoginText(
                enabled = selectedType != null,
                onLoginClick = {
                    selectedType?.let { onLoginClick(it) }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Header
@Composable
fun HeaderSection() {
    Text(
        text = "Join as User or Owner",
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black.copy(alpha = 0.8f)
    )
}

// Role Card
@Composable
fun UserTypeCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF3E5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFF7C4DFF),
                        modifier = Modifier.size(26.dp)
                    )
                }

                RadioButton(
                    selected = isSelected,
                    onClick = onClick,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = Color.Gray
                    ),
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = Color(0xFF7C4DFF),
                    fontWeight = FontWeight.Medium,
                    lineHeight = 22.sp
                )
                Text(
                    text = subtitle,
                    fontSize = 16.sp,
                    color = Color(0xFF7C4DFF),
                    fontWeight = FontWeight.Medium,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

// Button
@Composable
fun CreateAccountButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(12.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) Color(0xFF7C4DFF) else Color(0xFFBDBDBD)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = "Create an Account",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

// Login text
@Composable
fun LoginText(
    enabled: Boolean,
    onLoginClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Already have an Account? ",
            fontSize = 14.sp,
            color = Color.Black.copy(alpha = 0.95f)
        )
        TextButton(
            onClick = onLoginClick,
            enabled = enabled,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Login",
                fontSize = 14.sp,
                color = if (enabled) Color(0xFF7C4DFF) else Color.Gray,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

enum class UserType { STUDENT, OWNER }

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRoleSelectionScreen() {
    RoleSelectionScreen(
        onSignupClick = {},
        onLoginClick = {}
    )
}