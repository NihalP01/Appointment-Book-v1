package com.example.appointmentbook.data.AlllBookReq

class AllBookReqData : ArrayList<AllBookReqDataItem>()

data class AllBookReqDataItem(
    val action_by: Any,
    val created_at: String,
    val id: Int,
    val requested_by: Int,
    val requested_user: RequestedUser,
    val slot: Slot,
    val slot_id: Int,
    val status: String,
    val status_reason: Any,
    val updated_at: String
)

data class RequestedUser(
    val email: String,
    val id: Int,
    val name: String,
    val phone: String
)

data class Slot(
    val associate_id: Int,
    val associated_to: User,
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

data class User(
    val id: Int,
    val name: String,
    val phone: String,
    val email: String,
)