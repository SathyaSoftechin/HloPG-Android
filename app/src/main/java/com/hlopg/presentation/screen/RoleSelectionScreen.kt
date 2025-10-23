package com.hlopg.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hlopg.R

// Main Screen
@Composable
fun RoleSelectionScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.White
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 60.dp, bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Header with Icon and Text
                HeaderSection()

                Spacer(modifier = Modifier.height(24.dp))

                // User Type Cards
                var selectedType by remember { mutableStateOf<UserType?>(null) }

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

                // Create Account Button
                CreateAccountButton()

                Spacer(modifier = Modifier.height(16.dp))

                // Login Text
                LoginText()

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}



@Composable
fun HeaderSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Join as User Or Owner",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black.copy(alpha = 0.8f)
        )
    }
}

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
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
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
                        selectedColor = Color(0xFF7C4DFF),
                        unselectedColor = Color(0xFFCCCCCC)
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

@Composable
fun CreateAccountButton() {
    Button(
        onClick = { /* Handle create account */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp
        )
    ) {
        Text(
            text = "Create an Account",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
fun LoginText() {
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
            onClick = { /* Handle login */ },
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Login",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// User Type Enum
enum class UserType {
    STUDENT,
    OWNER
}

// Preview
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRoleSelectionScreen() {
    RoleSelectionScreen()
}