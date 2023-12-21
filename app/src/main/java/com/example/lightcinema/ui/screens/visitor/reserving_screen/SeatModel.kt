package com.example.lightcinema.ui.screens.visitor.reserving_screen

import androidx.compose.runtime.MutableState

data class SeatModel(
    val id: Int,
    val row: Int,
    val number: Int,
    val isVip: Boolean,
    val reserved: Boolean,
    val selected: MutableState<Boolean>
)
