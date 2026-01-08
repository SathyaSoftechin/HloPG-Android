package com.hlopg.presentation.admin.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hlopg.data.model.HostelUploadRequest
import com.hlopg.domain.repository.HostelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadPGViewModel @Inject constructor(
    private val repository: HostelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UploadPGUiState())
    val uiState: StateFlow<UploadPGUiState> = _uiState.asStateFlow()

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()

    private var currentImageType: String = ""



    // PG Basic Info
    fun updatePGName(name: String) {
        _uiState.value = _uiState.value.copy(pgName = name)
    }

    fun updatePGInformation(info: String) {
        _uiState.value = _uiState.value.copy(pgInformation = info)
    }

    fun updatePGType(type: String) {
        _uiState.value = _uiState.value.copy(pgType = type)
    }

    // Location
    fun updateCity(city: String) {
        _uiState.value = _uiState.value.copy(city = city)
    }

    fun updateArea(area: String) {
        _uiState.value = _uiState.value.copy(area = area)
    }

    // Image handling
    fun setCurrentImageType(type: String) {
        currentImageType = type
    }

    fun onImageSelected(uri: Uri) {
        when (currentImageType) {
            "room1" -> _uiState.value = _uiState.value.copy(roomImage1 = uri)
            "room2" -> _uiState.value = _uiState.value.copy(roomImage2 = uri)
            "washroom" -> _uiState.value = _uiState.value.copy(washRoomImage = uri)
            "parking" -> _uiState.value = _uiState.value.copy(parkingImage = uri)
            "extra" -> {
                val current = _uiState.value.extraImages.toMutableList()
                current.add(uri)
                _uiState.value = _uiState.value.copy(extraImages = current)
            }
        }
        currentImageType = ""
    }

    fun onVideoSelected(uri: Uri) {
        _uiState.value = _uiState.value.copy(videoUrl = uri)
    }

    // Food Menu
    fun updateFoodMenu(day: String, meal: String, value: String) {
        val currentMenu = _uiState.value.foodMenu.toMutableMap()
        val dayMenu = currentMenu[day]?.toMutableMap() ?: mutableMapOf()
        dayMenu[meal] = value
        currentMenu[day] = dayMenu
        _uiState.value = _uiState.value.copy(foodMenu = currentMenu)
    }

    // Amenities
    fun toggleAmenity(amenity: String) {
        val currentAmenities = _uiState.value.amenities.toMutableMap()
        currentAmenities[amenity] = !(currentAmenities[amenity] ?: false)
        _uiState.value = _uiState.value.copy(amenities = currentAmenities)
    }

    // Floors and Rooms
    fun updateNumberOfFloors(floors: Int) {
        _uiState.value = _uiState.value.copy(numberOfFloors = floors)
    }

    fun updateRoomsPerFloor(rooms: Int) {
        _uiState.value = _uiState.value.copy(roomsPerFloor = rooms)
    }

    fun updateStartingRoomNumber(roomNumber: String) {
        _uiState.value = _uiState.value.copy(startingRoomNumber = roomNumber)
    }

    // Sharing Prices
    fun updateSharingPrice(sharing: String, price: String) {
        val currentPrices = _uiState.value.sharingPrices.toMutableMap()
        currentPrices[sharing] = price
        _uiState.value = _uiState.value.copy(sharingPrices = currentPrices)
    }

    // Advance Amount
    fun updateAdvanceAmount(amount: String) {
        _uiState.value = _uiState.value.copy(advanceAmount = amount)
    }

    // Upload
    fun uploadPGDetails() {
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading

            try {
                // Validate input
                val validationError = validateInput()
                if (validationError != null) {
                    _uploadState.value = UploadState.Error(validationError)
                    return@launch
                }

                // Convert UI state to API request
                val uploadRequest = convertToUploadRequest()

                // Call repository to upload
                // TODO: Implement actual API call with image upload
                // Steps:
                // 1. Convert Uri to File or Base64
                // 2. Upload images to server or cloud storage
                // 3. Get image URLs
                // 4. Create request with URLs
                // 5. Call repository.uploadHostel(uploadRequest)

                // Uncomment when API is ready:
                // val result = repository.uploadHostel(uploadRequest)
                // if (result.isSuccess) {
                //     _uploadState.value = UploadState.Success
                // } else {
                //     _uploadState.value = UploadState.Error(result.exceptionOrNull()?.message ?: "Upload failed")
                // }

                // For now, simulate success after delay
                kotlinx.coroutines.delay(2000)
                _uploadState.value = UploadState.Success

            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun validateInput(): String? {
        val state = _uiState.value

        return when {
            state.pgName.isBlank() -> "Please enter PG name"
            state.pgInformation.isBlank() -> "Please enter PG information"
            state.pgType.isBlank() -> "Please select PG type"
            state.city.isBlank() -> "Please enter city"
            state.area.isBlank() -> "Please enter area"
            state.roomImage1 == null -> "Please upload at least one room image"
            state.numberOfFloors < 1 -> "Please select number of floors"
            state.roomsPerFloor < 1 -> "Please select rooms per floor"
            state.startingRoomNumber.isBlank() -> "Please enter starting room number"
            state.sharingPrices.values.all { it.isBlank() } -> "Please enter at least one sharing price"
            state.advanceAmount.isBlank() -> "Please enter advance amount"
            else -> null
        }
    }

    private fun convertToUploadRequest(): HostelUploadRequest {
        val state = _uiState.value

        // Convert sharing prices map to proper format
        val sharingMap = mutableMapOf<String, Int>()
        state.sharingPrices.forEach { (key, value) ->
            value.toIntOrNull()?.let { price ->
                sharingMap[key] = price
            }
        }

        // Get base price (lowest sharing price)
        val basePrice = sharingMap.values.minOrNull()?.toDouble() ?: 0.0

        return HostelUploadRequest(
            pgName = state.pgName,
            pgInfo = state.pgInformation,
            pgType = state.pgType,
            address = state.area,
            area = state.area,
            city = state.city,
            state = "Telangana", // Default or can be added to UI
            pincode = "500000", // Can be added to UI
            rules = listOf("No Alcohol", "No smoking"), // Can be made dynamic
            ownerId = 1, // Will come from logged-in user
            furnish = "Semi Furnished", // Can be added to UI
            sharing = sharingMap,
            foodMenu = convertFoodMenuToString(state.foodMenu),
            rating = 0.0,
            price = basePrice,
            amenities = state.amenities,
            popular = 0,
            numberOfFloors = state.numberOfFloors,
            roomsPerFloor = state.roomsPerFloor,
            startingRoomNumber = state.startingRoomNumber,
            advanceAmount = state.advanceAmount.toIntOrNull() ?: 0,
            images = buildImagesList(state)
        )
    }

    private fun convertFoodMenuToString(menu: Map<String, Map<String, String>>): String {
        // Convert food menu to string format for API
        // This can be customized based on API requirements
        return menu.entries.joinToString("\n") { (day, meals) ->
            "$day: ${meals.entries.joinToString(", ") { "${it.key}: ${it.value}" }}"
        }
    }

    private fun buildImagesList(state: UploadPGUiState): List<String> {
        // TODO: Convert Uri to actual image URLs after uploading
        // For now, return uri strings
        val images = mutableListOf<String>()
        state.roomImage1?.toString()?.let { images.add(it) }
        state.roomImage2?.toString()?.let { images.add(it) }
        state.washRoomImage?.toString()?.let { images.add(it) }
        state.parkingImage?.toString()?.let { images.add(it) }
        images.addAll(state.extraImages.map { it.toString() })
        return images
    }

    fun resetUploadState() {
        _uploadState.value = UploadState.Idle
    }

    fun resetForm() {
        _uiState.value = UploadPGUiState()
        _uploadState.value = UploadState.Idle
    }
}

// UI State
data class UploadPGUiState(
    val pgName: String = "",
    val pgInformation: String = "",
    val pgType: String = "",
    val city: String = "",
    val area: String = "",
    val roomImage1: Uri? = null,
    val roomImage2: Uri? = null,
    val washRoomImage: Uri? = null,
    val parkingImage: Uri? = null,
    val videoUrl: Uri? = null,
    val extraImages: List<Uri> = emptyList(),
    val foodMenu: Map<String, Map<String, String>> = mapOf(
        "MON-D" to mapOf("breakfast" to "", "lunch" to "", "dinner" to ""),
        "TUES-D" to mapOf("breakfast" to "", "lunch" to "", "dinner" to ""),
        "WED-D" to mapOf("breakfast" to "", "lunch" to "", "dinner" to ""),
        "THUR-D" to mapOf("breakfast" to "", "lunch" to "", "dinner" to ""),
        "FRI-D" to mapOf("breakfast" to "", "lunch" to "", "dinner" to ""),
        "SAT-D" to mapOf("breakfast" to "", "lunch" to "", "dinner" to ""),
        "SUN-D" to mapOf("breakfast" to "", "lunch" to "", "dinner" to "")
    ),
    val amenities: Map<String, Boolean> = mapOf(
        "Free Wifi" to false,
        "Fan" to false,
        "Bed" to false,
        "washing" to false,
        "Lights" to false,
        "coboard" to false,
        "Geyser" to false,
        "water" to false,
        "Fridge" to false,
        "Gym" to false,
        "TV" to false,
        "Ac" to false,
        "Parking" to false,
        "Food" to false,
        "Lift" to false,
        "Cam's" to false,
        "Self Cook..." to false
    ),
    val numberOfFloors: Int = 5,
    val roomsPerFloor: Int = 5,
    val startingRoomNumber: String = "",
    val sharingPrices: Map<String, String> = mapOf(
        "2 Sharing" to "",
        "3 Sharing" to "",
        "4 Sharing" to "",
        "5 Sharing" to ""
    ),
    val advanceAmount: String = ""
)

// Upload State
sealed class UploadState {
    object Idle : UploadState()
    object Loading : UploadState()
    object Success : UploadState()
    data class Error(val message: String) : UploadState()
}