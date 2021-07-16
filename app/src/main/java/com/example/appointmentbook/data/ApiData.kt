package com.example.appointmentbook.Network

data class Login(
    val type: String,
    val token: String,
)


data class User(
    val name: String,
    val email: String,
    val role: String
)

data class Signup(
    val status: String,
    val error: Error,
)


data class SignUpTeacher(
    val status: String
)