package com.example.lightcinema.ui.screens.profile

class ReserveModel(
    val sessionId: Int,
    val seatId: Int,
    val movieName: String,
    val hall: Int,
    val row: Int,
    val number: Int,
    val canUnreserve: Boolean,
    val date: String,
    val time: String,
)