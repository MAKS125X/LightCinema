package com.example.lightcinema.data.visitor.network.responses

data class MovieItemResponse(
    val id: Int,
    val name: String,
    val genre: List<String>,
    val posterLink: String,
    val sessions: List<SessionTimeResponse>?
)

