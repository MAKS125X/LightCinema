package com.example.lightcinema.ui.screens.admin.poster

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lightcinema.data.admin.network.responses.MovieCollectionResponse
import com.example.lightcinema.data.admin.network.responses.MovieItemResponse
import com.example.lightcinema.data.admin.repository.AdminRepository
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.di.MyApplication
import com.example.lightcinema.ui.models.SessionDate
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

class PosterViewModel(private val repository: AdminRepository) : ViewModel() {

    private var _posterToday =
        MutableStateFlow<ApiResponse<MovieCollectionResponse>>(ApiResponse.Loading)
    val posterToday: StateFlow<ApiResponse<MovieCollectionResponse>> = _posterToday.asStateFlow()

    private var _posterTomorrow =
        MutableStateFlow<ApiResponse<MovieCollectionResponse>>(ApiResponse.Loading)
    val posterTomorrow: StateFlow<ApiResponse<MovieCollectionResponse>> =
        _posterTomorrow.asStateFlow()

    private var _posterSoon =
        MutableStateFlow<ApiResponse<MovieCollectionResponse>>(ApiResponse.Loading)
    val posterSoon: StateFlow<ApiResponse<MovieCollectionResponse>> = _posterSoon.asStateFlow()

    private var _deleteSessionResponse: MutableSharedFlow<ApiResponse<ResponseBody>> =
        MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val deleteSessionResponse: Flow<ApiResponse<ResponseBody>>
        get() = _deleteSessionResponse.distinctUntilChanged()

    init {
        getPosterInfo(SessionDate.Today)
        getPosterInfo(SessionDate.Tomorrow)
        getPosterInfo(SessionDate.Soon)
    }

    fun getPosterInfo(date: SessionDate) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                if (error is SocketTimeoutException) {
                    Log.d("asd", "${error}")
                    when (date) {
                        SessionDate.Today -> _posterToday.value =
                            ApiResponse.Failure(500, "Отсутствие подключения к сети")

                        SessionDate.Tomorrow -> _posterTomorrow.value =
                            ApiResponse.Failure(500, "Отсутствие подключения к сети")

                        SessionDate.Soon -> _posterSoon.value =
                            ApiResponse.Failure(500, "Отсутствие подключения к сети")
                    }
                }
            }
        }) {
            repository.getMoviesByDay(date.name, true).collect {
                when (date) {
                    SessionDate.Today -> _posterToday.value = it
                    SessionDate.Tomorrow -> _posterTomorrow.value = it
                    SessionDate.Soon -> _posterSoon.value = it
                }
            }
        }
    }

    fun deleteSession(sessionId: Int) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                if (error is SocketTimeoutException) {
                    Log.d("asd", "$error")
                    _deleteSessionResponse.tryEmit(
                        ApiResponse.Failure(
                            500,
                            "Произошла внутренняя ошибка. Повторите попытку позднее"
                        )
                    )
                }
            }
        }) {
            repository.deleteSession(sessionId).collect {
                _deleteSessionResponse.tryEmit(it)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)

                val repository = application.adminModule.adminRepository

                PosterViewModel(repository = repository)
            }
        }
    }
}



