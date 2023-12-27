package com.example.lightcinema.ui.screens.visitor.reserving

data class SeatsModelCollection(
    val seats: Array<Array<SeatModel>>,
    val baseCost: Int,
    val vipCost: Int
)