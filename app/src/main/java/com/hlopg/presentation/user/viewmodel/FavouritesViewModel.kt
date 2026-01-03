package com.hlopg.presentation.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hlopg.domain.mapper.HostelMapper.toPGDetailsList
import com.hlopg.domain.repository.FavoritesRepository
import com.hlopg.domain.repository.HostelRepository
import com.hlopg.domain.repository.Resource
import com.hlopg.presentation.components.PGDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val hostelRepository: HostelRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _allPGs = MutableStateFlow<List<PGDetails>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val uiState: StateFlow<FavoritesUiState> = combine(
        _allPGs,
        favoritesRepository.favoritePGIds,
        _isLoading,
        _error
    ) { allPGs, favoriteIds, loading, error ->

        FavoritesUiState(
            favoritePGs = allPGs
                .filter { favoriteIds.contains(it.id) }
                .map { it.copy(isFavorite = true) },
            isLoading = loading,
            error = error
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, FavoritesUiState())

    init {
        loadAllPGs()
    }

    private fun loadAllPGs() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = hostelRepository.getPopularHostels()) {
                is Resource.Success -> {
                    _allPGs.value = result.data?.toPGDetailsList()!!
                    _error.value = null
                }
                is Resource.Error -> _error.value = result.message
                is Resource.Loading -> {}
            }
            _isLoading.value = false
        }
    }

    fun onFavoriteClick(pgId: String) {
        favoritesRepository.toggleFavorite(pgId)
    }

    fun clearAllFavorites() {
        favoritesRepository.clearAllFavorites()
    }

    fun retry() {
        loadAllPGs()
    }
}
