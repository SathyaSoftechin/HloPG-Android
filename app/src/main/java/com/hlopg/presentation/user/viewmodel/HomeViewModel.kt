package com.hlopg.presentation.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hlopg.domain.mapper.HostelMapper.toPGDetailsList
import com.hlopg.domain.repository.HostelRepository
import com.hlopg.domain.repository.Resource
import com.hlopg.presentation.components.PGDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val recommendedPGs: List<PGDetails> = emptyList(),
    val popularPGs: List<PGDetails> = emptyList(),
    val newlyAddedPGs: List<PGDetails> = emptyList(),
    val premiumPGs: List<PGDetails> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

// Navigation events from Home screen
sealed class HomeNavEvent {
    data class OpenPGDetails(val pgId: String) : HomeNavEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HostelRepository
) : ViewModel() {

    // raw lists from repository (unfiltered)
    private val _recommendedRaw = MutableStateFlow<List<PGDetails>>(emptyList())
    private val _popularRaw = MutableStateFlow<List<PGDetails>>(emptyList())
    private val _newlyAddedRaw = MutableStateFlow<List<PGDetails>>(emptyList())
    private val _premiumRaw = MutableStateFlow<List<PGDetails>>(emptyList())

    // selected filter index: 0 = All, 1 = Men, 2 = Women, 3 = Co-Living
    private val _selectedIndex = MutableStateFlow(0)
    val selectedIndex: StateFlow<Int> = _selectedIndex.asStateFlow()

    // loading & error indicators
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    // navigation events
    private val _navEvents = MutableSharedFlow<HomeNavEvent>()
    val navEvents: SharedFlow<HomeNavEvent> = _navEvents.asSharedFlow()

    private fun debugVisibleBadges(filtered: List<PGDetails>, label: String) {
        println("🔎 FILTER DEBUG [$label] (${filtered.size} PGs)")
        filtered.groupBy { it.badge }
            .forEach { (badge, group) ->
                println("Badge: '$badge' (${group.size})")
                group.forEach { pg ->
                    println("  - ${pg.name} (id: ${pg.id})")
                }
            }
        println("--------------------------------------------------")
    }



    /**
     * Exposed UI state: combine raw lists + selected filter to produce filtered lists.
     * Filtering is derived from PGDetails.badge (uses simple substring matching).
     */
    val uiState: StateFlow<HomeUiState> = combine(
        _recommendedRaw,
        _popularRaw,
        _newlyAddedRaw,
        _premiumRaw,
        _selectedIndex,
        _isLoading,
        _error
    ) { values ->

        val rec = values[0] as List<PGDetails>
        val pop = values[1] as List<PGDetails>
        val newb = values[2] as List<PGDetails>
        val prem = values[3] as List<PGDetails>
        val sel = values[4] as Int
        val loading = values[5] as Boolean
        val err = values[6] as String?

        fun matchesFilter(pg: PGDetails, index: Int): Boolean {
            val badge = pg.badge.lowercase().trim()
            return when (index) {
                0 -> true
                1 -> badge.contains("men") || badge.contains("male")
                2 -> badge.contains("women") || badge.contains("female")
                3 -> badge.contains("co-living") ||
                        badge.contains("coliving")
                else -> true
            }
        }

        val filteredRec = rec.filter { matchesFilter(it, sel) }
        val filteredPop = pop.filter { matchesFilter(it, sel) }
        val filteredNew = newb.filter { matchesFilter(it, sel) }
        val filteredPrem = prem.filter { matchesFilter(it, sel) }
        // 🔍 Debug visible badges for the active filter
        debugVisibleBadges(filteredRec, "Recommended")
        debugVisibleBadges(filteredPop, "Popular")
        debugVisibleBadges(filteredNew, "Newly Added")
        debugVisibleBadges(filteredPrem, "Premium")

        HomeUiState(
            recommendedPGs = rec.filter { matchesFilter(it, sel) },
            popularPGs = pop.filter { matchesFilter(it, sel) },
            newlyAddedPGs = newb.filter { matchesFilter(it, sel) },
            premiumPGs = prem.filter { matchesFilter(it, sel) },
            isLoading = loading,
            error = err
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, HomeUiState())


    init {
        loadAllData()
    }

    fun setFilter(index: Int) {
        _selectedIndex.value = index

    }

    fun loadAllData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            // load sections concurrently
            launch { loadRecommendedPGs() }
            launch { loadPopularPGs() }
            launch { loadNewlyAddedPGs() }
            launch { loadPremiumPGs() }
        }
    }

    private suspend fun loadRecommendedPGs() {
        when (val result = repository.getPopularHostels()) {
            is Resource.Success -> {
                _recommendedRaw.value = result.data?.toPGDetailsList()!!
                _isLoading.value = false
            }
            is Resource.Error -> {
                _error.value = result.message
                _isLoading.value = false
            }
            is Resource.Loading -> {
                _isLoading.value = true
            }
        }
    }

    private suspend fun loadPopularPGs() {
        when (val result = repository.getHyderabadHostels()) {
            is Resource.Success -> {
                _popularRaw.value = result.data?.toPGDetailsList()!!
            }
            is Resource.Error -> {
                _error.value = result.message
            }
            is Resource.Loading -> {}
        }
    }

    private suspend fun loadNewlyAddedPGs() {
        when (val result = repository.getChennaiHostels()) {
            is Resource.Success -> {
                _newlyAddedRaw.value = result.data?.toPGDetailsList()!!
            }
            is Resource.Error -> {
                _error.value = result.message
            }
            is Resource.Loading -> {}
        }
    }

    private suspend fun loadPremiumPGs() {
        when (val result = repository.getBangaloreHostels()) {
            is Resource.Success -> {
                _premiumRaw.value = result.data?.toPGDetailsList()!!
            }
            is Resource.Error -> {
                _error.value = result.message
            }
            is Resource.Loading -> {}
        }
    }

    fun onCardClick(pgId: String) {
        viewModelScope.launch {
            _navEvents.emit(HomeNavEvent.OpenPGDetails(pgId))
        }
    }

    fun onFavoriteClick(pgId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            // TODO: Call API to update favorite status - placeholder for now
            println("Favorite toggled for $pgId: $isFavorite")
        }
    }

    fun retry() {
        loadAllData()
    }
}