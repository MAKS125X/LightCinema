package com.example.lightcinema.ui.screens.cinemahall

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.lightcinema.data.models.CostClass
import com.example.lightcinema.getTestSeatsList
import com.example.lightcinema.seatMapper
import com.example.lightcinema.ui.models.Seat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CinemaHallViewModel(
) : ViewModel() {

    private val _hallMatrix =
        MutableStateFlow(
            getTestSeatsList().map { seatMapper(it) }
                .groupBy { it.row }
                .map { it.value.sortedBy { seat -> seat.numberInRow } }
                .sortedBy { it.first().row }.flatten().toMutableList()
        )
    val hallMatrix = _hallMatrix.asStateFlow()


    fun changeSeatSelectedState(seat: Seat) {
        if (seat.costClass != CostClass.TAKEN) {
            seat.isSelected.value = !seat.isSelected.value
        }
//        val index = hallMatrix.value.indexOfFirst { it == seat }
//        hallMatrix.value[index] = seat.copy(isSelected = !seat.isSelected)
    }


    fun changeSeatSelectedState(lastSeat: Seat, newSeat: Seat) {
        val newList = _hallMatrix.value.mapButReplace(lastSeat, newSeat)

//        _hallMatrix.value = newList.toMutableList()
    }

    private fun <T> List<T>.mapButReplace(targetItem: T, newItem: T) = map {
        if (it == targetItem) {
            newItem
        } else {
            it
        }
    }


    fun changeSeatSelectedState(row: Int, numberInRow: Int) {
        val index =
            _hallMatrix.value.indexOfFirst { it.row == row && it.numberInRow == numberInRow }
//        val seat = _hallMatrix.value[index].copy()

        _hallMatrix.value.removeIf { it.row == row && it.numberInRow == numberInRow }
//        _hallMatrix.value.add(seat.copy(isSelected = !seat.isSelected))

        Log.d(
            "aboba",
            _hallMatrix.value.first { it.row == row && it.numberInRow == numberInRow }.toString()
        )


//TODO 1

//        val rowIndex = _hallMatrix.value.indexOfFirst { it.first().row == row }
//        val numberInRowIndex =
//            _hallMatrix.value[rowIndex].indexOfFirst { it.numberInRow == numberInRow }
//        val seatSelected = _hallMatrix.value[rowIndex][numberInRowIndex].isSelected
//
//        _hallMatrix.value[rowIndex][numberInRowIndex] =
//            _hallMatrix.value[rowIndex][numberInRowIndex].copy(isSelected = !seatSelected)

        //TODO 2
//        val TAG = "aboba"
//
//        val index =
//            _hallMatrix.value.indexOfFirst { it.row == row && it.numberInRow == numberInRow }
//        val seatSelected = _hallMatrix.value[index].isSelected
//
//        _hallMatrix.value[index] = _hallMatrix.value[index].copy(isSelected = !seatSelected)
//
//        val updatedList = _hallMatrix.value
//
//        _hallMatrix.value.clear()
//        _hallMatrix.value.addAll(updatedList)


//        val seatIsSelectedValue = hallMatrix.value.first { it.first().row == row }
//            .first { it.row == row && it.numberInRow == numberInRow }.isSelected
//
//        val seatValue = hallMatrix.value.first { it.first().row == row }
//            .first { it.row == row && it.numberInRow == numberInRow }


//        hallMatrix.value.first { it.first().row == row }
//            .first { it.row == row && it.numberInRow == numberInRow } =
//            seatValue.copy(seatIsSelectedValue = !seatValue.isSelected)


//        hallMatrix
//            .value
//            .first { it.first().row == row }
//            .first{ it.row == row && it.numberInRow == numberInRow } =
//            seatValue.copy(isSelected = !seatValue.isSelected)

    }
}