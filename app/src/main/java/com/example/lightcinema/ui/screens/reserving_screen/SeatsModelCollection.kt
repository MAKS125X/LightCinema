package com.example.lightcinema.ui.screens.reserving_screen

data class SeatsModelCollection(
    val seats: Array<Array<SeatModel>>,
    val baseCost: Int,
    val vipCost: Int
)