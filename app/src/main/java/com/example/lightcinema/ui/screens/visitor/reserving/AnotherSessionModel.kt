package com.example.lightcinema.ui.screens.visitor.reserving

import com.example.lightcinema.ui.models.SessionModel

data class AnotherSessionModel(
    val movieId: Int,
    val movieName: String,
    val sessionsDate: String,
    val sessions: List<SessionModel>
)