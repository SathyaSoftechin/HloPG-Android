//package com.hlopg.presentation.viewmodel
//
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.hlopg.data.api.RetrofitInstance
//import com.hlopg.presentation.screen.PGDetail
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//class PGDetailViewModel : ViewModel() {
//
//    private val _pgDetail = MutableStateFlow<PGDetail?>(null)
//    val pgDetail: StateFlow<PGDetail?> = _pgDetail.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
//
//    private val _error = MutableStateFlow<String?>(null)
//    val error: StateFlow<String?> = _error.asStateFlow()
//
//    fun loadPGDetail(pgId: String) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            _error.value = null
//
//            try {
//                Log.d("PGDetailViewModel", "Fetching PG details for ID: $pgId")
//
//                val response = RetrofitInstance.api.getPGDetail(pgId)
//
//                if (response.isSuccessful) {
//                    response.body()?.let { apiResponse ->
//                        if (apiResponse.success) {
//                            _pgDetail.value = apiResponse.data
//                            Log.d("PGDetailViewModel", "PG details loaded successfully")
//                        } else {
//                            _error.value = apiResponse.message
//                            Log.e("PGDetailViewModel", "API Error: ${apiResponse.message}")
//                        }
//                    } ?: run {
//                        _error.value = "Empty response from server"
//                        Log.e("PGDetailViewModel", "Response body is null")
//                    }
//                } else {
//                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
//                    _error.value = "Error ${response.code()}: $errorMsg"
//                    Log.e("PGDetailViewModel", "HTTP Error: ${response.code()}, $errorMsg")
//                }
//            } catch (e: Exception) {
//                _error.value = "Failed to load PG details: ${e.message}"
//                Log.e("PGDetailViewModel", "Exception while loading PG details", e)
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    fun resetError() {
//        _error.value = null
//    }
//}