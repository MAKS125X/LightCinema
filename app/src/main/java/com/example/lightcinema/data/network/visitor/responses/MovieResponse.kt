package com.example.lightcinema.data.network.visitor.responses

class MovieResponse(
    val id: Int,
    val name: String,
    val genre: List<String>,
    val posterLink: String,
    val sessions: List<Session>?
)