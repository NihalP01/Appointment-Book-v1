package com.example.medomind.data

data class ApiSlotData (
    val results : ArrayList<SlotsData>
)

data class SlotsData(
    val associate_id: Int,
    val available: Boolean,
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