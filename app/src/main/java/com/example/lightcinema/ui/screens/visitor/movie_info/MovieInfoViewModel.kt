package com.example.lightcinema.ui.screens.visitor.movie_info

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class MovieInfoViewModel(
    private val repository: VisitorRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val movieId: Int = checkNotNull(savedStateHandle[MainDestinations.MOVIE_INFO])

    private var _movie: MutableStateFlow<ApiResponse<MovieModel>> =
        MutableStateFlow(ApiResponse.Loading)
    val movie: StateFlow<ApiResponse<MovieModel>> = _movie.asStateFlow()

    init {
        updateMovieInfo()
    }

    fun updateMovieInfo() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                if (error is SocketTimeoutException) {
                    Log.d("asd", "${error}")
                    _movie.value = ApiResponse.Failure(500, "Отсутствие подключения к сети")
                }
            }
        }) {
            repository.getMovieInfo(movieId).collect {
                _movie.value = it
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)

                val repository = application.visitorModule.visitorRepository

                val savedStateHandle = createSavedStateHandle()

                MovieInfoViewModel(repository = repository, savedStateHandle)
            }
        }
    }
}