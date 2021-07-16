package com.example.appointmentbook.data.SlotsbyReqIdData

class SlotsByReqId : ArrayList<SlotsByReqIdItem>()

data class SlotsByReqIdItem(
    val action_by: Int,
    val created_at: String,
    val id: Int,
    val requested_by: Int,
    val requested_user: RequestedUser,
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