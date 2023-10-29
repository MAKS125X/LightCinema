package com.example.lightcinema

import com.example.lightcinema.models.CostClass
import com.example.lightcinema.models.Seat

fun getTestSeatsList(): MutableList<Seat> {
    return mutableListOf(
        Seat(1, 1, 1, CostClass.BASIC, false),
        Seat(1, 1, 2, CostClass.TAKEN, false),
        Seat(1, 1, 3, CostClass.TAKEN, false),
        Seat(1, 1, 4, CostClass.BASIC, false),
        Seat(1, 1, 5, CostClass.BASIC, false),
        Seat(1, 1, 6, CostClass.BASIC, false),
        Seat(1, 1, 7, CostClass.BASIC, false),

        Seat(1, 2, 1, CostClass.BASIC, false),
        Seat(1, 2, 2, CostClass.BASIC, false),
        Seat(1, 2, 3, CostClass.TAKEN, false),
        Seat(1, 2, 4, CostClass.TAKEN, false),
        Seat(1, 2, 5, CostClass.BASIC, false),
        Seat(1, 2, 6, CostClass.BASIC, false),
        Seat(1, 2, 7, CostClass.BASIC, false),

        Seat(1, 3, 1, CostClass.BASIC, false),
        Seat(1, 3, 2, CostClass.TAKEN, false),
        Seat(1, 3, 3, CostClass.BASIC, false),
        Seat(1, 3, 4, CostClass.BASIC, false),
        Seat(1, 3, 5, CostClass.BASIC, false),
        Seat(1, 3, 6, CostClass.TAKEN, false),
        Seat(1, 3, 7, CostClass.BASIC, false),

        Seat(1, 4, 1, CostClass.BASIC, false),
        Seat(1, 4, 2, CostClass.BASIC, false),
        Seat(1, 4, 3, CostClass.BASIC, false),
        Seat(1, 4, 4, CostClass.BASIC, false),
        Seat(1, 4, 5, CostClass.BASIC, false),
        Seat(1, 4, 6, CostClass.BASIC, false),
        Seat(1, 4, 7, CostClass.BASIC, false),

        Seat(1, 5, 1, CostClass.BASIC, false),
        Seat(1, 5, 2, CostClass.TAKEN, true),
        Seat(1, 5, 3, CostClass.BASIC, true),
        Seat(1, 5, 4, CostClass.TAKEN, true),
        Seat(1, 5, 5, CostClass.BASIC, false),
        Seat(1, 5, 6, CostClass.BASIC, false),
        Seat(1, 5, 7, CostClass.BASIC, false),

        Seat(1, 6, 1, CostClass.VIP, false),
        Seat(1, 6, 2, CostClass.VIP, false),
        Seat(1, 6, 3, CostClass.VIP, false),
        Seat(1, 6, 4, CostClass.TAKEN, false),
        Seat(1, 6, 5, CostClass.VIP, false),
        Seat(1, 6, 6, CostClass.VIP, false),
        Seat(1, 6, 7, CostClass.VIP, false),

        Seat(1, 7, 1, CostClass.BASIC, false),
        Seat(1, 7, 2, CostClass.BASIC, false),
        Seat(1, 7, 3, CostClass.BASIC, false),
        Seat(1, 7, 4, CostClass.TAKEN, false),
        Seat(1, 7, 5, CostClass.TAKEN, false),
        Seat(1, 7, 6, CostClass.TAKEN, false),
        Seat(1, 7, 7, CostClass.BASIC, false),
    )
}