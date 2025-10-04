package com.hlopg.presentation.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun HelloPGHeader(
    selectedLocation: String = "Hyderabad",
    searchQuery: String = "",
    onLocationClick: () -> Unit = {},
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    onSearchQueryChange: (String) -> Unit = {},
    onNotificationClick: () -> Unit = {},
    navController: NavController,
    modifier: Modifier = Modifier
) {


    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp))
            .background(backgroundColor)
            .padding(20.dp)
    ) {
        Column {
            // Top row with title and notification icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hello PG",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = { navController.navigate(Screen.Notifications.route) },
                    modifier = Modifier
                        .size(36.dp)

                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Location dropdown
            Row(
                modifier = Modifier
                    .clickable { onLocationClick() }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedLocation,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                //Spacer(modifier = Modifier.width(2.dp))

                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "Select Location",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Search bar
            SearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hints: List<String> = listOf("Search Location", "Search PG Name", "Search Area"),
    hintDuration: Long = 3000L
) {
    var searchText by remember { mutableStateOf(query) }
    var currentHintIndex by remember { mutableStateOf(0) }

    LaunchedEffect(query) {
        searchText = query
    }

    // Cycle through hints automatically
    LaunchedEffect(Unit) {
        while (true) {
            delay(hintDuration)
            currentHintIndex = (currentHintIndex + 1) % hints.size
        }
    }

    Row(
        modifier = modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color.Gray.copy(),
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        BasicTextField(
            value = searchText,
            onValueChange = { newValue ->
                searchText = newValue
                onQueryChange(newValue)
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box {
                    if (searchText.isEmpty()) {
                        // Animated placeholder
                        androidx.compose.animation.AnimatedContent(
                            targetState = currentHintIndex,
                            transitionSpec = {
                            slideInVertically { height -> height } + fadeIn() togetherWith
                                    slideOutVertically { height -> -height } + fadeOut()
                        }
                        ) { index ->
                            Text(
                                text = hints[index],
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    }
                    innerTextField()
                }
            }
        )
    }
}

