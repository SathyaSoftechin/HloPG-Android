package com.hlopg.presentation.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hlopg.domain.mapper.HostelMapper.toPGDetails
import com.hlopg.domain.repository.HostelRepository
import com.hlopg.domain.repository.Resource
import com.hlopg.presentation.components.PGDetails
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

    // "pgId" must match your nav argument name
    private val hostelId: Int? =
        savedStateHandle.get<String>("pgId")?.toIntOrNull()

    private val _uiState = MutableStateFlow<PGDetails?>(null)
    val uiState: StateFlow<PGDetails?> = _uiState

    init {
        hostelId?.let { id ->
            loadPgDetails(id)
        }
    }

    private fun loadPgDetails(id: Int) {
        viewModelScope.launch {
            when (val result = repository.getHostelById(id)) {   // ✅ make sure HostelRepository has this
                is Resource.Success -> {
                    _uiState.value = result.data.toPGDetails()
                }
                is Resource.Error -> {
                    // you can add an error state later if you want
                    _uiState.value = null
                }
                is Resource.Loading -> {
                    // not used here, but you could add loading state if needed
                }
            }
        }
    }
}
