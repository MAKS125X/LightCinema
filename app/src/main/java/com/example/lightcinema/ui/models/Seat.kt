package com.example.lightcinema.ui.models

import androidx.compose.runtime.MutableState

class Seat(
    val hallId: Int,
    val row: Int,
    val numberInRow: Int,
    val costClass: CostClass,
    val isSelected: MutableState<Boolean>
)
