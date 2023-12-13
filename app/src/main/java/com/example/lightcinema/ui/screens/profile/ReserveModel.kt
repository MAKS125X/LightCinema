package com.example.lightcinema.ui.screens.profile

class ReserveModel(
    val sessionId: Int,
    val placeId: Int,
    val movieName: String,
    val hall: Int,
    val row: Int,
    val number: Int,
    val canUnreserve: Boolean,
    val data: String,
    val time: String,
)