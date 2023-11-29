package com.example.lightcinema.data.visitor.network.responses

class MovieResponse(
    val id: Int,
    val name: String,
    val genre: List<String>,
    val posterLink: String,
    val sessions: List<Session>?
)