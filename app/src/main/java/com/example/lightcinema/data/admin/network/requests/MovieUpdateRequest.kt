package com.example.lightcinema.data.admin.network.requests

data class MovieUpdateRequest(
    val name: String,
    val descriptions: String,
    val imageLink: String,
    val posterLink: String,
    val year: Int,
    val ageLimit: Int,
    val genres: List<Int>,
    val countries: List<Int>
)