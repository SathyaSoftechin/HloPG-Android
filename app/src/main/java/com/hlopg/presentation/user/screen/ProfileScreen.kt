package com.hlopg.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.hlopg.presentation.navigation.Screen

// Data Models
data class UserProfile(
    val id: String,
    val name: String,
    val email: String? = null,
    val avatarUrl: String? = null,
    val phone: String? = null
)

data class MenuItem(
    val id: String,
    val icon: ImageVector,
    val title: String,
    val iconTint: Color = Color(0xFF7556FF),
    val showArrow: Boolean = true,
    val enabled: Boolean = true,
    val onClick: () -> Unit = {}
)

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val user: UserProfile) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit = {}
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    var demoUser by remember {
        mutableStateOf(
            UserProfile(
                id = "1",
                name = "John",
                email = "john@example.com",
                avatarUrl = null
            )
        )
    }

    val menuItems = remember(onNavigate) {
        listOf(
            MenuItem(
                id = "notifications",
                icon = Icons.Outlined.Notifications,
                title = "Notifications",
                onClick = { onNavigate(Screen.Notifications.route) }
            ),
            MenuItem(
                id = "favorites",
                icon = Icons.Outlined.Favorite,
                title = "Favourite List",
                onClick = { onNavigate(Screen.Favorites.route) }
            ),
            MenuItem(
                id = "bookings",
                icon = Icons.Filled.CheckCircle,
                title = "Booked List",
                onClick = { onNavigate(Screen.BookedList.route) }
            ),
            MenuItem(
                id = "payment",
                icon = Icons.Filled.CreditCard,
                title = "Payment Details",
                onClick = { onNavigate(Screen.PaymentDetails.route) }
            ),
            MenuItem(
                id = "terms",
                icon = Icons.Filled.Info,
                title = "Terms and Conditions",
                onClick = { onNavigate(Screen.Terms.route) }
            ),
            MenuItem(
                id = "help",
                icon = Icons.Filled.Help,
                title = "Help and Support",
                onClick = { onNavigate(Screen.Help.route) }
            )
        )
    }

    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                // Clear user session and navigate to login
                onNavigate(Screen.Login.route)
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp), // Space for bottom nav
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            Text(
                text = "Account",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(Modifier.height(32.dp))

            ProfileAvatar(
                avatarUrl = demoUser.avatarUrl,
                name = demoUser.name
            )

            Spacer(Modifier.height(12.dp))

            ProfileName(
                name = demoUser.name,
                onEdit = { onNavigate(Screen.EditProfileScreen.route) }
            )

            Spacer(Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                menuItems.forEach { item ->
                    MenuItemCard(item = item)
                }

                LogoutCard(onLogout = { showLogoutDialog = true })
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Version 01",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun LogoutConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = null,
                    tint = Color(0xFF7556FF),
                    modifier = Modifier.size(64.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Logout",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Are you sure you want to logout?",
                    fontSize = 15.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF5F5F5)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Cancel",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7556FF)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Logout",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileAvatar(
    avatarUrl: String?,
    name: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Color(0xFFFFEB3B))
            .border(4.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (avatarUrl != null) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Profile Avatar",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Default Avatar",
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(60.dp)
            )
        }
    }
}

@Composable
private fun ProfileName(
    name: String,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.clickable(onClick = onEdit),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = name,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
        Spacer(Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit Name",
            tint = Color.Gray,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun MenuItemCard(
    item: MenuItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = item.enabled, onClick = item.onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.enabled) Color.White else Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = if (item.enabled) item.iconTint else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    color = if (item.enabled) Color.Black else Color.Gray,
                    fontWeight = FontWeight.Normal
                )
            }

            if (item.showArrow) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "Navigate",
                    tint = if (item.enabled) Color(0xFF9E9E9E) else Color.LightGray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun LogoutCard(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onLogout),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ExitToApp,
                contentDescription = null,
                tint = Color(0xFF7556FF),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Logout",
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
        }
    }
}