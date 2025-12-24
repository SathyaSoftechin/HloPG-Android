package com.hlopg.presentation.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Elevator
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SmokeFree
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hlopg.R
import com.hlopg.data.model.PGDetailUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PGDetailScreen(
    uiState: PGDetailUiState?,
    onBackClick: () -> Unit = {},
    onBookConfirmed: () -> Unit = {}
) {
    var selectedSharing by remember { mutableStateOf("1") }
    var isFoodMenuExpanded by remember { mutableStateOf(false) }
    var showBookingConfirmation by remember { mutableStateOf(false) }



    if (uiState == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = Color(0xFF6C63FF))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Loading PG details...",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
        return
    }

    // Calculate selected price
    val selectedPrice = when (selectedSharing) {
        "1" -> uiState.sharing["1 Sharing"] ?: uiState.price
        "2" -> uiState.sharing["2 Sharing"] ?: uiState.price
        "3" -> uiState.sharing["3 Sharing"] ?: uiState.price
        "4" -> uiState.sharing["4 Sharing"] ?: uiState.price
        "5" -> uiState.sharing["5 Sharing"] ?: uiState.price
        else -> uiState.price
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Top Bar
            item {
                PGDetailTopBar(
                    onBackClick = onBackClick,
                    onFavoriteClick = { },
                    onShareClick = { }
                )
            }

            // Location Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF333333),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    uiState.address?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            color = Color(0xFF333333),
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }

            // Image Carousel
            item {
                val pagerState = rememberPagerState(pageCount = { uiState.images.size })

                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(280.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            // For now using drawable, but you can replace with Coil for network images
                            Image(
                                painter = painterResource(R.drawable.hostel2),
                                contentDescription = "PG Image ${page + 1}",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    // Image indicators
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(pagerState.pageCount) { index ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp)
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (index == pagerState.currentPage) Color(0xFF6C63FF)
                                        else Color(0xFFD0D0D0)
                                    )
                            )
                        }
                    }
                }
            }

            // Description
            item {
                Text(
                    text = uiState.description,
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Badge
            item {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(uiState.badgeColor))
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = uiState.badge,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            // Sharing Type Section
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    Text(
                        text = "Select Sharing Type",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        listOf("1", "2", "3", "4", "5").forEach { type ->
                            SharingTypeChip(
                                type = type,
                                isSelected = selectedSharing == type,
                                onClick = { selectedSharing = type }
                            )
                        }
                    }

                    Text(
                        text = "₹$selectedPrice",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6C63FF),
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }

            // Amenities Section
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    Text(
                        text = "Amenities",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        maxItemsInEachRow = 6
                    ) {
                        uiState.amenities.forEach { amenity ->
                            AmenityItem(amenity, getAmenityIcon(amenity))
                        }
                    }
                }
            }

            // PG Rules Section
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    Text(
                        text = "PG Rules",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        uiState.rules.take(2).forEach { rule ->
                            RuleIcon(
                                icon = when {
                                    rule.contains("alcohol", ignoreCase = true) -> Icons.Default.LocalBar
                                    rule.contains("smok", ignoreCase = true) -> Icons.Default.SmokeFree
                                    else -> Icons.Default.CheckCircle
                                },
                                label = rule
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF5F5F5))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "If you plan to leave the PG, please inform us at least 15 days in advance to receive your advance amount. Failure to do so will result in forfeiture of the advance.",
                            fontSize = 13.sp,
                            color = Color(0xFF666666),
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // Food Menu Section
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isFoodMenuExpanded = !isFoodMenuExpanded }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Food Menu",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF000000)
                        )
                        Icon(
                            imageVector = if (isFoodMenuExpanded) Icons.Default.KeyboardArrowUp
                            else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color(0xFF000000),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    if (isFoodMenuExpanded) {
                        Spacer(modifier = Modifier.height(12.dp))
                        FoodMenuTable()
                    }
                }
            }

            // PG Location Section
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    Text(
                        text = "PG Location",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Image(
                            painter = painterResource(R.drawable.hos2),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // Ratings Section
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    Text(
                        text = "Ratings and Reviews",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Very Good",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF000000)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                repeat(5) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Total 19 ratings and\n10 reviews",
                                fontSize = 12.sp,
                                color = Color(0xFF666666),
                                textAlign = TextAlign.Center,
                                lineHeight = 16.sp
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RatingBar(5, 8)
                            RatingBar(4, 3)
                            RatingBar(3, 4)
                            RatingBar(2, 2)
                            RatingBar(1, 2)
                        }
                    }
                }
            }

            // Review Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE8F0FF))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color(0xFF6C63FF),
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF6C63FF))
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.hostel2),
                                    contentDescription = "Reviewer",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Absolutely loved my stay at this PG! The rooms were clean, spacious, and well-maintained. The staff was friendly and always ready to help — felt just like home!",
                                fontSize = 13.sp,
                                color = Color(0xFF000000),
                                lineHeight = 19.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = Color(0xFF6C63FF),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Bottom Button
        Button(
            onClick = { showBookingConfirmation = true },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6C63FF),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Book Now • From ₹$selectedPrice",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        BookingConfirmationDialog(
            visible = showBookingConfirmation,
            message = "Do you want to proceed with booking this PG?",
            onConfirm = {
                showBookingConfirmation = false
                onBookConfirmed()   // 🔥 navigate after confirm
            },
            onCancel = {
                showBookingConfirmation = false
            }
        )

    }
}

@Composable
fun FoodMenuTable() {
    val days = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
    val breakfast = listOf(
        "Dosa, Chutney",
        "Idly, chutney",
        "Bonda, chutney",
        "Uthapam, chutney",
        "Puri, curry",
        "Upma, chutney",
        "Ugani, chutney"
    )

    val lunch = listOf(
        "Rice, curry, dal curd",
        "Rice, curry, dal rasam, curd",
        "Rice, curry, dal curd",
        "Rice, curry, dal rasam, curd",
        "Rice, curry, dal samber, curd",
        "Rice, curry, dal samber, curd",
        "Rice, curry, chicken, raita, rasam"
    )

    val dinner = listOf(
        "Rice, curry, dal chapati, curry rasam, curd",
        "Rice, curry, dal chapati, curry rasam, curd",
        "Rice, curry, chicken, raita, rasam",
        "Rice, curry, dal chapati, curry rasam, curd",
        "Rice, curry, dal chapati, curry rasam, curd",
        "Rice, curry, dal chapati, curry rasam, curd",
        "Rice, curry, dal samber, curd"
    )


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp, 12.dp)
            .background(Color.White, RoundedCornerShape(5.dp))
            .border(1.dp, Color(0xFFEAEAEA), RoundedCornerShape(5.dp))
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // HEADER
        TableRow {
            TableCell("DAY", Color(0xFFD4C5F9), Modifier.weight(0.8f), isHeader = true)
            TableCell("BREAK FAST", Color(0xFF7FDEAB), Modifier.weight(1.2f), isHeader = true)
            TableCell("LUNCH", Color(0xFFFFB199), Modifier.weight(1.2f), isHeader = true)
            TableCell("DINNER", Color(0xFFFF99D8), Modifier.weight(1.2f), isHeader = true)
        }

        // DATA ROWS
        days.forEachIndexed { index, day ->
            TableRow {
                TableCell(day, Color(0xFFE8DCFF), Modifier.weight(0.8f), isDayCell = true)
                TableCell(breakfast[index], Color(0xFFD4F5E3), Modifier.weight(1.2f), isBullet = true)
                TableCell(lunch[index], Color(0xFFFFDDD4), Modifier.weight(1.2f))
                TableCell(dinner[index], Color(0xFFFFD4F0), Modifier.weight(1.2f))
            }
        }
    }
}
@Composable
fun TableCell(
    text: String,
    bgColor: Color,
    modifier: Modifier = Modifier,
    isBullet: Boolean = false,
    isHeader: Boolean = false,
    isDayCell: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(bgColor, RoundedCornerShape(1.dp))
            .padding(horizontal = 8.dp, vertical = 5.dp),
        contentAlignment = if (isHeader || isDayCell)
            Alignment.Center
        else
            Alignment.CenterStart
    ) {
        if (isBullet) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Text(
//                    text = "•",
//                    fontSize = 12.sp,
//                    modifier = Modifier.padding(end = 6.dp)
//                )
                Text(
                    text = text,
                    fontSize = 12.sp,
                    lineHeight = 15.sp
                )
            }
        } else {
            Text(
                text = text,
                fontSize = 12.sp,
                lineHeight = 15.sp,
                maxLines = if (isDayCell) 1 else 3,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                style = LocalTextStyle.current.copy(
                    lineBreak = LineBreak.Paragraph
                ),
                fontWeight = if (isHeader || isDayCell)
                    FontWeight.SemiBold
                else
                    FontWeight.Normal,
                textAlign = if (isHeader || isDayCell )
                    TextAlign.Center
                else
                    TextAlign.Start
            )
        }
    }
}

@Composable
private fun TableRow(
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        content = content
    )
}


@Composable
fun PGDetailTopBar(
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFE8F4FF))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF000000),
                    modifier = Modifier.size(24.dp)
                )
            }

            Row {
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color(0xFF000000),
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(onClick = onShareClick) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color(0xFF000000),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun SharingTypeChip(
    type: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val glowColor = Color(0xFF6C63FF)

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.06f else 1f,
        label = ""
    )

    Box(
        contentAlignment = Alignment.Center
    ) {

        // 🌈 REAL GLOW (fades outward)
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(56.dp) // slightly bigger than chip
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                glowColor.copy(alpha = 0.45f),
                                glowColor.copy(alpha = 0.25f),
                                glowColor.copy(alpha = 0.10f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }

        // 🔵 ACTUAL CHIP
        Box(
            modifier = Modifier
                .size(44.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(
                    if (isSelected) Color(0xFFF0EDFF) else Color.White
                )
                .border(
                    width = if (isSelected) 1.dp else 1.dp,
                    color = if (isSelected) glowColor else Color(0xFFE0E0E0),
                    shape = CircleShape
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = type,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) glowColor else Color(0xFF999999)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = if (isSelected) glowColor else Color(0xFF999999),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}


@Composable
fun AmenityItem(name: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(56.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = name,
            tint = Color(0xFF6C63FF),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = name,
            fontSize = 10.sp,
            color = Color(0xFF333333),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
fun RuleIcon(icon: ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF6C63FF),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color(0xFF333333),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RatingBar(stars: Int, count: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$stars",
            fontSize = 13.sp,
            color = Color(0xFF000000),
            modifier = Modifier.width(18.dp)
        )
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFF000000),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(Color(0xFFE0E0E0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(count / 10f)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xFF6C63FF))
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = count.toString(),
            fontSize = 13.sp,
            color = Color(0xFF666666),
            modifier = Modifier.width(18.dp)
        )
    }
}

fun getAmenityIcon(name: String): ImageVector {
    return when (name.lowercase()) {
        "free wifi", "wifi" -> Icons.Default.Wifi
        "fan" -> Icons.Default.Air
        "bed" -> Icons.Default.Bed
        "washing", "washing machine" -> Icons.Default.LocalLaundryService
        "lights" -> Icons.Default.Lightbulb
        "colboard", "coboard" -> Icons.Default.Weekend
        "geyser" -> Icons.Default.WaterDrop
        "water" -> Icons.Default.Water
        "fridge" -> Icons.Default.Kitchen
        "gym" -> Icons.Default.FitnessCenter
        "tv" -> Icons.Default.Tv
        "ac" -> Icons.Default.AcUnit
        "parking" -> Icons.Default.LocalParking
        "food" -> Icons.Default.Restaurant
        "lift" -> Icons.Default.Elevator
        "cam's" -> Icons.Default.Videocam
        "self cook..." -> Icons.Default.SoupKitchen
        else -> Icons.Default.CheckCircle
    }
}
@Composable
fun BookingConfirmationDialog(
    visible: Boolean,
    title: String = "Confirm Booking",
    message: String = "Are you sure you want to book now?",
    confirmText: String = "Confirm",
    cancelText: String = "Cancel",
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    if (!visible) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.25f)) // 🔹 lighter dim
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onCancel() },
        contentAlignment = Alignment.Center
    ) {

        Card(
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .fillMaxWidth(0.78f)   // 🔹 smaller card
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {}
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = title,
                    fontSize = 16.sp,           // 🔹 smaller
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = message,
                    fontSize = 13.sp,           // 🔹 smaller
                    textAlign = TextAlign.Center,
                    color = Color(0xFF666666)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),       // 🔹 smaller buttons
                        onClick = onCancel
                    ) {
                        Text(cancelText, fontSize = 13.sp)
                    }

                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6C63FF),
                            contentColor = Color.White
                        )
                    ) {
                        Text(confirmText, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
