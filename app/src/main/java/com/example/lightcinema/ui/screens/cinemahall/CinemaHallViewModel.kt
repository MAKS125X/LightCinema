package com.example.lightcinema.ui.screens.cinemahall

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.visitor.repository.VisitorRepository
import com.example.lightcinema.di.MyApplication
import com.example.lightcinema.ui.navigation.MainDestinations
import com.example.lightcinema.ui.screens.movie_info.MovieInfoViewModel
import com.example.lightcinema.ui.screens.movie_info.MovieModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CinemaHallViewModel(
    private val repository: VisitorRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var sessionId: Int
    private val movieId: Int

    private val _seatsModelCollection: MutableStateFlow<ApiResponse<SeatsModelCollection>> =
        MutableStateFlow(ApiResponse.Loading)
    val seatsModelCollection
        get() = _seatsModelCollection.asStateFlow()

    private var _movie: MutableStateFlow<ApiResponse<MovieModel>> =
        MutableStateFlow(ApiResponse.Loading)
    val movie: StateFlow<ApiResponse<MovieModel>>
        get() = _movie.asStateFlow()

    init {
        sessionId = checkNotNull(savedStateHandle[MainDestinations.SESSIONS])
        movieId = checkNotNull(savedStateHandle[MainDestinations.MOVIE_INFO])
        updateSessionInfo(sessionId)
        updateMovieInfo()
    }

    fun updateMovieInfo() {
        viewModelScope.launch {
            repository.getMovieInfo(movieId).collect {
                _movie.value = it
            }
        }
    }

    fun updateSessionInfo(sessionId: Int) {
        viewModelScope.launch {
            repository.getSessionSeatsById(sessionId).collect {
                _seatsModelCollection.value = it
            }
        }
    }
//    private val _hallMatrix =
//        MutableStateFlow(
//            getTestSeatsList().map { seatMapper(it) }
//                .groupBy { it.row }
//                .map { it.value.sortedBy { seat -> seat.numberInRow } }
//                .sortedBy { it.first().row }.flatten().toMutableList()
//        )
//    val hallMatrix = _hallMatrix.asStateFlow()


    fun changeSeatSelectedState(seat: SeatModel) {
        if (!seat.reserved) {
            seat.selected.value = !seat.selected.value
        }
//        val index = hallMatrix.value.indexOfFirst { it == seat }
//        hallMatrix.value[index] = seat.copy(isSelected = !seat.isSelected)
    }


//    fun changeSeatSelectedState(lastSeat: SeatModel, newSeat: SeatModel) {
//        val newList = _hallMatrix.value.mapButReplace(lastSeat, newSeat)
//
////        _hallMatrix.value = newList.toMutableList()
//    }

    private fun <T> List<T>.mapButReplace(targetItem: T, newItem: T) = map {
        if (it == targetItem) {
            newItem
        } else {
            it
        }
    }


//    fun changeSeatSelectedState(row: Int, numberInRow: Int) {
//        val index =
//            _hallMatrix.value.indexOfFirst { it.row == row && it.numberInRow == numberInRow }
////        val seat = _hallMatrix.value[index].copy()
//
//        _hallMatrix.value.removeIf { it.row == row && it.numberInRow == numberInRow }
////        _hallMatrix.value.add(seat.copy(isSelected = !seat.isSelected))
//
//        Log.d(
//            "aboba",
//            _hallMatrix.value.first { it.row == row && it.numberInRow == numberInRow }.toString()
//        )


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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)

                val repository = application.visitorModule.visitorRepository
//                val repository = VisitorRepositoryMock(
//                    Retrofit.Builder()
//                        .baseUrl(MyApplication.URL)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build()
//                        .create(VisitorService::class.java)
//                )

                val savedStateHandle = createSavedStateHandle()

                CinemaHallViewModel(repository = repository, savedStateHandle)
            }
        }
    }
}
//}