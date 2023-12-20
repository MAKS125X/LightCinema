package com.example.lightcinema.ui.screens.poster

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.visitor.network.responses.MovieCollectionResponse
import com.example.lightcinema.data.visitor.repository.VisitorRepository
import com.example.lightcinema.di.MyApplication
import com.example.lightcinema.ui.models.SessionDate
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class PosterViewModel(private val repository: VisitorRepository) : ViewModel() {

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
            repository.getMovieCollection(true, date.name).collect {
                when (date) {
                    SessionDate.Today -> _posterToday.value = it
                    SessionDate.Tomorrow -> _posterTomorrow.value = it
                    SessionDate.Soon -> _posterSoon.value = it
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)

                val repository = application.visitorModule.visitorRepository
//                val repository = VisitorRepositoryMock(
//                    Retrofit.Builder().baseUrl(MyApplication.URL)
//                        .addConverterFactory(GsonConverterFactory.create()).build()
//                        .create(VisitorService::class.java)
//                )

                PosterViewModel(repository = repository)
            }
        }
    }
}



