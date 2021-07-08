package com.example.appointmentbook.data.BookRequestData

class BookRequestData : ArrayList<BookRequestDataItem>()

data class BookRequestDataItem(
    val action_by: Any,
    val action_perfromed_by: Any,
    val created_at: String,
    val id: Int,
    val slot_data: SlotData,
    val status: String,
    val student_id: Int,
    val teacher_slot_id: Int,
    val updated_at: String
)

data class Slot(
    val capacity: Int,
    val created_at: String,
    val id: Int,
    val name: String,
    val timing_id: Int,
    val updated_at: String,
    val user_id: Int
)

data class SlotData(
    val created_at: String,
    val id: Int,
    val slot: Slot,
    val slot_id: Int,
    val teacher: Teacher,
    val teacher_id: Int,
    val updated_at: String
)

data class Teacher(
    val id: Int,
    val name: String
)