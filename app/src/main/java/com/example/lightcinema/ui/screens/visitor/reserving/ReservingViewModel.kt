package com.example.lightcinema.ui.screens.visitor.reserving

import android.util.Log
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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.net.SocketTimeoutException

class ReservingViewModel(
    private val repository: VisitorRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var sessionId: MutableStateFlow<Int> =
        MutableStateFlow(checkNotNull(savedStateHandle[MainDestinations.SESSION]))

    private val _seatsModelCollection: MutableStateFlow<ApiResponse<SeatsModelCollection>> =
        MutableStateFlow(ApiResponse.Loading)
    val seatsModelCollection: StateFlow<ApiResponse<SeatsModelCollection>>
        get() = _seatsModelCollection.asStateFlow()

    private var _movie: MutableStateFlow<ApiResponse<AnotherSessionModel>> =
        MutableStateFlow(ApiResponse.Loading)
    val movie: StateFlow<ApiResponse<AnotherSessionModel>>
        get() = _movie.asStateFlow()

    private var _reservingResponse: MutableSharedFlow<ApiResponse<ResponseBody>> =
        MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val reservingResponse: Flow<ApiResponse<ResponseBody>>
        get() = _reservingResponse.distinctUntilChanged()

    private val _successString: MutableStateFlow<String> = MutableStateFlow("")
    val successString: StateFlow<String>
        get() = _successString.asStateFlow()

    init {
//        sessionId.value = checkNotNull(savedStateHandle[MainDestinations.SESSION])
        updateSessionsInfo(sessionId.value)
        updateHallInfoBySession(sessionId.value)
    }

    fun setSession(newSessionId: Int) {
        sessionId.value = newSessionId
        updateHallInfoBySession(sessionId.value)
    }

    fun updateSessionsInfo(sessionId: Int) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                if (error is SocketTimeoutException) {
                    Log.d("asd", "${error}")
                    _movie.value = ApiResponse.Failure(
                        500,
                        "Произошла внутренняя ошибка. Повторите попытку позднее"
                    )
                }
            }
        }) {
            repository.getAnotherMovieSessions(sessionId).collect {
                _movie.value = it
            }
        }
    }

    fun updateHallInfoBySession(sessionId: Int) {
//        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
//            viewModelScope.launch(Dispatchers.Main) {
//                if (error is SocketTimeoutException) {
//                    Log.d("asd", "${error}")
//                    _movie.value = ApiResponse.Failure(
//                        500,
//                        "Произошла внутренняя ошибка. Повторите попытку позднее"
//                    )
//                }
//            }
//        }) {
//            repository.getSessionSeatsById(sessionId).collect {
//                _seatsModelCollection.value = it
//            }
//        }
        if (movie.value is ApiResponse.Success) {
            if ((movie.value as ApiResponse.Success<AnotherSessionModel>).data.sessions.any { it.id == sessionId }) {
                viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
                    viewModelScope.launch(Dispatchers.Main) {
                        if (error is SocketTimeoutException) {
                            Log.d("asd", "${error}")
                            _movie.value = ApiResponse.Failure(
                                500,
                                "Произошла внутренняя ошибка. Повторите попытку позднее"
                            )
                        }
                    }
                }) {
                    repository.getSessionSeatsById(sessionId).collect {
                        _seatsModelCollection.value = it
                    }
                }
            } else {
                _seatsModelCollection.tryEmit(
                    ApiResponse.Failure(
                        400,
                        "На данный сеанс нельзя забронировать места"
                    )
                )
            }
        }

    }

    fun getCountOfSelectedSeats(seats: SeatsModelCollection): Int {
        return seats.seats
            .flatMap { it.toList() }
            .filter { it.selected.value }
            .count()
    }

    fun reserveSeats() {
        val seats =
            (seatsModelCollection.value as ApiResponse.Success<SeatsModelCollection>).data.seats
        val selectedSeats =
            seats.flatMap { it.toList() }
                .filter { it.selected.value }
                .map { it.id }
        val movie = (movie.value as ApiResponse.Success<AnotherSessionModel>).data
        val time = movie.sessions.filter { session -> session.id == sessionId.value }
        Log.d("Aboba", movie.toString())
        if (time.isNullOrEmpty()) {
            _reservingResponse.tryEmit(
                ApiResponse.Failure(
                    404,
                    "Невозможно забронировать места на начавшийся фильм"
                )
            )
        } else {
            val time2 = time.first().time
            viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
                viewModelScope.launch(Dispatchers.Main) {
                    if (error is SocketTimeoutException) {
                        Log.d("asd", "${error}")
                        _reservingResponse.tryEmit(
                            ApiResponse.Failure(
                                500,
                                "Отсутствие подключения к сети"
                            )
                        )
                    }
                }
            }) {
                repository.reserveSeats(sessionId.value, selectedSeats).collect {
                    _reservingResponse.tryEmit(it)
                    _successString.value =
                        "Вы забронировали ${selectedSeats.count()} мест на фильм ${movie.movieName} в " +
                                time2
                }
            }
        }
    }

//    fun reserveSeats(seats: SeatsModelCollection, sessions: List<SessionModel>) {
//        val selectedSeats = seats.seats
//            .flatMap { it.toList() }
//            .filter { it.selected.value }
//            .map { it.id }
//        Log.e("Aboba", sessionId.value.toString())
//        Log.e("Aboba", sessions.joinToString { " " })
//        _successString.value =
//            "Вы забронировали ${selectedSeats.count()} мест на фильм ${movieName} в " +
//                    sessions
//                        .first { session -> session.id == sessionId.value }
//                        .time
//        Log.d("Aboba", _successString.value)
//        viewModelScope.launch {
//            repository.reserveSeats(sessionId.value, selectedSeats).collect {
//                _reservingResponse.tryEmit(it)
//            }
//        }
//    }

    fun changeSeatSelectedState(seat: SeatModel) {
        if (!seat.reserved) {
            seat.selected.value = !seat.selected.value
        }
    }

    private fun <T> List<T>.mapButReplace(targetItem: T, newItem: T) = map {
        if (it == targetItem) {
            newItem
        } else {
            it
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)

                val repository = application.visitorModule.visitorRepository

                val savedStateHandle = createSavedStateHandle()

                ReservingViewModel(repository = repository, savedStateHandle)
            }
        }
    }
}