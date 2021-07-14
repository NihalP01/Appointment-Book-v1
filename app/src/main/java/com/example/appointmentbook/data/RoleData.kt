package com.example.appointmentbook.data.roleData

data class RoleData(
    val `data`: Data
)

data class Data(
    val details: Any,
    val user: User
)

data class User(
    val email: String,
    val id: Int,
    val name: String,
    val phone: String,
    val role: String
)