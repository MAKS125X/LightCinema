package com.example.lightcinema.ui.screens.admin.addSession

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lightcinema.data.admin.network.responses.HallCollectionResponse
import com.example.lightcinema.data.admin.repository.AdminRepository
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.di.MyApplication
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class AddSessionDialogViewModel(private val repository: AdminRepository) : ViewModel() {
    private var _hallList =
        MutableStateFlow<ApiResponse<HallCollectionResponse>>(ApiResponse.Loading)
    val hallList: StateFlow<ApiResponse<HallCollectionResponse>> =
        _hallList.asStateFlow()

    private var _sessionResponse: MutableSharedFlow<ApiResponse<ResponseBody>?> =
        MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val sessionResponse: Flow<ApiResponse<ResponseBody>?>
        get() = _sessionResponse.distinctUntilChanged()

    init {
        updateHallsList()
    }

    fun emitNullValue() {
        _sessionResponse.tryEmit(null)
    }

    fun updateHallsList() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                if (error is SocketTimeoutException) {
                    Log.d("asd", "${error}")
                    _hallList.value = ApiResponse.Failure(
                        500,
                        "Отсутствие подключения к сети"
                    )
                }
            }
        }) {
            repository.getHalls().collect {
                _hallList.value = it
            }
        }
    }

    fun addSession(
        movieId: Int,
        date: Long,
        hours: Int,
        minutes: Int,
        hallNumber: Int,
        price: Int,
        increasedPrice: Int
    ) {
        _sessionResponse.tryEmit(ApiResponse.Loading)
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        dateFormatter.timeZone = TimeZone.getTimeZone("GMT")

        val calendar: Calendar = Calendar.getInstance()
//        calendar.timeZone = TimeZone.getTimeZone("GMT+4")
        calendar.timeInMillis = date + hours * 60 * 60 * 1000 + minutes * 60 * 1000
        Log.d("AbobaCalendar", calendar.toString())
        Log.d("AbobaCalendar", dateFormatter.format(calendar.time))
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                if (error is SocketTimeoutException) {
                    Log.d("asd", "${error}")
                    _sessionResponse.tryEmit(
                        ApiResponse.Failure(
                            500,
                            "Отсутствие подключения к сети"
                        )
                    )
                }
            }
        }) {
            repository.addSession(
                movieId,
                dateFormatter.format(calendar.time),
                hallNumber,
                price,
                increasedPrice
            ).collect {
                _sessionResponse.tryEmit(it)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)

                val repository = application.adminModule.adminRepository

                AddSessionDialogViewModel(repository = repository)
            }
        }
    }
}