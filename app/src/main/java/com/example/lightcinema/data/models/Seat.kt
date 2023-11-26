package com.example.lightcinema.data.models

import androidx.compose.runtime.MutableState

data class Seat(
    val hallId: Int,
    val row: Int,
    val numberInRow: Int,
    val costClass: CostClass,
    var isSelected: Boolean
) {

}