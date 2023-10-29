package com.example.lightcinema.models

data class Seat(
    val hallId: Int,
    val row: Int,
    val numberInRow: Int,
    val costClass: CostClass,
    var isSelected: Boolean
) {

}