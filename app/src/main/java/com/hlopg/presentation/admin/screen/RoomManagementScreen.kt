package com.hlopg.presentation.admin.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hlopg.presentation.admin.viewmodel.RoomData
import com.hlopg.presentation.admin.viewmodel.RoomManagementViewModel
import kotlinx.coroutines.launch

// Color scheme for bed states
object BedColors {
    val Available = Color(0xFF4CAF50) // Green
    val Occupied = Color(0xFFF44336) // Red
    val Maintenance = Color(0xFFFF9800) // Orange
    val Reserved = Color(0xFF2196F3) // Blue
}

enum class RoomFilter {
    ALL, AVAILABLE, FULL, PARTIAL, EMPTY
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomManagementScreen(
    navController: NavController,
    viewModel: RoomManagementViewModel = hiltViewModel()
) {
    // Get navigation arguments from previous screen
    val numberOfFloors = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<Int>("numberOfFloors") ?: 5
    val roomsPerFloor = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<Int>("roomsPerFloor") ?: 5
    val startingRoomNumber = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("startingRoomNumber") ?: "101"

    // Initialize rooms when screen loads
    LaunchedEffect(Unit) {
        viewModel.initializeRooms(numberOfFloors, roomsPerFloor, startingRoomNumber)
    }

    val roomsState by viewModel.roomsState.collectAsState()
    val statistics by viewModel.statisticsState.collectAsState()

    // Use derivedStateOf to prevent recalculation on every recomposition
    val floors by remember {
        derivedStateOf { viewModel.getAllFloors() }
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(RoomFilter.ALL) }
    var showStatistics by remember { mutableStateOf(true) }
    var showBulkActions by remember { mutableStateOf(false) }
    var showFilterMenu by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Room Management",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showFilterMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint = Color.Black
                        )
                    }

                    DropdownMenu(
                        expanded = showFilterMenu,
                        onDismissRequest = { showFilterMenu = false }
                    ) {
                        RoomFilter.values().forEach { filter ->
                            DropdownMenuItem(
                                text = { Text(filter.name.replace('_', ' ')) },
                                onClick = {
                                    selectedFilter = filter
                                    showFilterMenu = false
                                }
                            )
                        }
                    }

                    IconButton(onClick = { showStatistics = !showStatistics }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Statistics",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBulkActions = !showBulkActions },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Bulk Actions"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search Bar
            item(key = "search_bar") {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it }
                )
            }

            // Statistics Card
            if (showStatistics) {
                item(key = "statistics") {
                    StatisticsCard(statistics = statistics)
                }
            }

            // Bulk Actions
            if (showBulkActions) {
                item(key = "bulk_actions") {
                    BulkActionsCard(
                        onOccupyAll = {
                            viewModel.occupyAllBeds()
                            scope.launch {
                                snackbarHostState.showSnackbar("All beds marked as occupied")
                            }
                        },
                        onFreeAll = {
                            viewModel.freeAllBeds()
                            scope.launch {
                                snackbarHostState.showSnackbar("All beds marked as available")
                            }
                        },
                        onResetAll = {
                            viewModel.resetAllRoomCapacities()
                            scope.launch {
                                snackbarHostState.showSnackbar("All room capacities reset")
                            }
                        }
                    )
                }
            }

            // Instructions Card
            item(key = "instructions") {
                InstructionCard()
            }

            // Legend
            item(key = "legend") {
                EnhancedLegendRow()
            }

            // Floor sections with filtered rooms
            items(
                items = floors,
                key = { floorKey -> floorKey }
            ) { floorKey ->
                val floorRooms = remember(roomsState, floorKey) {
                    viewModel.getRoomsForFloor(floorKey)
                }

                val filteredRooms = remember(floorRooms, searchQuery, selectedFilter) {
                    filterRooms(floorRooms, searchQuery, selectedFilter)
                }

                if (filteredRooms.isNotEmpty()) {
                    FloorSection(
                        floorKey = floorKey,
                        rooms = filteredRooms,
                        onIncreaseBeds = { roomNumber ->
                            viewModel.increaseBedCapacity(floorKey, roomNumber)
                        },
                        onDecreaseBeds = { roomNumber ->
                            viewModel.decreaseBedCapacity(floorKey, roomNumber)
                        },
                        onBedClick = { roomNumber, bedNumber ->
                            viewModel.toggleBedOccupancy(floorKey, roomNumber, bedNumber)
                        },
                        onOccupyAll = { roomNumber ->
                            viewModel.occupyAllBedsInRoom(floorKey, roomNumber)
                        },
                        onFreeAll = { roomNumber ->
                            viewModel.freeAllBedsInRoom(floorKey, roomNumber)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search rooms...") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search")
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
fun StatisticsCard(statistics: com.hlopg.presentation.admin.viewmodel.RoomStatistics) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "📊 Overview",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    label = "Total Rooms",
                    value = statistics.totalRooms.toString(),
                    color = Color(0xFF2196F3)
                )
                StatItem(
                    label = "Total Beds",
                    value = statistics.totalBeds.toString(),
                    color = Color(0xFF9C27B0)
                )
                StatItem(
                    label = "Occupied",
                    value = statistics.occupiedBeds.toString(),
                    color = Color(0xFFF44336)
                )
                StatItem(
                    label = "Available",
                    value = statistics.availableBeds.toString(),
                    color = Color(0xFF4CAF50)
                )
            }

            // Occupancy Progress Bar
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Occupancy Rate",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                    Text(
                        text = "${String.format("%.1f", statistics.occupancyPercentage)}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                val animatedProgress by animateFloatAsState(
                    targetValue = statistics.occupancyPercentage / 100f,
                    label = "progress"
                )

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = when {
                        statistics.occupancyPercentage < 50 -> Color(0xFF4CAF50)
                        statistics.occupancyPercentage < 80 -> Color(0xFFFF9800)
                        else -> Color(0xFFF44336)
                    },
                )
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BulkActionsCard(
    onOccupyAll: () -> Unit,
    onFreeAll: () -> Unit,
    onResetAll: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Bulk Actions",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onOccupyAll,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFF44336)
                    )
                ) {
                    Text("Occupy All", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = onFreeAll,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text("Free All", fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = onResetAll,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF9E9E9E)
                    )
                ) {
                    Text("Reset All", fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun InstructionCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        )
    ) {
        Text(
            text = "Use +/- buttons to adjust bed capacity. Tap on bed icons to toggle occupancy status (Available ↔ Occupied). Use room actions for quick operations.",
            modifier = Modifier.padding(12.dp),
            fontSize = 13.sp,
            color = Color.Black,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun EnhancedLegendRow() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LegendItem(color = BedColors.Available, label = "Available")
            LegendItem(color = BedColors.Occupied, label = "Occupied")
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, shape = CircleShape)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Composable
fun FloorSection(
    floorKey: String,
    rooms: List<RoomData>,
    onIncreaseBeds: (String) -> Unit,
    onDecreaseBeds: (String) -> Unit,
    onBedClick: (String, Int) -> Unit,
    onOccupyAll: (String) -> Unit,
    onFreeAll: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Floor Header
        val floorStats = remember(rooms) {
            val total = rooms.sumOf { it.beds.size }
            val occupied = rooms.sumOf { it.occupiedBeds }
            "$occupied/$total beds"
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = floorKey,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = floorStats,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.9f)
            )
        }

        // Rooms grid - using Column instead of nested LazyColumn
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            rooms.chunked(2).forEach { roomPair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    roomPair.forEach { room ->
                        // Use key to help Compose track individual rooms
                        key(room.roomNumber) {
                            EnhancedRoomCard(
                                room = room,
                                onIncreaseBeds = { onIncreaseBeds(room.roomNumber) },
                                onDecreaseBeds = { onDecreaseBeds(room.roomNumber) },
                                onBedClick = { bedNumber ->
                                    onBedClick(room.roomNumber, bedNumber)
                                },
                                onOccupyAll = { onOccupyAll(room.roomNumber) },
                                onFreeAll = { onFreeAll(room.roomNumber) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    // Fill remaining space if odd number of rooms
                    if (roomPair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedRoomCard(
    room: RoomData,
    onIncreaseBeds: () -> Unit,
    onDecreaseBeds: () -> Unit,
    onBedClick: (Int) -> Unit,
    onOccupyAll: () -> Unit,
    onFreeAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showActions by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = when {
            room.isFull -> BedColors.Occupied
            room.isEmpty -> BedColors.Available
            else -> Color(0xFFFF9800)
        },
        label = "border"
    )

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Room Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Room ${room.roomNumber}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                IconButton(
                    onClick = { showActions = !showActions },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Actions",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Capacity Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDecreaseBeds,
                    enabled = room.sharingCapacity > 0,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease beds",
                        tint = if (room.sharingCapacity > 0) Color(0xFFF44336) else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${room.sharingCapacity}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "beds",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }

                IconButton(
                    onClick = onIncreaseBeds,
                    enabled = room.sharingCapacity < 10,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase beds",
                        tint = if (room.sharingCapacity < 10) Color(0xFF4CAF50) else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Bed Grid with Icons
            if (room.beds.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            color = borderColor.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        room.beds.chunked(3).forEach { bedRow ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                bedRow.forEach { bed ->
                                    key(bed.bedNumber) {
                                        BedIconButton(
                                            bedNumber = bed.bedNumber,
                                            isOccupied = bed.isOccupied,
                                            onClick = { onBedClick(bed.bedNumber) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Bed Status
                Text(
                    text = "${room.occupiedBeds}/${room.beds.size} occupied",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            } else {
                Text(
                    text = "No beds configured",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }

            // Quick Actions
            AnimatedVisibility(
                visible = showActions && room.beds.isNotEmpty(),
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TextButton(
                        onClick = onOccupyAll,
                        modifier = Modifier.weight(1f),
                        enabled = !room.isFull
                    ) {
                        Text("Fill", fontSize = 10.sp)
                    }

                    TextButton(
                        onClick = onFreeAll,
                        modifier = Modifier.weight(1f),
                        enabled = !room.isEmpty
                    ) {
                        Text("Clear", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun BedIconButton(
    bedNumber: Int,
    isOccupied: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isOccupied) 0.95f else 1f,
        label = "scale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isOccupied) BedColors.Occupied else BedColors.Available,
        label = "color"
    )

    Box(
        modifier = Modifier
            .size(40.dp)
            .scale(scale)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor.copy(alpha = 0.2f))
            .border(
                width = 2.dp,
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🛏️",
                fontSize = 18.sp
            )
            Text(
                text = bedNumber.toString(),
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = backgroundColor
            )
        }
    }
}

// Filter function
fun filterRooms(
    rooms: List<RoomData>,
    searchQuery: String,
    filter: RoomFilter
): List<RoomData> {
    var filtered = rooms

    // Apply search filter
    if (searchQuery.isNotBlank()) {
        filtered = filtered.filter {
            it.roomNumber.contains(searchQuery, ignoreCase = true)
        }
    }

    // Apply status filter
    filtered = when (filter) {
        RoomFilter.ALL -> filtered
        RoomFilter.AVAILABLE -> filtered.filter { !it.isFull && it.beds.isNotEmpty() }
        RoomFilter.FULL -> filtered.filter { it.isFull }
        RoomFilter.PARTIAL -> filtered.filter {
            it.beds.isNotEmpty() && !it.isFull && !it.isEmpty
        }
        RoomFilter.EMPTY -> filtered.filter { it.isEmpty || it.beds.isEmpty() }
    }

    return filtered
}