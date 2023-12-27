package com.example.lightcinema.ui.models

data class MovieModel(
    val id: Int,
    var name: String,
    val description: String,
    val genres: List<Genre>,
    val createdYear: Int,
    val countries: List<Country>,
    val ageLimit: Int,
    var imageLink: String,
    var posterLink: String,
    val sessionMap: Map<String, List<SessionModel>>
)

class Genre(val id: Int, val name: String)
class Country(val id: Int, val name: String)