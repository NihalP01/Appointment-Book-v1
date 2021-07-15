package com.example.appointmentbook.data.SlotsListData

class SlotsListData : ArrayList<SlotsListDataItem>()

data class SlotsListDataItem(
    val associate_id: Int,
    val available: Int,
    val booking_end_time: String,
    val booking_start_time: String,
    val capacity: Int,
    val created_at: String,
    val current_filled: Int,
    val end_time: String,
    val expires_at: Any,
    val id: Int,
    val slot_uid: String,
    val start_time: String,
    val updated_at: String
)