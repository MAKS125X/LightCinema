package com.example.lightcinema.ui.screens.filminfo


data class MovieModel(
    val id: Int,
    val name: String,
    val description: String,
    val genre: List<String>,
    val createdYear: Int,
    val country: String,
    val onlyAdult: Boolean,
    val imageLink: String,
    val sessionMap: Map<String, List<SessionModel>>
)