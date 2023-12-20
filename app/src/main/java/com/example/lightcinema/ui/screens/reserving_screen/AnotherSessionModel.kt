package com.example.lightcinema.ui.screens.reserving_screen

import com.example.lightcinema.ui.models.SessionModel

data class AnotherSessionModel(
    val movieId: Int,
    val movieName: String,
    val sessionsDate: String,
    val sessions: List<SessionModel>
)