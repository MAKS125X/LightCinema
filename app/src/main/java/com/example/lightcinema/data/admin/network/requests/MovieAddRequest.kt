package com.example.lightcinema.data.admin.network.requests

class MovieAddRequest(
    val name: String,
    val descriptions: String,
    val imageLink: String,
    val posterLink: String,
    val year: Int,
    val ageLimit: Int,
    val genre: List<Int>,
    val countries: List<Int>
)