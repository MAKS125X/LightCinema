package com.example.lightcinema.ui.screens.visitor.movie_info

import com.example.lightcinema.ui.models.SessionModel


data class MovieModel(
    val id: Int,
    val name: String,
    val description: String,
    val genres: List<String>,
    val createdYear: Int,
    val countries: List<String>,
    val ageLimit: Int,
    val imageLink: String,
    val sessionMap: Map<String, List<SessionModel>>
)