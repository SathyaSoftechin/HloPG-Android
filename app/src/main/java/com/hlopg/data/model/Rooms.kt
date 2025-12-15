package com.hlopg.data.model

data class Room(
    val room_id: Int,
    val hostel_id: Int,
    val floor: Int,
    val room_number: String,
    val sharing: Int,
    val price: Double,
    val status: String
)

data class RoomStructure(
    val roomNo: String,
    val beds: List<Int>
)

data class FloorStructure(
    val floorNumber: Int,
    val rooms: List<RoomStructure>
)

data class BulkCreateRoomsRequest(
    val hostel_id: Int,
    val floors: List<FloorStructure>
)

data class RoomUpdateItem(
    val room_id: Int,
    val sharing: Int? = null,
    val price: Double? = null,
    val status: String? = null
)

data class UpdateRoomsRequest(
    val rooms: List<RoomUpdateItem>
)

// Smart save
data class SmartRoomItem(
    val room_id: Int? = null,
    val floor: Int? = null,
    val room_number: String,
    val sharing: Int,
    val price: Double
)

data class SmartSaveRoomsRequest(
    val rooms: List<SmartRoomItem>
)
