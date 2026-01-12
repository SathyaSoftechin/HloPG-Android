package com.hlopg.presentation.admin.viewmodel

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RoomManagementViewModel @Inject constructor() : ViewModel() {

    private val _roomsState = MutableStateFlow<Map<String, List<RoomData>>>(emptyMap())
    val roomsState: StateFlow<Map<String, List<RoomData>>> = _roomsState.asStateFlow()

    private val _statisticsState = MutableStateFlow(RoomStatistics())
    val statisticsState: StateFlow<RoomStatistics> = _statisticsState.asStateFlow()

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

        // Create 3 floors with 6 rooms each
        for (floor in 1..3) {
            val floorKey = "${floor}${getOrdinalSuffix(floor)} Floor"
            val roomsList = mutableListOf<RoomData>()

            for (room in 0 until 6) {
                val roomNumber = ((floor * 100) + 1 + room).toString()
                // Varied sharing capacity for testing
                val sharingCapacity = when (room % 4) {
                    0 -> 3
                    1 -> 2
                    2 -> 4
                    else -> 1
                }

                val beds = createBedsForCapacity(sharingCapacity).mapIndexed { index, bed ->
                    // Mix of occupied and available beds
                    bed.copy(isOccupied = index % 3 == 0)
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
        updateStatistics()
    }

    /**
     * Initialize rooms based on PG configuration from previous screen
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
                val roomNumber = (startingNumber + (floor - 1) * 100 + room).toString()
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
        updateStatistics()
    }

    /**
     * Increase bed capacity for a room (optimized for instant update)
     */
    fun increaseBedCapacity(floorKey: String, roomNumber: String) {
        _roomsState.update { currentRooms ->
            val floorRooms = currentRooms[floorKey] ?: return@update currentRooms

            val updatedFloorRooms = floorRooms.map { room ->
                if (room.roomNumber == roomNumber && room.sharingCapacity < 10) {
                    val newCapacity = room.sharingCapacity + 1
                    val updatedBeds = room.beds + BedData(
                        bedNumber = newCapacity,
                        isOccupied = false
                    )
                    room.copy(
                        sharingCapacity = newCapacity,
                        beds = updatedBeds
                    )
                } else {
                    room
                }
            }

            currentRooms + (floorKey to updatedFloorRooms)
        }
        updateStatistics()
    }

    /**
     * Decrease bed capacity for a room (optimized for instant update)
     */
    fun decreaseBedCapacity(floorKey: String, roomNumber: String) {
        _roomsState.update { currentRooms ->
            val floorRooms = currentRooms[floorKey] ?: return@update currentRooms

            val updatedFloorRooms = floorRooms.map { room ->
                if (room.roomNumber == roomNumber && room.sharingCapacity > 0) {
                    val newCapacity = room.sharingCapacity - 1
                    val updatedBeds = if (newCapacity > 0) {
                        room.beds.dropLast(1)
                    } else {
                        emptyList()
                    }
                    room.copy(
                        sharingCapacity = newCapacity,
                        beds = updatedBeds
                    )
                } else {
                    room
                }
            }

            currentRooms + (floorKey to updatedFloorRooms)
        }
        updateStatistics()
    }

    /**
     * Update sharing capacity for a specific room (legacy method for text input)
     */
    fun updateRoomSharingCapacity(floorKey: String, roomNumber: String, capacity: Int) {
        _roomsState.update { currentRooms ->
            val floorRooms = currentRooms[floorKey] ?: return@update currentRooms

            val updatedFloorRooms = floorRooms.map { room ->
                if (room.roomNumber == roomNumber) {
                    val updatedBeds = createBedsForCapacity(capacity)
                    // Preserve existing bed occupancy if possible
                    val preservedBeds = updatedBeds.mapIndexed { index, bed ->
                        if (index < room.beds.size) {
                            bed.copy(isOccupied = room.beds[index].isOccupied)
                        } else {
                            bed
                        }
                    }
                    room.copy(
                        sharingCapacity = capacity,
                        beds = preservedBeds
                    )
                } else {
                    room
                }
            }

            currentRooms + (floorKey to updatedFloorRooms)
        }
        updateStatistics()
    }

    /**
     * Create bed objects based on sharing capacity
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
     * Toggle bed occupancy status (optimized for instant update)
     */
    fun toggleBedOccupancy(floorKey: String, roomNumber: String, bedNumber: Int) {
        _roomsState.update { currentRooms ->
            val floorRooms = currentRooms[floorKey] ?: return@update currentRooms

            val updatedFloorRooms = floorRooms.map { room ->
                if (room.roomNumber == roomNumber) {
                    val updatedBeds = room.beds.map { bed ->
                        if (bed.bedNumber == bedNumber) {
                            bed.copy(isOccupied = !bed.isOccupied)
                        } else {
                            bed
                        }
                    }
                    room.copy(beds = updatedBeds)
                } else {
                    room
                }
            }

            currentRooms + (floorKey to updatedFloorRooms)
        }
        updateStatistics()
    }

    /**
     * Occupy all beds in a specific room (optimized)
     */
    fun occupyAllBedsInRoom(floorKey: String, roomNumber: String) {
        _roomsState.update { currentRooms ->
            val floorRooms = currentRooms[floorKey] ?: return@update currentRooms

            val updatedFloorRooms = floorRooms.map { room ->
                if (room.roomNumber == roomNumber) {
                    room.copy(beds = room.beds.map { it.copy(isOccupied = true) })
                } else {
                    room
                }
            }

            currentRooms + (floorKey to updatedFloorRooms)
        }
        updateStatistics()
    }

    /**
     * Free all beds in a specific room (optimized)
     */
    fun freeAllBedsInRoom(floorKey: String, roomNumber: String) {
        _roomsState.update { currentRooms ->
            val floorRooms = currentRooms[floorKey] ?: return@update currentRooms

            val updatedFloorRooms = floorRooms.map { room ->
                if (room.roomNumber == roomNumber) {
                    room.copy(beds = room.beds.map { it.copy(isOccupied = false) })
                } else {
                    room
                }
            }

            currentRooms + (floorKey to updatedFloorRooms)
        }
        updateStatistics()
    }

    /**
     * Occupy all beds in all rooms (Bulk Action)
     */
    fun occupyAllBeds() {
        _roomsState.update { currentRooms ->
            currentRooms.mapValues { (_, rooms) ->
                rooms.map { room ->
                    room.copy(beds = room.beds.map { it.copy(isOccupied = true) })
                }
            }
        }
        updateStatistics()
    }

    /**
     * Free all beds in all rooms (Bulk Action)
     */
    fun freeAllBeds() {
        _roomsState.update { currentRooms ->
            currentRooms.mapValues { (_, rooms) ->
                rooms.map { room ->
                    room.copy(beds = room.beds.map { it.copy(isOccupied = false) })
                }
            }
        }
        updateStatistics()
    }

    /**
     * Reset all room capacities to 0 (Bulk Action)
     */
    fun resetAllRoomCapacities() {
        _roomsState.update { currentRooms ->
            currentRooms.mapValues { (_, rooms) ->
                rooms.map { room ->
                    room.copy(
                        sharingCapacity = 0,
                        beds = emptyList()
                    )
                }
            }
        }
        updateStatistics()
    }

    /**
     * Update statistics whenever room data changes
     */
    private fun updateStatistics() {
        val allRooms = _roomsState.value.values.flatten()
        val totalRooms = allRooms.size
        val totalBeds = allRooms.sumOf { it.beds.size }
        val occupiedBeds = allRooms.sumOf { room -> room.beds.count { it.isOccupied } }
        val availableBeds = totalBeds - occupiedBeds

        _statisticsState.value = RoomStatistics(
            totalRooms = totalRooms,
            totalBeds = totalBeds,
            occupiedBeds = occupiedBeds,
            availableBeds = availableBeds,
            occupancyPercentage = if (totalBeds > 0) (occupiedBeds * 100f / totalBeds) else 0f
        )
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
        return _statisticsState.value
    }

    /**
     * Get floor-wise statistics
     */
    fun getFloorStatistics(floorKey: String): FloorStatistics {
        val rooms = _roomsState.value[floorKey] ?: emptyList()
        val totalBeds = rooms.sumOf { it.beds.size }
        val occupiedBeds = rooms.sumOf { it.occupiedBeds }
        val availableBeds = totalBeds - occupiedBeds

        return FloorStatistics(
            floorKey = floorKey,
            totalRooms = rooms.size,
            totalBeds = totalBeds,
            occupiedBeds = occupiedBeds,
            availableBeds = availableBeds,
            occupancyPercentage = if (totalBeds > 0) (occupiedBeds * 100f / totalBeds) else 0f
        )
    }

    /**
     * Export room data to be sent along with PG upload
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
                errorMessage = "Please set sharing capacity for ${roomsWithoutCapacity.size} room(s)"
            )
        }

        return ValidationResult(isValid = true, errorMessage = null)
    }

    /**
     * Search rooms by room number
     */
    fun searchRooms(query: String): List<Pair<String, RoomData>> {
        val results = mutableListOf<Pair<String, RoomData>>()

        _roomsState.value.forEach { (floorKey, rooms) ->
            rooms.filter {
                it.roomNumber.contains(query, ignoreCase = true)
            }.forEach { room ->
                results.add(floorKey to room)
            }
        }

        return results
    }

    /**
     * Reset all room data
     */
    fun resetRoomData() {
        _roomsState.value = emptyMap()
        updateStatistics()
    }
}

// Data Models

/**
 * Represents a single room with its beds
 * Marked as @Stable for better Compose performance
 */
@Stable
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

    val occupancyPercentage: Float
        get() = if (beds.isNotEmpty()) (occupiedBeds * 100f / beds.size) else 0f
}

/**
 * Represents a single bed in a room
 * Marked as @Stable for better Compose performance
 */
@Stable
data class BedData(
    val bedNumber: Int,
    val isOccupied: Boolean
)

/**
 * Statistics for all rooms
 */
@Stable
data class RoomStatistics(
    val totalRooms: Int = 0,
    val totalBeds: Int = 0,
    val occupiedBeds: Int = 0,
    val availableBeds: Int = 0,
    val occupancyPercentage: Float = 0f
)

/**
 * Statistics for a specific floor
 */
@Stable
data class FloorStatistics(
    val floorKey: String,
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