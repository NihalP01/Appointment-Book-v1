package com.example.appointmentbook.data.BookReqData

class BookReqData : ArrayList<BookReqDataItem>()

data class BookReqDataItem(
    val associate_id: Int,
    val available: Int,
    val booking_end_time: String,
    val booking_start_time: String,
    val bookings: List<Booking>,
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

data class Booking(
    val action_by: Any,
    val created_at: String,
    val id: Int,
    val requested_by: Int,
    val requested_user: List<RequestedUser>,
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