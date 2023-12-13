package com.example.lightcinema

import androidx.compose.runtime.mutableStateOf
import com.example.lightcinema.ui.screens.cinemahall.CostClass
import com.example.lightcinema.ui.screens.cinemahall.SeatModel

//fun seatMapper(seat: com.example.lightcinema.data.models.Seat): com.example.lightcinema.ui.screens.cinemahall.Seat {
//    return com.example.lightcinema.ui.screens.cinemahall.Seat(
//        SeatId(seat.hallId, seat.row, seat.numberInRow),
//        seat.costClass,
//        seat.isSelected
//    )
//}

//fun seatMapper(seat: SeatModel): SeatModel {
//    return SeatModel(
//        seat.hallId,
//        seat.row,
//        seat.numberInRow,
//        seat.costClass,
//        seat.isSelected
//    )
//}
//
//
//fun getTestSeatsList(): MutableList<SeatModel> {
//    return mutableListOf(
//        SeatModel(1, 1, 1, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 1, 2, CostClass.TAKEN, mutableStateOf(false)),
//        SeatModel(1, 1, 3, CostClass.TAKEN, mutableStateOf(false)),
//        SeatModel(1, 1, 4, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 1, 5, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 1, 6, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 1, 7, CostClass.BASIC, mutableStateOf(false)),
//
//        SeatModel(1, 2, 1, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 2, 2, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 2, 3, CostClass.TAKEN, mutableStateOf(false)),
//        SeatModel(1, 2, 4, CostClass.TAKEN, mutableStateOf(false)),
//        SeatModel(1, 2, 5, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 2, 6, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 2, 7, CostClass.BASIC, mutableStateOf(false)),
//
//        SeatModel(1, 3, 1, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 3, 2, CostClass.TAKEN, mutableStateOf(false)),
//        SeatModel(1, 3, 3, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 3, 4, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 3, 5, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 3, 6, CostClass.TAKEN, mutableStateOf(false)),
//        SeatModel(1, 3, 7, CostClass.BASIC, mutableStateOf(false)),
//
//        SeatModel(1, 4, 1, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 4, 2, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 4, 3, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 4, 4, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 4, 5, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 4, 6, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 4, 7, CostClass.BASIC, mutableStateOf(false)),
//
//        SeatModel(1, 5, 1, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 5, 2, CostClass.TAKEN, mutableStateOf(false)),
//        SeatModel(1, 5, 3, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 5, 4, CostClass.TAKEN, mutableStateOf(false)),
//        SeatModel(1, 5, 5, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 5, 6, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 5, 7, CostClass.BASIC, mutableStateOf(false)),
//
//        SeatModel(1, 6, 1, CostClass.VIP, mutableStateOf(false)),
//        SeatModel(1, 6, 2, CostClass.VIP, mutableStateOf(false)),
//        SeatModel(1, 6, 3, CostClass.VIP, mutableStateOf(false)),
//        SeatModel(1, 6, 4, CostClass.TAKEN, mutableStateOf(false)),
//        SeatModel(1, 6, 5, CostClass.VIP, mutableStateOf(false)),
//        SeatModel(1, 6, 6, CostClass.VIP, mutableStateOf(false)),
//        SeatModel(1, 6, 7, CostClass.VIP, mutableStateOf(false)),
//
//        SeatModel(1, 7, 1, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 7, 2, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 7, 3, CostClass.BASIC, mutableStateOf(false)),
//        SeatModel(1, 7, 4, CostClass.TAKEN, mutableStateOf(false)),
//        SeatModel(1, 7, 5, CostClass.TAKEN, mutableStateOf(false)),
//        SeatModel(1, 7, 6, CostClass.TAKEN, mutableStateOf(false)),
//        SeatModel(1, 7, 7, CostClass.BASIC, mutableStateOf(false)),
//    )
//}