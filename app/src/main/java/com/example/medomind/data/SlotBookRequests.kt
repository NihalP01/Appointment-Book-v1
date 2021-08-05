package com.example.medomind.data.SlotBookRequests

class SlotBookRequests : ArrayList<SlotBookRequestsItem>()

data class SlotBookRequestsItem(
    val action_by: Any,
    val created_at: String,
    val id: Int,
    val requested_by: Int,
    val slot: Slot,
    val slot_id: Int,
    val status: String,
    val status_reason: Any,
    val updated_at: String
)

data class Slot(
    val associate_id: Int,
    val associated_to: AssociatedTo,
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

data class AssociatedTo(
    val email: String,
    val id: Int,
    val name: String,
    val phone: String
)