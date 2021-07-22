package com.example.appointmentbook.data

data class DetailsUpdateData(
    val details: Details
)

data class Details(
    val speciality: String,
    val works_at: String
)

data class ResStatus(
    val status: String,
)