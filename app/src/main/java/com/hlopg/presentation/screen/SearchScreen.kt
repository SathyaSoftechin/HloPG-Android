import androidx.compose.runtime.Composable
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog

@Composable
fun PGSearchScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var showFilterDialog by remember { mutableStateOf(false) }
    var showRecentSearches by remember { mutableStateOf(false) }

    // Filter states
    var selectedPriceRange by remember { mutableStateOf(0f..20000f) }
    var selectedGender by remember { mutableStateOf("All") }
    var selectedOccupancy by remember { mutableStateOf("All") }
    var selectedAmenities by remember { mutableStateOf(setOf<String>()) }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar with Search
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Search PG / Hostels",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Search Bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                showRecentSearches = it.isEmpty()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .onFocusChanged { if (it.isFocused && searchQuery.isEmpty()) showRecentSearches = true },
                            placeholder = { Text("Search PG / Hostels", fontSize = 14.sp) },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Search,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(
                                            Icons.Filled.Close,
                                            contentDescription = "Clear",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF7556FF),
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            singleLine = true
                        )

                        // Filter Button
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF7556FF))
                                .clickable { showFilterDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.FilterList,
                                contentDescription = "Filters",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            // Recent Searches Overlay
            if (showRecentSearches && searchQuery.isEmpty()) {
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
                                "Recently Searched",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Clear",
                                fontSize = 14.sp,
                                color = Color(0xFFFF6B6B),
                                modifier = Modifier.clickable { }
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        RecentSearchItem("Madhapur", "100 Main Road, Universo PG, Co-Living")
                        Spacer(Modifier.height(8.dp))
                        RecentSearchItem("KPHB", "3 line, Sai ram , Boys Hostel")
                    }
                }
            }

            // PG Listings Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(6) { index ->
                    ModernPGCard(
                        name = if (index % 2 == 0) "Universal PG (Madhapur)" else "Siva Kumar PG (Madhapur)",
                        location = "Madhapur",
                        rating = 4.5,
                        reviews = 248,
                        price = if (index % 2 == 0) 9000 else 7000,
                        badge = if (index % 2 == 0) "Girls" else "Boys",
                        badgeColor = if (index % 2 == 0) Color(0xFFFF4081) else Color(0xFF2196F3),
                        discount = if (index == 0) 20 else null,
                        isNew = index == 1,
                        isPremium = index % 3 == 0
                    )
                }
            }
        }

        // Filter Dialog
        if (showFilterDialog) {
            FilterDialog(
                priceRange = selectedPriceRange,
                selectedGender = selectedGender,
                selectedOccupancy = selectedOccupancy,
                selectedAmenities = selectedAmenities,
                onPriceRangeChange = { selectedPriceRange = it },
                onGenderChange = { selectedGender = it },
                onOccupancyChange = { selectedOccupancy = it },
                onAmenitiesChange = { selectedAmenities = it },
                onDismiss = { showFilterDialog = false },
                onApply = { showFilterDialog = false }
            )
        }
    }
}

@Composable
fun RecentSearchItem(title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            Icons.Filled.History,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
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
    onApply: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(20.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Filters",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color.Gray
                        )
                    }
                }

                Divider(color = Color(0xFFE0E0E0))

                // Filter Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Price Range
                    FilterSection(title = "Price Range") {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("₹${priceRange.start.toInt()}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Text("₹${priceRange.endInclusive.toInt()}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            }
                            RangeSlider(
                                value = priceRange,
                                onValueChange = onPriceRangeChange,
                                valueRange = 0f..20000f,
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF7556FF),
                                    activeTrackColor = Color(0xFF7556FF),
                                    inactiveTrackColor = Color(0xFFE0E0E0)
                                )
                            )
                        }
                    }

                    // Gender
                    FilterSection(title = "Gender") {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("All", "Boys", "Girls").forEach { gender ->
                                FilterChip(
                                    label = gender,
                                    selected = selectedGender == gender,
                                    onClick = { onGenderChange(gender) }
                                )
                            }
                        }
                    }

                    // Occupancy
                    FilterSection(title = "Occupancy") {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("All", "Single", "Double", "Triple", "4+").forEach { occupancy ->
                                    FilterChip(
                                        label = occupancy,
                                        selected = selectedOccupancy == occupancy,
                                        onClick = { onOccupancyChange(occupancy) }
                                    )
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
                            listOf("WiFi", "AC", "Food", "Parking", "Laundry", "Power Backup", "TV", "Gym").forEach { amenity ->
                                FilterChip(
                                    label = amenity,
                                    selected = selectedAmenities.contains(amenity),
                                    onClick = {
                                        val newSet = selectedAmenities.toMutableSet()
                                        if (newSet.contains(amenity)) newSet.remove(amenity)
                                        else newSet.add(amenity)
                                        onAmenitiesChange(newSet)
                                    }
                                )
                            }
                        }
                    }

                    // Property Type
                    FilterSection(title = "Property Type") {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("PG", "Hostel", "Co-Living", "Flat").forEach { type ->
                                FilterChip(
                                    label = type,
                                    selected = false,
                                    onClick = { }
                                )
                            }
                        }
                    }
                }

                Divider(color = Color(0xFFE0E0E0))

                // Footer Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onPriceRangeChange(0f..20000f)
                            onGenderChange("All")
                            onOccupancyChange("All")
                            onAmenitiesChange(emptySet())
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF7556FF)
                        ),
                        border = BorderStroke(1.dp, Color(0xFF7556FF))
                    ) {
                        Text("Reset", fontWeight = FontWeight.SemiBold)
                    }

                    Button(
                        onClick = onApply,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7556FF)
                        )
                    ) {
                        Text("Apply Filters", fontWeight = FontWeight.SemiBold)
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

// Add the ModernPGCard component from your original code here
@Composable
fun ModernPGCard(
    name: String,
    location: String,
    rating: Double,
    reviews: Int,
    price: Int,
    badge: String,
    badgeColor: Color,
    discount: Int? = null,
    isNew: Boolean = false,
    isPremium: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFFE0E0E0), Color(0xFFBDBDBD))
                            )
                        )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = null,
                        tint = Color.Gray.copy(alpha = 0.3f),
                        modifier = Modifier.align(Alignment.Center).size(60.dp)
                    )
                }

                if (discount != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFF5252))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "$discount% OFF",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (isNew) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF4CAF50))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "NEW",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color(0xFF7556FF),
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (isPremium) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF212121).copy(alpha = 0.8f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "PREMIUM",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(location, fontSize = 13.sp, color = Color.Gray)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(badgeColor.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            badge,
                            color = badgeColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Filled.Star,
                        null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        "$rating",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Text("($reviews reviews)", fontSize = 12.sp, color = Color.Gray)
                }

                Spacer(Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Wifi, null, tint = Color(0xFF7556FF), modifier = Modifier.size(16.dp))
                    Icon(Icons.Outlined.AcUnit, null, tint = Color(0xFF7556FF), modifier = Modifier.size(16.dp))
                    Icon(Icons.Outlined.Restaurant, null, tint = Color(0xFF7556FF), modifier = Modifier.size(16.dp))
                    Icon(Icons.Outlined.LocalParking, null, tint = Color(0xFF7556FF), modifier = Modifier.size(16.dp))
                    Text("+6", color = Color(0xFF7556FF), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "₹$price",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7556FF)
                        )
                        Text("per month", fontSize = 11.sp, color = Color.Gray)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF7556FF))
                            .clickable { }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            "View",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    PGSearchScreen()
}