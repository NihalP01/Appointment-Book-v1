package com.example.appointmentbook.data

data class ApiSlotData (
    val results : ArrayList<SlotsData>
)

data class SlotsData(
    val created_at: String,
    val id: Int,
    val slot: Slot,
    val slot_id: Int,
    val teacher: Teacher,
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

data class Teacher(
    val id: Int,
    val name: String
)

data class Timing(
    val created_at: String,
    val id: Int,
    val timing: String,
    val updated_at: String
)