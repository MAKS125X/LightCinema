package com.example.lightcinema.ui.models

import androidx.compose.runtime.MutableState
import com.example.lightcinema.data.models.CostClass

class Seat(
    val hallId: Int, val row: Int, val numberInRow: Int,
    val costClass: CostClass,
    val isSelected: MutableState<Boolean>
)


class SeatId(val hallId: Int, val row: Int, val numberInRow: Int)