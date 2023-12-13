package com.example.lightcinema.data.visitor.network.responses

class MovieLongResponse(
    val id: Int,
    val name: String,
    val description: String,
    val genres: List<String>,
    val createdYear: Int,
    val countries: List<String>,
    val onlyAdult: Boolean,
    val imageLink: String,
    val sessions: List<SessionDateResponse>
)