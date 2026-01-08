package com.hlopg.presentation.admin.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RoomManagementViewModel @Inject constructor() : ViewModel() {

    private val _roomsState = MutableStateFlow<Map<String, List<RoomData>>>(emptyMap())
    val roomsState: StateFlow<Map<String, List<RoomData>>> = _roomsState.asStateFlow()

    init {
        // Initialize with dummy data for testing
        initializeDummyData()
    }

    /**
     * Initialize with dummy data for testing
     * Remove this and use initializeRooms() when integrating with UploadPGScreen
     */
    private fun initializeDummyData() {
        val roomsMap = mutableMapOf<String, List<RoomData>>()

        // Create 2 floors with 5 rooms each
        for (floor in 1..2) {
            val floorKey = "${floor}${getOrdinalSuffix(floor)} Floor"
            val roomsList = mutableListOf<RoomData>()

            for (room in 0 until 5) {
                val roomNumber = (101 + room).toString()
                // Some rooms pre-filled with sharing capacity for testing
                val sharingCapacity = when (room) {
                    0 -> 3 // Room 101 has 3 sharing
                    1 -> 2 // Room 102 has 2 sharing
                    2 -> 4 // Room 103 has 4 sharing
                    else -> 0 // Others empty
                }

                val beds = if (sharingCapacity > 0) {
                    createBedsForCapacity(sharingCapacity).mapIndexed { index, bed ->
                        // Mark some beds as occupied for testing
                        bed.copy(isOccupied = index % 2 == 0)
                    }
                } else {
                    emptyList()
                }

                roomsList.add(
                    RoomData(
                        roomNumber = roomNumber,
                        floorNumber = floor,
                        sharingCapacity = sharingCapacity,
                        beds = beds
                    )
                )
            }
            roomsMap[floorKey] = roomsList
        }

        _roomsState.value = roomsMap
    }

    /**
     * Initialize rooms based on PG configuration from previous screen
     * Call this when entering the screen with data from UploadPGViewModel
     */
    fun initializeRooms(
        numberOfFloors: Int,
        roomsPerFloor: Int,
        startingRoomNumber: String
    ) {
        val roomsMap = mutableMapOf<String, List<RoomData>>()
        val startingNumber = startingRoomNumber.toIntOrNull() ?: 101

        for (floor in 1..numberOfFloors) {
            val floorKey = "${floor}${getOrdinalSuffix(floor)} Floor"
            val roomsList = mutableListOf<RoomData>()

            for (room in 0 until roomsPerFloor) {
                val roomNumber = (startingNumber + room).toString()
                roomsList.add(
                    RoomData(
                        roomNumber = roomNumber,
                        floorNumber = floor,
                        sharingCapacity = 0,
                        beds = emptyList()
                    )
                )
            }
            roomsMap[floorKey] = roomsList
        }

        _roomsState.value = roomsMap
    }

    /**
     * Update sharing capacity for a specific room
     * When user enters sharing capacity in the input field
     */
    fun updateRoomSharingCapacity(floorKey: String, roomNumber: String, capacity: Int) {
        val currentRooms = _roomsState.value.toMutableMap()
        val floorRooms = currentRooms[floorKey]?.toMutableList() ?: return

        val roomIndex = floorRooms.indexOfFirst { it.roomNumber == roomNumber }
        if (roomIndex != -1) {
            val room = floorRooms[roomIndex]
            val updatedBeds = createBedsForCapacity(capacity)
            floorRooms[roomIndex] = room.copy(
                sharingCapacity = capacity,
                beds = updatedBeds
            )
            currentRooms[floorKey] = floorRooms
            _roomsState.value = currentRooms
        }
    }

    /**
     * Create bed objects based on sharing capacity
     * Each bed can be marked as available or filled
     */
    private fun createBedsForCapacity(capacity: Int): List<BedData> {
        return (1..capacity).map { bedNumber ->
            BedData(
                bedNumber = bedNumber,
                isOccupied = false
            )
        }
    }

    /**
     * Toggle bed occupancy status by clicking on the color box
     * Green (Available) <-> Red (Filled)
     */
    fun toggleBedOccupancy(floorKey: String, roomNumber: String, bedNumber: Int) {
        val currentRooms = _roomsState.value.toMutableMap()
        val floorRooms = currentRooms[floorKey]?.toMutableList() ?: return

        val roomIndex = floorRooms.indexOfFirst { it.roomNumber == roomNumber }
        if (roomIndex != -1) {
            val room = floorRooms[roomIndex]
            val updatedBeds = room.beds.toMutableList()
            val bedIndex = updatedBeds.indexOfFirst { it.bedNumber == bedNumber }

            if (bedIndex != -1) {
                updatedBeds[bedIndex] = updatedBeds[bedIndex].copy(
                    isOccupied = !updatedBeds[bedIndex].isOccupied
                )
                floorRooms[roomIndex] = room.copy(beds = updatedBeds)
                currentRooms[floorKey] = floorRooms
                _roomsState.value = currentRooms
            }
        }
    }

    /**
     * Get all floors for display
     */
    fun getAllFloors(): List<String> {
        return _roomsState.value.keys.toList().sortedBy {
            it.filter { char -> char.isDigit() }.toIntOrNull() ?: 0
        }
    }

    /**
     * Get rooms for a specific floor
     */
    fun getRoomsForFloor(floorKey: String): List<RoomData> {
        return _roomsState.value[floorKey] ?: emptyList()
    }

    /**
     * Get room by number from a specific floor
     */
    fun getRoom(floorKey: String, roomNumber: String): RoomData? {
        return _roomsState.value[floorKey]?.find { it.roomNumber == roomNumber }
    }

    /**
     * Get ordinal suffix for floor numbers (1st, 2nd, 3rd, etc.)
     */
    private fun getOrdinalSuffix(number: Int): String {
        return when {
            number % 100 in 11..13 -> "th"
            number % 10 == 1 -> "st"
            number % 10 == 2 -> "nd"
            number % 10 == 3 -> "rd"
            else -> "th"
        }
    }

    /**
     * Get room occupancy statistics
     */
    fun getRoomStatistics(): RoomStatistics {
        val allRooms = _roomsState.value.values.flatten()
        val totalRooms = allRooms.size
        val totalBeds = allRooms.sumOf { it.beds.size }
        val occupiedBeds = allRooms.sumOf { room -> room.beds.count { it.isOccupied } }
        val availableBeds = totalBeds - occupiedBeds

        return RoomStatistics(
            totalRooms = totalRooms,
            totalBeds = totalBeds,
            occupiedBeds = occupiedBeds,
            availableBeds = availableBeds,
            occupancyPercentage = if (totalBeds > 0) (occupiedBeds * 100f / totalBeds) else 0f
        )
    }

    /**
     * Export room data to be sent along with PG upload
     * This can be passed back to the previous screen or sent directly to API
     */
    fun exportRoomData(): List<RoomExportData> {
        val exportList = mutableListOf<RoomExportData>()

        _roomsState.value.forEach { (floorKey, rooms) ->
            rooms.forEach { room ->
                exportList.add(
                    RoomExportData(
                        roomNumber = room.roomNumber,
                        floorNumber = room.floorNumber,
                        sharingCapacity = room.sharingCapacity,
                        totalBeds = room.beds.size,
                        occupiedBeds = room.beds.count { it.isOccupied },
                        availableBeds = room.beds.count { !it.isOccupied },
                        bedDetails = room.beds.map {
                            BedExportData(
                                bedNumber = it.bedNumber,
                                isOccupied = it.isOccupied
                            )
                        }
                    )
                )
            }
        }

        return exportList
    }

    /**
     * Validate if all rooms have sharing capacity set
     */
    fun validateRoomsConfiguration(): ValidationResult {
        val allRooms = _roomsState.value.values.flatten()

        if (allRooms.isEmpty()) {
            return ValidationResult(
                isValid = false,
                errorMessage = "No rooms configured. Please check floor configuration."
            )
        }

        val roomsWithoutCapacity = allRooms.filter { it.sharingCapacity == 0 }
        if (roomsWithoutCapacity.isNotEmpty()) {
            return ValidationResult(
                isValid = false,
                errorMessage = "Please set sharing capacity for all ${roomsWithoutCapacity.size} rooms"
            )
        }

        return ValidationResult(isValid = true, errorMessage = null)
    }

    /**
     * Reset all room data
     */
    fun resetRoomData() {
        _roomsState.value = emptyMap()
    }
}

// Data Models

/**
 * Represents a single room with its beds
 */
data class RoomData(
    val roomNumber: String,
    val floorNumber: Int,
    val sharingCapacity: Int,
    val beds: List<BedData>
) {
    val availableBeds: Int
        get() = beds.count { !it.isOccupied }

    val occupiedBeds: Int
        get() = beds.count { it.isOccupied }

    val isEmpty: Boolean
        get() = beds.isEmpty() || beds.all { !it.isOccupied }

    val isFull: Boolean
        get() = beds.isNotEmpty() && beds.all { it.isOccupied }
}

/**
 * Represents a single bed in a room
 */
data class BedData(
    val bedNumber: Int,
    val isOccupied: Boolean
)

/**
 * Statistics for all rooms
 */
data class RoomStatistics(
    val totalRooms: Int,
    val totalBeds: Int,
    val occupiedBeds: Int,
    val availableBeds: Int,
    val occupancyPercentage: Float
)

/**
 * Room data for export/API
 */
data class RoomExportData(
    val roomNumber: String,
    val floorNumber: Int,
    val sharingCapacity: Int,
    val totalBeds: Int,
    val occupiedBeds: Int,
    val availableBeds: Int,
    val bedDetails: List<BedExportData>
)

/**
 * Bed data for export/API
 */
data class BedExportData(
    val bedNumber: Int,
    val isOccupied: Boolean
)

/**
 * Validation result
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String?
)