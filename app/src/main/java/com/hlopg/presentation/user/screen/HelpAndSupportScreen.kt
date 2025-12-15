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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpAndSupportScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Help And Support",
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

            // Welcome text
            Text(
                text = "Welcome to PG Finder! We're here to help you have a smooth and stress-free experience while finding your ideal PG or rental room.",
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // How to Search section
            SectionHeader(
                icon = "🔍",
                title = "How to Search for a PG or Rental Room"
            )
            Spacer(modifier = Modifier.height(12.dp))
            NumberedStep("1.", "Go to the Home screen.")
            NumberedStep("2.", "Enter your preferred location (e.g., Madhapur, KPHB).")
            NumberedStep("3.", "Use filters to refine results by room type, budget, amenities, etc.")
            NumberedStep("4.", "Browse the listings and tap any property to view details.")

            Spacer(modifier = Modifier.height(32.dp))

            // How to Book section
            SectionHeader(
                icon = "📆",
                title = "How to Book a Room"
            )
            Spacer(modifier = Modifier.height(12.dp))
            NumberedStep("1.", "Tap on the listing you like.")
            NumberedStep("2.", "View all details and photos.")
            NumberedStep("3.", "Click \"Book Now\" or \"Contact Owner\".")
            NumberedStep("4.", "Follow the booking instructions or wait for confirmation.")

            Spacer(modifier = Modifier.height(32.dp))

            // Payment section
            SectionHeader(
                icon = "💳",
                title = "Do I Need to Pay Through the App?"
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Currently, payment is handled directly with the property owner or manager. Always verify details before making any transaction.",
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // View Bookings section
            SectionHeader(
                icon = "📋",
                title = "How to View My Bookings"
            )
            Spacer(modifier = Modifier.height(12.dp))
            NumberedStep("1.", "Tap the Profile icon.")
            NumberedStep("2.", "Go to \"My Bookings\" to see upcoming or past bookings.")

            Spacer(modifier = Modifier.height(32.dp))

            // Need More Help section
            SectionHeader(
                icon = "❓",
                title = "Need More Help?"
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "If you have any issues or questions, feel free to contact us:",
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            ContactInfo("📧", "Email: support@pgfinder.com")
            ContactInfo("📞", "Phone: +91-XXXXXXXXXX")
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "We're happy to help you 24/7!",
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionHeader(icon: String, title: String) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = icon,
            fontSize = 20.sp,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
private fun NumberedStep(number: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text(
            text = number,
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(end = 8.dp)
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

@Composable
private fun ContactInfo(icon: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = icon,
            fontSize = 14.sp,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.DarkGray,
            lineHeight = 22.sp
        )
    }
}