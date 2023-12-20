package com.example.lightcinema.data.visitor.network.responses

class ReserveResponse(
    val sessionId: Int,
    val seatId: Int,
    val movieName: String,
    val hall: Int,
    val row: Int,
    val number: Int,
    val canUnreserve: Boolean,
    val dateTime: String,
)
