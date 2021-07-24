package com.example.appointmentbook.data.roleData

data class RoleData(
    val `data`: Data
)

data class Data(
    val details: Details,
    val user: User
)

data class User(
    val email: String,
    val id: Int,
    val name: String,
    val phone: String,
    val role: String
)

data class Details(
    val id: Int,
    val user_id: Int,
    val details: WorkInfo,
    val created_at: String,
    val updated_at: String,
)

data class WorkInfo(
    val works_at: String,
    val speciality: String,
)