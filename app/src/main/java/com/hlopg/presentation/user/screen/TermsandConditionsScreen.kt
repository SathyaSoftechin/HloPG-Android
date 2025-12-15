package com.hlopg.presentation.user.screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAndConditionsScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Terms And Conditions",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Introduction
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Gray)) {
                        append("Last Updated: 23/07/2025\n")
                    }
                    append("Welcome to PG Finder! By using our mobile application or website, you agree to the following Terms and Conditions. Please read them carefully.")
                },
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 1. Acceptance of Terms
            SectionTitle("1. Acceptance of Terms")
            Spacer(modifier = Modifier.height(8.dp))
            SectionContent(
                "By downloading, accessing, or using the PG Finder app (\"App\"), you agree to be bound by these Terms and Conditions and our Privacy Policy. If you do not agree, please do not use the app."
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Services Offered
            SectionTitle("2. Services Offered")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "PG Finder provides a platform for users to:",
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            BulletPoint("Search and book Paying Guest (PG) accommodations")
            BulletPoint("Search and book Rental Rooms")
            BulletPoint("View property details, amenities, availability, and pricing")
            BulletPoint("Contact property owners or managers")

            Spacer(modifier = Modifier.height(20.dp))

            // 3. User Eligibility
            SectionTitle("3. User Eligibility")
            Spacer(modifier = Modifier.height(8.dp))
            SectionContent(
                "You must be at least 18 years of age to use the app. By using PG Finder, you confirm that you meet this requirement."
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Booking and Payments
            SectionTitle("4. Booking and Payments")
            Spacer(modifier = Modifier.height(8.dp))
            BulletPoint("PG Finder may allow bookings through the platform or may redirect you to third-party payment portals.")
            BulletPoint("All payments, terms of stay, security deposits, and cancellation policies are governed by the respective property owners.")
            BulletPoint("PG Finder is not responsible for any disputes or losses related to payments, deposits, or stay arrangements.")

            Spacer(modifier = Modifier.height(20.dp))

            // 5. User Responsibilities
            SectionTitle("5. User Responsibilities")
            Spacer(modifier = Modifier.height(8.dp))
            BulletPoint("Provide accurate information during registration and bookings.")

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

@Composable
private fun SectionContent(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = Color.DarkGray,
        lineHeight = 22.sp
    )
}

@Composable
private fun BulletPoint(text: String) {
    Row(
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    ) {
        Text(
            text = "• ",
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.DarkGray,
            lineHeight = 22.sp,
            modifier = Modifier.weight(1f)
        )
    }
}