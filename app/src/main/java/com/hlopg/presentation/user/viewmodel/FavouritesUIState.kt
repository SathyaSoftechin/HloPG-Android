package com.hlopg.presentation.user.viewmodel

import com.hlopg.presentation.components.PGDetails

data class FavoritesUiState(
    val favoritePGs: List<PGDetails> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
