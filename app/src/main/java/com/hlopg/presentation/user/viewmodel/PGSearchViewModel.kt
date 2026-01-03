package com.hlopg.presentation.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hlopg.domain.mapper.HostelMapper.toPGDetailsList
import com.hlopg.domain.repository.HostelRepository
import com.hlopg.domain.repository.Resource
import com.hlopg.presentation.components.PGDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val allPGs: List<PGDetails> = emptyList(),
    val filteredPGs: List<PGDetails> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    // Filter states
    val selectedPriceRange: ClosedFloatingPointRange<Float> = 0f..20000f,
    val selectedGender: String = "All",
    val selectedOccupancy: String = "All",
    val selectedAmenities: Set<String> = emptySet()
)

@HiltViewModel
class PGSearchViewModel @Inject constructor(
    private val repository: HostelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadAllPGs()
    }

    /**
     * Load all PGs from all cities - using the same pattern as HomeViewModel
     */
    private fun loadAllPGs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Launch all API calls concurrently using async
            val popularDeferred = async { repository.getPopularHostels() }
            val hyderabadDeferred = async { repository.getHyderabadHostels() }
            val chennaiDeferred = async { repository.getChennaiHostels() }
            val bangaloreDeferred = async { repository.getBangaloreHostels() }

            // Collect results
            val allPGsList = mutableListOf<PGDetails>()
            var hasError = false
            var errorMessage: String? = null

            // Wait for popular hostels (main data source)
            when (val result = popularDeferred.await()) {
                is Resource.Success -> {
                    result.data?.let { allPGsList.addAll(it.toPGDetailsList()) }
                }
                is Resource.Error -> {
                    hasError = true
                    errorMessage = result.message
                }
                is Resource.Loading -> {}
            }

            // Wait for Hyderabad hostels
            when (val result = hyderabadDeferred.await()) {
                is Resource.Success -> {
                    result.data?.let { allPGsList.addAll(it.toPGDetailsList()) }
                }
                is Resource.Error -> {
                    if (!hasError) {
                        errorMessage = result.message
                    }
                }
                is Resource.Loading -> {}
            }

            // Wait for Chennai hostels
            when (val result = chennaiDeferred.await()) {
                is Resource.Success -> {
                    result.data?.let { allPGsList.addAll(it.toPGDetailsList()) }
                }
                is Resource.Error -> {
                    if (!hasError) {
                        errorMessage = result.message
                    }
                }
                is Resource.Loading -> {}
            }

            // Wait for Bangalore hostels
            when (val result = bangaloreDeferred.await()) {
                is Resource.Success -> {
                    result.data?.let { allPGsList.addAll(it.toPGDetailsList()) }
                }
                is Resource.Error -> {
                    if (!hasError) {
                        errorMessage = result.message
                    }
                }
                is Resource.Loading -> {}
            }

            // Remove duplicates based on PG ID
            val uniquePGs = allPGsList.distinctBy { it.id }

            if (uniquePGs.isEmpty() && hasError) {
                _uiState.update {
                    it.copy(
                        error = errorMessage ?: "Failed to load PGs",
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        allPGs = uniquePGs,
                        filteredPGs = uniquePGs,
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }

    /**
     * Search PGs by query - implements debouncing
     */
    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }

        // Cancel previous search job
        searchJob?.cancel()

        // Debounce search with 300ms delay
        searchJob = viewModelScope.launch {
            delay(300)
            applyFilters()
        }
    }

    /**
     * Search for specific city
     */
    fun searchByCity(city: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    searchQuery = city
                )
            }

            val result = when (city.lowercase()) {
                "hyderabad", "hyd" -> repository.getHyderabadHostels()
                "chennai", "che" -> repository.getChennaiHostels()
                "bangalore", "bengaluru", "ben" -> repository.getBangaloreHostels()
                else -> repository.getPopularHostels()
            }

            when (result) {
                is Resource.Success -> {
                    val pgs = result.data!!.toPGDetailsList()
                    _uiState.update {
                        it.copy(
                            allPGs = pgs,
                            filteredPGs = pgs,
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    /**
     * Apply all filters: search query, price, gender, occupancy, amenities
     */
    private fun applyFilters() {
        val state = _uiState.value
        var filtered = state.allPGs

        // Filter by search query (name or location)
        if (state.searchQuery.isNotBlank()) {
            filtered = filtered.filter { pg ->
                pg.name.contains(state.searchQuery, ignoreCase = true)  ||
                        pg.badge.contains(state.searchQuery, ignoreCase = true)
            }
        }

        // Filter by price range
        filtered = filtered.filter { pg ->
            pg.price in state.selectedPriceRange.start.toInt()..state.selectedPriceRange.endInclusive.toInt()
        }

        // Filter by gender
        if (state.selectedGender != "All") {
            filtered = filtered.filter { pg ->
                val badge = pg.badge.lowercase()
                when (state.selectedGender) {
                    "Boys" -> badge.contains("men") || badge.contains("male") || badge.contains("boys")
                    "Girls" -> badge.contains("women") || badge.contains("female") || badge.contains("girls")
                    else -> true
                }
            }
        }

        // Filter by occupancy (if your PGDetails has occupancy field)
        if (state.selectedOccupancy != "All") {
            filtered = filtered.filter { pg ->
                // Add occupancy filtering logic if you have occupancy field
                // For now, keeping all PGs
                true
            }
        }



        _uiState.update { it.copy(filteredPGs = filtered) }
    }

    // Filter update functions
    fun updatePriceRange(range: ClosedFloatingPointRange<Float>) {
        _uiState.update { it.copy(selectedPriceRange = range) }
        applyFilters()
    }

    fun updateGender(gender: String) {
        _uiState.update { it.copy(selectedGender = gender) }
        applyFilters()
    }

    fun updateOccupancy(occupancy: String) {
        _uiState.update { it.copy(selectedOccupancy = occupancy) }
        applyFilters()
    }

    fun updateAmenities(amenities: Set<String>) {
        _uiState.update { it.copy(selectedAmenities = amenities) }
        applyFilters()
    }

    fun resetFilters() {
        _uiState.update {
            it.copy(
                selectedPriceRange = 0f..20000f,
                selectedGender = "All",
                selectedOccupancy = "All",
                selectedAmenities = emptySet()
            )
        }
        applyFilters()
    }

    fun onFavoriteClick(pgId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            // TODO: Call API to update favorite status
            println("Favorite toggled for $pgId: $isFavorite")

            // Update local state
            _uiState.update { state ->
                state.copy(
                    allPGs = state.allPGs.map { pg ->
                        if (pg.id == pgId) pg.copy(isFavorite = isFavorite) else pg
                    },
                    filteredPGs = state.filteredPGs.map { pg ->
                        if (pg.id == pgId) pg.copy(isFavorite = isFavorite) else pg
                    }
                )
            }
        }
    }

    fun retry() {
        loadAllPGs()
    }
}