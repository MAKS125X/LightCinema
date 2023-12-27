package com.example.lightcinema.data.admin.network.requests

data class SessionRequest(
    val movieId: Int,
    val dateTime: String,
    val hallNumber: Int,
    val price: Int,
    val increasedPrice: Int
)