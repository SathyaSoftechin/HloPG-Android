package com.hlopg.presentation.admin.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hlopg.presentation.admin.viewmodel.RoomData
import com.hlopg.presentation.admin.viewmodel.RoomManagementViewModel

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
    val floors = viewModel.getAllFloors()
    var showValidationError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Rooms",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
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
            // Instructions Card
            item {
                InstructionCard()
            }

            // Legend
            item {
                LegendRow()
            }

            // Floor sections
            items(floors) { floorKey ->
                FloorSection(
                    floorKey = floorKey,
                    rooms = viewModel.getRoomsForFloor(floorKey),
                    onSharingCapacityChange = { roomNumber, capacity ->
                        viewModel.updateRoomSharingCapacity(floorKey, roomNumber, capacity)
                    },
                    onBedClick = { roomNumber, bedNumber ->
                        viewModel.toggleBedOccupancy(floorKey, roomNumber, bedNumber)
                    }
                )
            }

            // Show validation error if any
            showValidationError?.let { error ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFEBEE)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = error,
                                color = Color(0xFFC62828),
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = { showValidationError = null }) {
                                Text("OK", color = Color(0xFFC62828))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InstructionCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        )
    ) {
        Text(
            text = "Enter the sharing capacity for each room. You can update the room occupancy by clicking on the color box. This allows you to easily view the current room status and also hold specific rooms by changing their color.",
            modifier = Modifier.padding(12.dp),
            fontSize = 13.sp,
            color = Color.Black,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun LegendRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        LegendItem(
            color = Color(0xFF4CAF50),
            label = "Available"
        )
        LegendItem(
            color = Color(0xFFF44336),
            label = "Filled"
        )
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
                .size(14.dp)
                .background(color, shape = RoundedCornerShape(50))
        )
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Composable
fun FloorSection(
    floorKey: String,
    rooms: List<RoomData>,
    onSharingCapacityChange: (String, Int) -> Unit,
    onBedClick: (String, Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Floor Header with rounded corners
        Text(
            text = floorKey,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )

        // Rooms in this floor
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            rooms.forEach { room ->
                RoomCard(
                    room = room,
                    onSharingCapacityChange = { capacity ->
                        onSharingCapacityChange(room.roomNumber, capacity)
                    },
                    onBedClick = { bedNumber ->
                        onBedClick(room.roomNumber, bedNumber)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun RoomCard(
    room: RoomData,
    onSharingCapacityChange: (Int) -> Unit,
    onBedClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var sharingInput by remember { mutableStateOf(if (room.sharingCapacity > 0) room.sharingCapacity.toString() else "") }

    Column(
        modifier = modifier.padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Room Number
        Text(
            text = room.roomNumber,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = Color.Black
        )

        // Bed boxes in a grid (max 3 per row)
        if (room.beds.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                room.beds.chunked(3).forEach { bedRow ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        bedRow.forEach { bed ->
                            BedBox(
                                isOccupied = bed.isOccupied,
                                onClick = { onBedClick(bed.bedNumber) }
                            )
                        }
                    }
                }
            }
        } else {
            // Empty space when no beds
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Sharing Capacity Input with custom styling
        OutlinedTextField(
            value = sharingInput,
            onValueChange = { newValue ->
                sharingInput = newValue
                newValue.toIntOrNull()?.let { capacity ->
                    if (capacity in 1..10) {
                        onSharingCapacityChange(capacity)
                    }
                }
            },
            placeholder = {
                Text(
                    "Enter Sharing",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            },
            modifier = Modifier
                .width(75.dp)
                .height(40.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 11.sp,
                color = Color.Black
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color(0xFFBDBDBD),
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            shape = RoundedCornerShape(4.dp)
        )
    }
}

@Composable
fun BedBox(
    isOccupied: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .border(
                width = 1.5.dp,
                color = if (isOccupied) Color(0xFFF44336) else Color(0xFF4CAF50),
                shape = RoundedCornerShape(3.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(3.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Line indicator (horizontal line like in the image)
        Box(
            modifier = Modifier
                .width(12.dp)
                .height(2.dp)
                .background(
                    color = if (isOccupied) Color(0xFFF44336) else Color(0xFF4CAF50),
                    shape = RoundedCornerShape(1.dp)
                )
        )
    }
}