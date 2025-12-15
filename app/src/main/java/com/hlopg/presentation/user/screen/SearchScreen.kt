package com.hlopg.presentation.user.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.hlopg.presentation.components.ModernPGCard
import com.hlopg.presentation.components.PGDetails
import com.hlopg.presentation.user.viewmodel.PGSearchViewModel

@Composable
fun PGSearchScreen(
    viewModel: PGSearchViewModel = hiltViewModel(),
    onPGClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFilterDialog by remember { mutableStateOf(false) }
    var showRecentSearches by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            /* -------------------- TOP BAR -------------------- */
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Search Bar
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = {
                                viewModel.onSearchQueryChange(it)
                                showRecentSearches = it.isEmpty()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .onFocusChanged {
                                    if (it.isFocused && uiState.searchQuery.isEmpty()) {
                                        showRecentSearches = true
                                    }
                                },
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 18.sp,
                                color = Color.Black.copy(alpha = 0.8f)
                            ),
                            placeholder = {
                                Text("Search PG / Hostels / City")
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Search,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            trailingIcon = {
                                if (uiState.searchQuery.isNotEmpty()) {
                                    IconButton(onClick = {
                                        viewModel.onSearchQueryChange("")
                                        showRecentSearches = false
                                    }) {
                                        Icon(
                                            Icons.Filled.Close,
                                            contentDescription = "Clear",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            },
                            shape = RoundedCornerShape(30.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF7556FF),
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            singleLine = true
                        )

                        // 🎛 Filter Button
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.LightGray, CircleShape)
                                .background(Color.White)
                                .clickable { showFilterDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Tune,
                                contentDescription = "Filters",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            /* -------------------- RECENT SEARCHES / CITY SHORTCUTS -------------------- */
            if (showRecentSearches && uiState.searchQuery.isEmpty() && !uiState.isLoading) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Quick Search",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        RecentSearchItem(
                            title = "Hyderabad",
                            subtitle = "Search PGs in Hyderabad",
                            onClick = {
                                viewModel.searchByCity("Hyderabad")
                                showRecentSearches = false
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                        RecentSearchItem(
                            title = "Chennai",
                            subtitle = "Search PGs in Chennai",
                            onClick = {
                                viewModel.searchByCity("Chennai")
                                showRecentSearches = false
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                        RecentSearchItem(
                            title = "Bangalore",
                            subtitle = "Search PGs in Bangalore",
                            onClick = {
                                viewModel.searchByCity("Bangalore")
                                showRecentSearches = false
                            }
                        )
                    }
                }
            }

            /* -------------------- LOADING STATE -------------------- */
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(color = Color(0xFF7556FF))
                        Text(
                            "Loading PGs...",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
            /* -------------------- ERROR STATE -------------------- */
            else if (uiState.error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Icon(
                            Icons.Filled.Error,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            "Oops! Something went wrong",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            uiState.error ?: "Failed to load PGs",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Button(
                            onClick = { viewModel.retry() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF7556FF)
                            )
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }
            /* -------------------- EMPTY STATE -------------------- */
            else if (uiState.filteredPGs.isEmpty() && uiState.searchQuery.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Icon(
                            Icons.Filled.SearchOff,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            "No PGs found",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Text(
                            "Try searching with different keywords",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(Modifier.height(8.dp))
                        TextButton(
                            onClick = {
                                viewModel.onSearchQueryChange("")
                                viewModel.resetFilters()
                            }
                        ) {
                            Text(
                                "Clear Search & Filters",
                                color = Color(0xFF7556FF),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            /* -------------------- PG GRID -------------------- */
            else if (uiState.filteredPGs.isNotEmpty()) {
                Column {
                    // Results count
                    if (uiState.searchQuery.isNotEmpty()) {
                        Text(
                            "${uiState.filteredPGs.size} PGs found",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    PGGrid(
                        pgList = uiState.filteredPGs,
                        onCardClick = onPGClick,
                        onFavoriteClick = viewModel::onFavoriteClick
                    )
                }
            }
        }

        /* -------------------- FILTER DIALOG -------------------- */
        if (showFilterDialog) {
            FilterDialog(
                priceRange = uiState.selectedPriceRange,
                selectedGender = uiState.selectedGender,
                selectedOccupancy = uiState.selectedOccupancy,
                selectedAmenities = uiState.selectedAmenities,
                onPriceRangeChange = viewModel::updatePriceRange,
                onGenderChange = viewModel::updateGender,
                onOccupancyChange = viewModel::updateOccupancy,
                onAmenitiesChange = viewModel::updateAmenities,
                onDismiss = { showFilterDialog = false },
                onApply = { showFilterDialog = false },
                onReset = {
                    viewModel.resetFilters()
                }
            )
        }
    }
}

@Composable
fun RecentSearchItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.LocationOn,
            contentDescription = null,
            tint = Color(0xFF7556FF),
            modifier = Modifier.size(24.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Text(
                subtitle,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(
            Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun PGGrid(
    pgList: List<PGDetails>,
    onCardClick: (String) -> Unit,
    onFavoriteClick: (String, Boolean) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            count = pgList.size,
            key = { index -> pgList[index].id }
        ) { index ->
            val pg = pgList[index]
            ModernPGCard(
                pgDetails = pg,
                onCardClick = onCardClick,
                onFavoriteClick = onFavoriteClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun FilterDialog(
    priceRange: ClosedFloatingPointRange<Float>,
    selectedGender: String,
    selectedOccupancy: String,
    selectedAmenities: Set<String>,
    onPriceRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onGenderChange: (String) -> Unit,
    onOccupancyChange: (String) -> Unit,
    onAmenitiesChange: (Set<String>) -> Unit,
    onDismiss: () -> Unit,
    onApply: () -> Unit,
    onReset: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.98f)
                .height(460.dp),
            shape = RoundedCornerShape(10.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Filters",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                HorizontalDivider(color = Color(0xFFE0E0E0))

                // Filter Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Gender
                    FilterSection(title = "Gender") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Boys", "Women", "Co-Living").forEach { gender ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (selectedGender == gender)
                                                Color(0xFF7556FF).copy(alpha = 0.1f)
                                            else Color(0xFFF5F5F5)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (selectedGender == gender)
                                                Color(0xFF7556FF)
                                            else Color.Transparent,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable { onGenderChange(gender) }
                                        .padding(vertical = 2.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        gender,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (selectedGender == gender)
                                            Color(0xFF7556FF)
                                        else Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    // Occupancy
                    FilterSection(title = "Occupancy") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                "2-Sharing" to Icons.Default.Person,
                                "3-Sharing" to Icons.Default.Person,
                                "4-Sharing" to Icons.Default.Person,
                                "5-Sharing" to Icons.Default.Person
                            ).forEach { (occupancy, icon) ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (selectedOccupancy == occupancy)
                                                Color(0xFF7556FF).copy(alpha = 0.1f)
                                            else Color(0xFFF5F5F5)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (selectedOccupancy == occupancy)
                                                Color(0xFF7556FF)
                                            else Color.Transparent,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable { onOccupancyChange(occupancy) }
                                        .padding(vertical = 2.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            occupancy.split("-")[0],
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (selectedOccupancy == occupancy)
                                                Color(0xFF7556FF)
                                            else Color.Gray
                                        )
                                        Icon(
                                            icon,
                                            contentDescription = null,
                                            tint = if (selectedOccupancy == occupancy)
                                                Color(0xFF7556FF)
                                            else Color.Gray,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Amenities
                    FilterSection(title = "Amenities") {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                "Air conditioning",
                                "Elevator",
                                "Extra Storage",
                                "Gym",
                                "High speed internet",
                                "Garage",
                                "Pet allowed"
                            ).forEach { amenity ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (selectedAmenities.contains(amenity))
                                                Color(0xFF7556FF).copy(alpha = 0.1f)
                                            else Color(0xFFF5F5F5)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (selectedAmenities.contains(amenity))
                                                Color(0xFF7556FF)
                                            else Color.Transparent,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable {
                                            val newSet = selectedAmenities.toMutableSet()
                                            if (newSet.contains(amenity)) newSet.remove(amenity)
                                            else newSet.add(amenity)
                                            onAmenitiesChange(newSet)
                                        }
                                        .padding(horizontal = 14.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        amenity,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (selectedAmenities.contains(amenity))
                                            Color(0xFF7556FF)
                                        else Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    // Monthly Price Range
                    FilterSection(title = "Montly Price Range") {
                        Column {
                            // Slider
                            Slider(
                                value = priceRange.start,
                                onValueChange = {
                                    onPriceRangeChange(it..priceRange.endInclusive)
                                },
                                valueRange = 0f..20000f,
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF7556FF),
                                    activeTrackColor = Color(0xFF7556FF),
                                    inactiveTrackColor = Color(0xFFE0E0E0)
                                )
                            )

                            // Price Display
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "₹${priceRange.start.toInt().toString().replace(Regex("(\\d)(?=(\\d{3})+$)"), "$1,")}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFFE0E0E0))

                // Footer Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onReset,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Gray
                        ),
                        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                    ) {
                        Text(
                            "Clear All",
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = onApply,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7556FF)
                        )
                    ) {
                        Text(
                            "Save Changes",
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        content()
    }
}

@Composable
fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) Color(0xFF7556FF) else Color(0xFFF0F0F0))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (selected) Color.White else Color.Gray
        )
    }
}