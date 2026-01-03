package com.hlopg.presentation.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.hlopg.app.Screen

@Composable
fun AdminBottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val leftItems = listOf(
        Screen.AdminHome.route to Icons.Outlined.Home,
        Screen.PGMembersList.route to Icons.Outlined.Business
    )

    val rightItems = listOf(
        Screen.PaymentList.route to Icons.Outlined.CreditCard,
        Screen.AdminProfile.route to Icons.Outlined.Person
    )

    Surface(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .shadow(10.dp, RoundedCornerShape(50.dp)),
        shape = RoundedCornerShape(50.dp),
        color = Color(0xFF1E1E1E)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // LEFT ITEMS
            leftItems.forEach { (route, icon) ->
                BottomNavItem(
                    icon = icon,
                    selected = currentRoute == route,
                    onClick = {
                        if (currentRoute != route) {
                            onNavigate(route)
                        }
                    }
                )
            }

            // CENTER ADD BUTTON
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF7556FF))
                    .clickable {
                        //onNavigate(Screen.AddPG.route) // safe even if screen not ready
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add New PG",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // RIGHT ITEMS
            rightItems.forEach { (route, icon) ->
                BottomNavItem(
                    icon = icon,
                    selected = currentRoute == route,
                    onClick = {
                        if (currentRoute != route) {
                            onNavigate(route)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(if (selected) Color.White else Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) Color.Black else Color.White.copy(alpha = 0.6f),
            modifier = Modifier.size(24.dp)
        )
    }
}
