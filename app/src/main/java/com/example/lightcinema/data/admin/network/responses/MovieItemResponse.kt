package com.example.lightcinema.data.admin.network.responses

data class MovieItemResponse(
    val id: Int,
    val name: String,
    val genres: List<String>,
    val posterLink: String,
    val sessions: List<SessionTimeResponse>?
)

