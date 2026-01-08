package com.hlopg.presentation.admin.screen

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Elevator
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.hlopg.presentation.admin.viewmodel.UploadPGViewModel
import com.hlopg.presentation.admin.viewmodel.UploadState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadPGScreen(
    navController: NavHostController,
    viewModel: UploadPGViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uploadState by viewModel.uploadState.collectAsStateWithLifecycle()


    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.onImageSelected(it)
        }
    }

    // Video picker launcher
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.onVideoSelected(it)
        }
    }

    LaunchedEffect(uploadState) {
        if (uploadState is UploadState.Success) {
            // Navigate back after successful upload
            navController.popBackStack()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
        ) {
            // Top Bar
            TopAppBar(
                title = {
                    Text(
                        text = "Upload PG Details",
                        fontSize = 18.sp,
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // PG Name
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "PG Name",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        OutlinedTextField(
                            value = uiState.pgName,
                            onValueChange = { viewModel.updatePGName(it) },
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF6C63FF),
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            shape = RoundedCornerShape(5.dp),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                        )
                    }
                }

                // PG Information
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "PG Information",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        OutlinedTextField(
                            value = uiState.pgInformation,
                            onValueChange = { viewModel.updatePGInformation(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF6C63FF),
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            shape = RoundedCornerShape(5.dp),
                            maxLines = 4,
                            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                        )
                    }
                }

                // PG Type
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "PG Type",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            listOf("Boys", "Women", "Co-Living").forEach { type ->
                                PGTypeChip(
                                    type = type,
                                    isSelected = uiState.pgType == type,
                                    onClick = { viewModel.updatePGType(type) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                // PG Location
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Enter PG Location",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Enter City",
                                fontSize = 13.sp,
                                color = Color(0xFF666666)
                            )
                            OutlinedTextField(
                                value = uiState.city,
                                onValueChange = { viewModel.updateCity(it) },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF6C63FF),
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black
                                ),
                                shape = RoundedCornerShape(5.dp),
                                singleLine = true,
                                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Enter Area",
                                fontSize = 13.sp,
                                color = Color(0xFF666666)
                            )
                            OutlinedTextField(
                                value = uiState.area,
                                onValueChange = { viewModel.updateArea(it) },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF6C63FF),
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black
                                ),
                                shape = RoundedCornerShape(5.dp),
                                singleLine = true,
                                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                            )
                        }
                    }
                }

                // PG Images
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "PG Images",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                ImageUploadCard(
                                    label = "Room\nImage 1",
                                    mediaUri = uiState.roomImage1,
                                    onClick = {
                                        viewModel.setCurrentImageType("room1")
                                        imagePickerLauncher.launch("image/*")
                                    },
                                    modifier = Modifier.weight(1f)
                                )

                                ImageUploadCard(
                                    label = "Room\nImage 2",
                                    mediaUri = uiState.roomImage2,
                                    onClick = {
                                        viewModel.setCurrentImageType("room2")
                                        imagePickerLauncher.launch("image/*")
                                    },
                                    modifier = Modifier.weight(1f)
                                )

                                ImageUploadCard(
                                    label = "Washroom\nImage",
                                    mediaUri = uiState.washRoomImage,
                                    onClick = {
                                        viewModel.setCurrentImageType("washroom")
                                        imagePickerLauncher.launch("image/*")
                                    },
                                    modifier = Modifier.weight(1f)
                                )

                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                ImageUploadCard(
                                    label = "Parking\nImage",
                                    mediaUri = uiState.parkingImage,
                                    onClick = {
                                        viewModel.setCurrentImageType("parking")
                                        imagePickerLauncher.launch("image/*")
                                    },
                                    modifier = Modifier.weight(1f)
                                )

                                ImageUploadCard(
                                    label = "Video\nof Room",
                                    mediaUri = uiState.videoUrl,
                                    isVideo = true,
                                    onClick = {
                                        videoPickerLauncher.launch("video/*")
                                    },
                                    modifier = Modifier.weight(1f)
                                )

                                ImageUploadCard(
                                    label = "+",
                                    mediaUri = null,
                                    isAddMore = true,
                                    onClick = {
                                        viewModel.setCurrentImageType("extra")
                                        imagePickerLauncher.launch("image/*")
                                    },
                                    modifier = Modifier.weight(1f)
                                )

                            }
                        }
                    }
                }

                // Food Menu
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Food Menu",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )

                        FoodMenuTable(
                            foodMenu = uiState.foodMenu,
                            onMenuUpdate = { day, meal, value ->
                                viewModel.updateFoodMenu(day, meal, value)
                            }
                        )
                    }
                }

                // Amenities
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Amenities",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                AmenityItem(
                                    icon = Icons.Default.Wifi,
                                    label = "Free Wifi",
                                    isSelected = uiState.amenities["Free Wifi"] == true,
                                    onClick = { viewModel.toggleAmenity("Free Wifi") },
                                    modifier = Modifier.weight(1f)
                                )
                                AmenityItem(
                                    icon = Icons.Default.Air,
                                    label = "Fan",
                                    isSelected = uiState.amenities["Fan"] == true,
                                    onClick = { viewModel.toggleAmenity("Fan") },
                                    modifier = Modifier.weight(1f)
                                )
                                AmenityItem(
                                    icon = Icons.Default.Bed,
                                    label = "Bed",
                                    isSelected = uiState.amenities["Bed"] == true,
                                    onClick = { viewModel.toggleAmenity("Bed") },
                                    modifier = Modifier.weight(1f)
                                )
                                AmenityItem(
                                    icon = Icons.Default.LocalLaundryService,
                                    label = "washing",
                                    isSelected = uiState.amenities["washing"] == true,
                                    onClick = { viewModel.toggleAmenity("washing") },
                                    modifier = Modifier.weight(1f)
                                )
                                AmenityItem(
                                    icon = Icons.Default.Lightbulb,
                                    label = "Lights",
                                    isSelected = uiState.amenities["Lights"] == true,
                                    onClick = { viewModel.toggleAmenity("Lights") },
                                    modifier = Modifier.weight(1f)
                                )
                                AmenityItem(
                                    icon = Icons.Default.Weekend,
                                    label = "coboard",
                                    isSelected = uiState.amenities["coboard"] == true,
                                    onClick = { viewModel.toggleAmenity("coboard") },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                AmenityItem(
                                    icon = Icons.Default.WaterDrop,
                                    label = "Geyser",
                                    isSelected = uiState.amenities["Geyser"] == true,
                                    onClick = { viewModel.toggleAmenity("Geyser") },
                                    modifier = Modifier.weight(1f)
                                )
                                AmenityItem(
                                    icon = Icons.Default.Water,
                                    label = "water",
                                    isSelected = uiState.amenities["water"] == true,
                                    onClick = { viewModel.toggleAmenity("water") },
                                    modifier = Modifier.weight(1f)
                                )
                                AmenityItem(
                                    icon = Icons.Default.Kitchen,
                                    label = "Fridge",
                                    isSelected = uiState.amenities["Fridge"] == true,
                                    onClick = { viewModel.toggleAmenity("Fridge") },
                                    modifier = Modifier.weight(1f)
                                )
                                AmenityItem(
                                    icon = Icons.Default.FitnessCenter,
                                    label = "Gym",
                                    isSelected = uiState.amenities["Gym"] == true,
                                    onClick = { viewModel.toggleAmenity("Gym") },
                                    modifier = Modifier.weight(1f)
                                )
                                AmenityItem(
                                    icon = Icons.Default.Tv,
                                    label = "TV",
                                    isSelected = uiState.amenities["TV"] == true,
                                    onClick = { viewModel.toggleAmenity("TV") },
                                    modifier = Modifier.weight(1f)
                                )
                                AmenityItem(
                                    icon = Icons.Default.AcUnit,
                                    label = "Ac",
                                    isSelected = uiState.amenities["Ac"] == true,
                                    onClick = { viewModel.toggleAmenity("Ac") },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                AmenityItem(
                                    icon = Icons.Default.LocalParking,
                                    label = "Parking",
                                    isSelected = uiState.amenities["Parking"] == true,
                                    onClick = { viewModel.toggleAmenity("Parking") },
                                    modifier = Modifier.weight(1f)
                                )
                                AmenityItem(
                                    icon = Icons.Default.Restaurant,
                                    label = "Food",
                                    isSelected = uiState.amenities["Food"] == true,
                                    onClick = { viewModel.toggleAmenity("Food") },
                                    modifier = Modifier.weight(1f)
                                )
                                AmenityItem(
                                    icon = Icons.Default.Elevator,
                                    label = "Lift",
                                    isSelected = uiState.amenities["Lift"] == true,
                                    onClick = { viewModel.toggleAmenity("Lift") },
                                    modifier = Modifier.weight(1f)
                                )
                                AmenityItem(
                                    icon = Icons.Default.Videocam,
                                    label = "Cam's",
                                    isSelected = uiState.amenities["Cam's"] == true,
                                    onClick = { viewModel.toggleAmenity("Cam's") },
                                    modifier = Modifier.weight(1f)
                                )
                                AmenityItem(
                                    icon = Icons.Default.SoupKitchen,
                                    label = "Self Cook...",
                                    isSelected = uiState.amenities["Self Cook..."] == true,
                                    onClick = { viewModel.toggleAmenity("Self Cook...") },
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                // Floors and Rooms
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Select PG Floors and Rooms",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(5.dp))
                                .background(Color(0xFFF0F4FF))
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            DropdownField(
                                label = "1. Number Floors In your building",
                                value = uiState.numberOfFloors.toString(),
                                options = (1..10).map { it.toString() },
                                onValueChange = { viewModel.updateNumberOfFloors(it.toIntOrNull() ?: 1) }
                            )

                            DropdownField(
                                label = "2. Number Rooms  In a Floor",
                                value = uiState.roomsPerFloor.toString(),
                                options = (1..10).map { it.toString() },
                                onValueChange = { viewModel.updateRoomsPerFloor(it.toIntOrNull() ?: 1) }
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "3. Enter Starting Room number",
                                    fontSize = 13.sp,
                                    color = Color.Black
                                )
                                OutlinedTextField(
                                    value = uiState.startingRoomNumber,
                                    onValueChange = { viewModel.updateStartingRoomNumber(it) },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF6C63FF),
                                        unfocusedBorderColor = Color(0xFFE0E0E0),
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        focusedTextColor = Color.Black,
                                        unfocusedTextColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(5.dp),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                                )
                            }
                        }
                    }
                }

                // Sharing Prices
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Enter Sharing price",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            listOf("2 Sharing", "3 Sharing", "4 Sharing", "5 Sharing").forEach { sharing ->
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = sharing,
                                        fontSize = 11.sp,
                                        color = Color.Black,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    OutlinedTextField(
                                        value = uiState.sharingPrices[sharing] ?: "",
                                        onValueChange = { viewModel.updateSharingPrice(sharing, it) },
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFF6C63FF),
                                            unfocusedBorderColor = Color(0xFFE0E0E0),
                                            focusedContainerColor = Color(0xFFE8E8E8),
                                            unfocusedContainerColor = Color(0xFFE8E8E8),
                                            focusedTextColor = Color.Black,
                                            unfocusedTextColor = Color.Black
                                        ),
                                        shape = RoundedCornerShape(5.dp),
                                        placeholder = {
                                            Text(
                                                text = " ",
                                                fontSize = 10.sp,
                                                color = Color(0xFF999999)
                                            )
                                        },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        textStyle = LocalTextStyle.current.copy(fontSize = 12.sp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Advance Amount
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Enter Advance Amount",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        OutlinedTextField(
                            value = uiState.advanceAmount,
                            onValueChange = { viewModel.updateAdvanceAmount(it) },
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF6C63FF),
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            shape = RoundedCornerShape(5.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }



// Upload Button
        Button(
            onClick = { viewModel.uploadPGDetails() },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .height(50.dp),
            shape = RoundedCornerShape(5.dp),
            enabled = uploadState !is UploadState.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6C63FF),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFF6C63FF)
            )
        ) {
            AnimatedContent(
                targetState = uploadState is UploadState.Loading,
                label = "upload_button_animation"
            ) { isLoading ->
                if (isLoading) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "Uploading…",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                } else {
                    Text(
                        text = "Upload Now",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }


        // Error Snackbar
        if (uploadState is UploadState.Error) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp, start = 16.dp, end = 16.dp),
                containerColor = Color(0xFFFF5252)
            ) {
                Text((uploadState as UploadState.Error).message)
            }
        }
    }
}

@Composable
fun PGTypeChip(
    type: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(42.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(if (isSelected) Color(0xFF6C63FF) else Color(0xFFE8E8E8))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = type,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF666666)
        )
    }
}

@Composable
fun ImageUploadCard(
    label: String,
    mediaUri: Uri?,
    isVideo: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isAddMore: Boolean = false
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(5.dp))
            .background(Color(0xFFE8E8E8))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {

        when {
            mediaUri != null && !isAddMore -> {
                if (isVideo) {
                    VideoThumbnail(uri = mediaUri)
                } else {
                    ImagePreview(uri = mediaUri)
                }

            }

            else -> {
                Text(
                    text = label,
                    fontSize = if (isAddMore) 28.sp else 12.sp,
                    fontWeight = if (isAddMore) FontWeight.Light else FontWeight.Normal,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center
                )
            }
        }

        if (mediaUri != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

    }
}

@Composable
fun ImagePreview(uri: Uri) {
    AsyncImage(
        model = uri,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun VideoThumbnail(uri: Uri) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(uri) {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)
        bitmap = retriever.frameAtTime
        retriever.release()
    }

    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}



@Composable
fun FoodMenuTable(
    foodMenu: Map<String, Map<String, String>>,
    onMenuUpdate: (String, String, String) -> Unit
) {
    val days = listOf("MON-D", "TUES-D", "WED-D", "THUR-D", "FRI-D", "SAT-D", "SUN-D")
    val meals = listOf("breakfast", "lunch", "dinner")
    val mealHeaders = listOf("BREAK FAST", "LUNCH", "DINNER")
    val mealColors = listOf(
        Color(0xFF4CAF50),
        Color(0xFFFF7043),
        Color(0xFFEC407A)
    )
    val cellColors = listOf(
        Color(0xFFD4F5E3),
        Color(0xFFFFDDD4),
        Color(0xFFFFD4F0)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(5.dp))
            .background(Color.White)
            .padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Header Row
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(0.65f)
                    .height(36.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFD4C5F9)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "DAY",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            mealHeaders.forEachIndexed { index, meal ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(mealColors[index].copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = meal,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Data Rows
        days.forEach { day ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.height(IntrinsicSize.Min)
            ) {
                Box(
                    modifier = Modifier
                        .weight(0.65f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFE8DCFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }

                meals.forEachIndexed { index, meal ->
                    val currentValue = foodMenu[day]?.get(meal) ?: ""

                    OutlinedTextField(
                        value = currentValue,
                        onValueChange = { onMenuUpdate(day, meal, it) },
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF6C63FF),
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = cellColors[index],
                            unfocusedContainerColor = cellColors[index],
                            cursorColor = Color(0xFF6C63FF),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(4.dp),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 9.sp,
                            lineHeight = 11.sp,
                            color = Color.Black
                        ),
                        maxLines = 3,
                        singleLine = false
                    )
                }
            }
        }
    }
}

@Composable
fun AmenityItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) Color(0xFF6C63FF).copy(alpha = 0.1f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Color(0xFF6C63FF) else Color(0xFF999999),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            fontSize = 9.sp,
            color = if (isSelected) Color(0xFF6C63FF) else Color(0xFF666666),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color.Black
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6C63FF),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                shape = RoundedCornerShape(5.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
            )

            ExposedDropdownMenu(
                expanded = expanded, 
                onDismissRequest = { expanded = false },
                containerColor = Color.White
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, fontSize = 14.sp, color = Color.Black.copy(0.6f)) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}