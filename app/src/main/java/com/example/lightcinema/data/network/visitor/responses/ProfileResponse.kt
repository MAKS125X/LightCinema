package com.example.lightcinema.data.network.visitor.responses

class ProfileResponse(
    val email: String,
    val name: String,
    val reserves: List<ReserveResponse>
)