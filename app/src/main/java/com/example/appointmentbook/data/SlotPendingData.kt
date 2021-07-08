package com.example.appointmentbook.data.sample

class SlotPendingData : ArrayList<SlotPendingDataItem>()

data class SlotPendingDataItem(
    val action_by: Any,
    val created_at: String,
    val id: Int,
    val requested_by: RequestedBy,
    val slot_data: SlotData,
    val status: String,
    val student_id: Int,
    val teacher_slot_id: Int,
    val updated_at: String
)

data class SlotData(
    val created_at: String,
    val id: Int,
    val slot: Slot,
    val slot_id: Int,
    val teacher_id: Int,
    val updated_at: String
)

data class Slot(
    val capacity: Int,
    val created_at: String,
    val id: Int,
    val name: String,
    val timing: Timing,
    val timing_id: Int,
    val updated_at: String,
    val user_id: Int
)

data class RequestedBy(
    val email: String,
    val id: Int,
    val name: String
)


data class Timing(
    val created_at: String,
    val id: Int,
    val timing: String,
    val updated_at: String
)