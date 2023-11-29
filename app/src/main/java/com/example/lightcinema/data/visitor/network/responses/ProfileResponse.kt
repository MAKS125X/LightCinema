package com.example.lightcinema.data.visitor.network.responses

class ProfileResponse(
    val email: String,
    val name: String,
    val reserves: List<ReserveResponse>
)