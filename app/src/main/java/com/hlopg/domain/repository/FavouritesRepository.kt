package com.hlopg.domain.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavoritesRepository {

    private val _favoritePGIds = MutableStateFlow<Set<String>>(emptySet())
    val favoritePGIds: StateFlow<Set<String>> = _favoritePGIds

    fun toggleFavorite(pgId: String) {
        val current = _favoritePGIds.value.toMutableSet()
        if (current.contains(pgId)) {
            current.remove(pgId)
        } else {
            current.add(pgId)
        }
        _favoritePGIds.value = current
    }

    fun clearAllFavorites() {
        _favoritePGIds.value = emptySet()
    }
}
