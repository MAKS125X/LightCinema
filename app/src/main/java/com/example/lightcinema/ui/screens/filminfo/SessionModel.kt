package com.example.lightcinema.ui.screens.filminfo

import java.time.LocalTime

data class SessionModel(
    val id: Int,
    val time: LocalTime,
    val minPrice: Int
)