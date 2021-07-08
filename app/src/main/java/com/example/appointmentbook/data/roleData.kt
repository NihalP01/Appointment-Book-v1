package com.example.appointmentbook.data.RoleDataClass

data class roleData(
    val `data`: Data
)

data class Data(
    val details: Any,
    val user: User
)

data class User(
    val created_at: String,
    val email: String,
    val id: Int,
    val name: String,
    val password: String,
    val role: String,
    val updated_at: String
)
