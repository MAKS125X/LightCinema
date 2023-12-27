package com.example.lightcinema.data.share

class MovieLongResponse(
    val id: Int,
    val name: String,
    val description: String,
    val genres: List<GenreResponse>,
    val createdYear: Int,
    val countries: List<CountryResponse>,
    val ageLimit: Int,
    val imageLink: String,
    val posterLink: String,
    val sessions: List<SessionDateResponse>
)