package com.example.lightcinema.data.visitor.network.responses

class SessionsByMovieResponse(
    val movieId: Int,
    val movieName: String,
    val sessionsDate: String,
    val sessions: List<SessionTimeResponse>
)