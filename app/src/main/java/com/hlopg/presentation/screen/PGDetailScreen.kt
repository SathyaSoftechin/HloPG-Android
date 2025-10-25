//package com.hlopg.presentation.screen
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PGDetailScreen(
//    pgId: String,
//    viewModel: PGDetailViewModel,
//    onBackClick: () -> Unit,
//    onBookClick: () -> Unit
//) {
//    val pgDetail by viewModel.pgDetail.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//    val error by viewModel.error.collectAsState()
//
//    LaunchedEffect(pgId) {
//        viewModel.loadPGDetail(pgId)
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { },
//                navigationIcon = {
//                    IconButton(onClick = onBackClick) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowBack,
//                            contentDescription = "Back",
//                            tint = Color.Black
//                        )
//                    }
//                },
//                actions = {
//                    IconButton(onClick = { /* Favorite action */ }) {
//                        Icon(
//                            imageVector = Icons.Default.FavoriteBorder,
//                            contentDescription = "Favorite",
//                            tint = Color.Black
//                        )
//                    }
//                    IconButton(onClick = { /* Share action */ }) {
//                        Icon(
//                            imageVector = Icons.Default.Share,
//                            contentDescription = "Share",
//                            tint = Color.Black
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color.White
//                )
//            )
//        },
//        bottomBar = {
//            Button(
//                onClick = onBookClick,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//                    .height(56.dp),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF7C4DFF)
//                )
//            ) {
//                Text(
//                    text = "Book Now",
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.SemiBold
//                )
//            }
//        }
//    ) { paddingValues ->
//        when {
//            isLoading -> {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator(color = Color(0xFF7C4DFF))
//                }
//            }
//            error != null -> {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = error ?: "Unknown error",
//                        color = Color.Red,
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
//            pgDetail != null -> {
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues)
//                ) {
//                    item {
//                        // Main Image
//                        Image(
//                            painter = rememberAsyncImagePainter(pgDetail!!.mainImage),
//                            contentDescription = "PG Image",
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(240.dp),
//                            contentScale = ContentScale.Crop
//                        )
//
//                        // Image Indicators
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 12.dp),
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//                            repeat(5) { index ->
//                                Box(
//                                    modifier = Modifier
//                                        .size(8.dp)
//                                        .padding(horizontal = 2.dp)
//                                        .clip(CircleShape)
//                                        .background(
//                                            if (index == 0) Color(0xFF7C4DFF)
//                                            else Color.LightGray
//                                        )
//                                )
//                            }
//                        }
//                    }
//
//                    item {
//                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
//                            // Location
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                                modifier = Modifier.padding(bottom = 8.dp)
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.LocationOn,
//                                    contentDescription = null,
//                                    tint = Color(0xFF7C4DFF),
//                                    modifier = Modifier.size(20.dp)
//                                )
//                                Spacer(modifier = Modifier.width(4.dp))
//                                Text(
//                                    text = pgDetail!!.address,
//                                    fontSize = 14.sp,
//                                    color = Color.Gray
//                                )
//                            }
//
//                            // Description
//                            Text(
//                                text = pgDetail!!.description,
//                                fontSize = 14.sp,
//                                color = Color.Black.copy(alpha = 0.7f),
//                                lineHeight = 20.sp,
//                                modifier = Modifier.padding(bottom = 16.dp)
//                            )
//
//                            Divider(color = Color.LightGray.copy(alpha = 0.5f))
//                        }
//                    }
//
//                    item {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text(
//                                text = "Select Sharing Type",
//                                fontSize = 16.sp,
//                                fontWeight = FontWeight.SemiBold,
//                                modifier = Modifier.padding(bottom = 12.dp)
//                            )
//
//                            LazyRow(
//                                horizontalArrangement = Arrangement.spacedBy(12.dp)
//                            ) {
//                                items(pgDetail!!.sharingTypes) { type ->
//                                    SharingTypeChip(type)
//                                }
//                            }
//
//                            Divider(
//                                color = Color.LightGray.copy(alpha = 0.5f),
//                                modifier = Modifier.padding(top = 16.dp)
//                            )
//                        }
//                    }
//
//                    item {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text(
//                                text = "Amenities",
//                                fontSize = 16.sp,
//                                fontWeight = FontWeight.SemiBold,
//                                modifier = Modifier.padding(bottom = 12.dp)
//                            )
//
//                            AmenitiesGrid(pgDetail!!.amenities)
//
//                            Divider(
//                                color = Color.LightGray.copy(alpha = 0.5f),
//                                modifier = Modifier.padding(top = 16.dp)
//                            )
//                        }
//                    }
//
//                    item {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text(
//                                text = "PG Rules",
//                                fontSize = 16.sp,
//                                fontWeight = FontWeight.SemiBold,
//                                modifier = Modifier.padding(bottom = 12.dp)
//                            )
//
//                            pgDetail!!.rules.forEach { rule ->
//                                RuleItem(rule)
//                            }
//
//                            Divider(
//                                color = Color.LightGray.copy(alpha = 0.5f),
//                                modifier = Modifier.padding(top = 16.dp)
//                            )
//                        }
//                    }
//
//                    item {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text(
//                                text = "Food Menu",
//                                fontSize = 16.sp,
//                                fontWeight = FontWeight.SemiBold,
//                                modifier = Modifier.padding(bottom = 12.dp)
//                            )
//
//                            FoodMenuTable(pgDetail!!.foodMenu)
//                        }
//                    }
//
//                    item {
//                        Spacer(modifier = Modifier.height(80.dp))
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun SharingTypeChip(type: String) {
//    Box(
//        modifier = Modifier
//            .size(48.dp)
//            .clip(RoundedCornerShape(12.dp))
//            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
//            .background(Color.White),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = type,
//            fontSize = 12.sp,
//            fontWeight = FontWeight.Medium
//        )
//    }
//}
//
//@Composable
//fun AmenitiesGrid(amenities: List<Amenity>) {
//    Column {
//        amenities.chunked(4).forEach { row ->
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                row.forEach { amenity ->
//                    AmenityItem(amenity)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun AmenityItem(amenity: Amenity) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.width(70.dp)
//    ) {
//        Icon(
//            imageVector = getAmenityIcon(amenity.name),
//            contentDescription = amenity.name,
//            tint = Color(0xFF7C4DFF),
//            modifier = Modifier.size(32.dp)
//        )
//        Spacer(modifier = Modifier.height(4.dp))
//        Text(
//            text = amenity.name,
//            fontSize = 11.sp,
//            color = Color.Black.copy(alpha = 0.7f),
//            textAlign = TextAlign.Center
//        )
//    }
//}
//
//@Composable
//fun RuleItem(rule: String) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(
//            imageVector = Icons.Default.Circle,
//            contentDescription = null,
//            tint = Color(0xFF7C4DFF),
//            modifier = Modifier.size(6.dp)
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//        Text(
//            text = rule,
//            fontSize = 13.sp,
//            color = Color.Black.copy(alpha = 0.7f)
//        )
//    }
//}
//
//@Composable
//fun FoodMenuTable(foodMenu: List<FoodMenuItem>) {
//    Column {
//        foodMenu.forEach { item ->
//            FoodMenuRow(item)
//        }
//    }
//}
//
//@Composable
//fun FoodMenuRow(item: FoodMenuItem) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 6.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Box(
//            modifier = Modifier
//                .width(60.dp)
//                .height(32.dp)
//                .clip(RoundedCornerShape(8.dp))
//                .background(getDayColor(item.day)),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = item.day,
//                fontSize = 11.sp,
//                fontWeight = FontWeight.SemiBold,
//                color = Color.White
//            )
//        }
//
//        MealBox(item.breakfast, Color(0xFFE8F5E9))
//        MealBox(item.lunch, Color(0xFFFFF3E0))
//        MealBox(item.dinner, Color(0xFFFCE4EC))
//    }
//}
//
//@Composable
//fun MealBox(meal: String, backgroundColor: Color) {
//    Box(
//        modifier = Modifier
//            .width(80.dp)
//            .height(32.dp)
//            .clip(RoundedCornerShape(8.dp))
//            .background(backgroundColor),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = meal,
//            fontSize = 10.sp,
//            color = Color.Black.copy(alpha = 0.8f),
//            textAlign = TextAlign.Center
//        )
//    }
//}
//
//fun getAmenityIcon(name: String): ImageVector {
//    return when (name.lowercase()) {
//        "free wifi" -> Icons.Default.Wifi
//        "fan" -> Icons.Default.Air
//        "bed" -> Icons.Default.Bed
//        "washing" -> Icons.Default.LocalLaundryService
//        "lights" -> Icons.Default.Lightbulb
//        "cupboard" -> Icons.Default.Weekend
//        "geyser" -> Icons.Default.WaterDrop
//        "fridge" -> Icons.Default.Kitchen
//        "tv" -> Icons.Default.Tv
//        "balcony" -> Icons.Default.Balcony
//        "cctv" -> Icons.Default.Videocam
//        "parking" -> Icons.Default.LocalParking
//        "gym" -> Icons.Default.FitnessCenter
//        "swimming" -> Icons.Default.Pool
//        "lift" -> Icons.Default.Elevator
//        "games" -> Icons.Default.SportsEsports
//        "call cons" -> Icons.Default.Call
//        else -> Icons.Default.CheckCircle
//    }
//}
//
//fun getDayColor(day: String): Color {
//    return when (day.uppercase()) {
//        "MON-D" -> Color(0xFF9C27B0)
//        "TUE-D" -> Color(0xFF9C27B0)
//        "WED-D" -> Color(0xFF9C27B0)
//        "THU-D" -> Color(0xFF9C27B0)
//        "FRI-D" -> Color(0xFF9C27B0)
//        "SAT-D" -> Color(0xFF9C27B0)
//        "SUN-D" -> Color(0xFF9C27B0)
//        else -> Color(0xFF7C4DFF)
//    }
//}
//
//// Data Models
//data class PGDetail(
//    val id: String,
//    val name: String,
//    val address: String,
//    val mainImage: String,
//    val images: List<String>,
//    val description: String,
//    val sharingTypes: List<String>,
//    val amenities: List<Amenity>,
//    val rules: List<String>,
//    val foodMenu: List<FoodMenuItem>
//)
//
//data class Amenity(
//    val name: String,
//    val available: Boolean = true
//)
//
//data class FoodMenuItem(
//    val day: String,
//    val breakfast: String,
//    val lunch: String,
//    val dinner: String
//)
//
//@Preview
//@Composable
//fun PGDetailScreenPreview() {
//    PGDetailScreen(
//        pgId = 1,
//        viewModel = TODO(),
//        onBackClick = TODO(),
//        onBookClick = TODO()
//    )
//}