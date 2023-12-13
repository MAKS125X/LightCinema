package com.example.lightcinema.ui.screens.movie_info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.visitor.network.api.VisitorService
import com.example.lightcinema.data.visitor.repository.VisitorRepository
import com.example.lightcinema.data.visitor.repository.VisitorRepositoryMock
import com.example.lightcinema.di.MyApplication
import com.example.lightcinema.ui.navigation.MainDestinations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        viewModelScope.launch {
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
//                val repository = VisitorRepositoryMock(
//                    Retrofit.Builder()
//                        .baseUrl(MyApplication.URL)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build()
//                        .create(VisitorService::class.java)
//                )

                val savedStateHandle = createSavedStateHandle()

                MovieInfoViewModel(repository = repository, savedStateHandle)
            }
        }
    }
}