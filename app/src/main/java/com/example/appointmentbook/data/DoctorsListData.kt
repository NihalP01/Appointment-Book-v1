package com.example.appointmentbook.data.DoctorListData

data class DoctorsListData(
    val details: Details,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String
)

data class Details(
    val created_at: String,
    val details: String,
    val id: Int,
    val updated_at: String,
    val user_id: Int
)