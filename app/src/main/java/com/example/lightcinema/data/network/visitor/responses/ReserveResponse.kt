package com.example.lightcinema.data.network.visitor.responses

class ReserveResponse(
    val sessionId: Int,
    val placeId: Int,
    val movieName: String,
    val hall: Int,
    val row: Int,
    val number: Int,
    val dataTime: String //TODO Типизировать явно dateTime
)
