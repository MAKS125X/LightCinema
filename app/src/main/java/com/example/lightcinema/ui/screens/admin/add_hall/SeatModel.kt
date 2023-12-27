package com.example.lightcinema.ui.screens.admin.add_hall

import androidx.compose.runtime.MutableState

class SeatModel(
    val row: Int,
    val number: Int,
    val isIncreasedPrice: MutableState<Boolean>,
)
