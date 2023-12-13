package com.example.lightcinema.data.visitor.network.responses

class ProfileResponse(
    val login: String,
    val reserves: List<ReserveResponse>
)