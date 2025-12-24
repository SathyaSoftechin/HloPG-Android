package com.hlopg.presentation.user.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hlopg.data.model.Hostel
import com.hlopg.data.model.PGDetailUiState
import com.hlopg.domain.repository.HostelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PGDetailViewModel @Inject constructor(
    private val repository: HostelRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val hostelId: String? = savedStateHandle.get<String>("pgId")

    private val _uiState = MutableStateFlow<PGDetailUiState?>(null)
    val uiState: StateFlow<PGDetailUiState?> = _uiState

    init {
        loadPgDetails()
    }

    private fun loadPgDetails() {
        viewModelScope.launch {
            // Show loading state
            _uiState.value = null

            // Try to load from repository first, fallback to fake data
            try {
                // Uncomment when API is ready:
                // val hostel = repository.getHostelById(hostelId?.toIntOrNull() ?: 1)
                // mapHostelToUiState(hostel)

                // For now, using fake data:
                loadFakePgDetails()
            } catch (e: Exception) {
                // If API fails, load fake data
                loadFakePgDetails()
            }
        }
    }

    private fun loadFakePgDetails() {
        val fakeHostel = Hostel(
            id = hostelId?.toIntOrNull() ?: 1,
            pgName = "Universe PG (Co-Living)",
            pgInfo = "Universe PG in Madhapur, Hyderabad offers semi-furnished twin-sharing rooms across 3 floors with open parking and meals. Ideal for professionals and students, it's conveniently located near key amenities and available from March 25, 2025.",
            pgType = "Co-Living",
            address = "Madhapur ,100 Feet Road",
            area = "Madhapur",
            city = "Hyderabad",
            state = "Telangana",
            pincode = "500081",
            rules = listOf("No Alcohol", "No smoking"),
            ownerId = 101,
            furnish = "Semi Furnished",
            sharing = mapOf(
                "1 Sharing" to 15000,
                "2 Sharing" to 12000,
                "3 Sharing" to 10000,
                "4 Sharing" to 8500,
                "5 Sharing" to 7500
            ),
            foodMenu = "Day to day menu",
            rating = 4.5,
            price = 15000.0,
            amenities = mapOf(
                "Free Wifi" to true,
                "Fan" to true,
                "Bed" to true,
                "washing" to true,
                "Lights" to true,
                "coboard" to true,
                "Geyser" to true,
                "water" to true,
                "Fridge" to true,
                "Gym" to true,
                "TV" to true,
                "Ac" to true
            ),
            popular = 1
        )

        mapHostelToUiState(fakeHostel)
    }

    private fun mapHostelToUiState(hostel: Hostel) {
        val amenitiesList = hostel.amenities
            ?.filter { it.value }
            ?.keys
            ?.toList()
            ?: emptyList()

        val imagesList = listOf(
            "https://example.com/pg1.jpg",
            "https://example.com/pg2.jpg",
            "https://example.com/pg3.jpg",
            "https://example.com/pg4.jpg",
            "https://example.com/pg5.jpg"
        )

        _uiState.value = PGDetailUiState(
            id = hostel.id.toString(),
            name = hostel.pgName,
            address = "${hostel.area}, ${hostel.city}, ${hostel.address}",
            description = hostel.pgInfo ?: "A comfortable PG accommodation with modern amenities.",
            images = imagesList,
            sharing = hostel.sharing ?: mapOf("1 Sharing" to hostel.price.toInt()),
            amenities = amenitiesList,
            amenitiesCount = amenitiesList.size,
            rules = hostel.rules ?: emptyList(),
            foodMenu = hostel.foodMenu ?: "Menu details coming soon",
            rating = hostel.rating,
            price = hostel.price.toInt(),
            badge = hostel.pgType,
            badgeColor = getBadgeColor(hostel.pgType)
        )
    }

    private fun getBadgeColor(type: String): Long {
        return when (type.lowercase()) {
            "boys", "men" -> 0xFF2979FF           // Blue
            "girls", "women" -> 0xFFE91E63        // Pink
            "co-living", "coliving" -> 0xFF00C853 // Green
            "premium" -> 0xFFFF9800                // Orange
            else -> 0xFF4CAF50                     // Default Green
        }
    }

    fun refreshPgDetails() {
        loadPgDetails()
    }


    // Function to be called when real API is integrated
    fun loadFromApi(hostelId: Int) {
        viewModelScope.launch {
            _uiState.value = null
            try {
                // Uncomment when repository method is ready:
                // val hostel = repository.getHostelById(hostelId)
                // mapHostelToUiState(hostel)
            } catch (e: Exception) {
                // Handle error
                loadFakePgDetails()
            }
        }
    }
}