package com.example.lightcinema.ui.screens.poster

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.visitor.network.api.VisitorService
import com.example.lightcinema.data.visitor.network.responses.MovieCollectionResponse
import com.example.lightcinema.data.visitor.repository.VisitorRepository
import com.example.lightcinema.data.visitor.repository.VisitorRepositoryMock
import com.example.lightcinema.di.MyApplication
import com.example.lightcinema.ui.models.SessionDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PosterViewModel(private val repository: VisitorRepository) : ViewModel() {

    private var _posterToday =
        MutableStateFlow<ApiResponse<MovieCollectionResponse>>(ApiResponse.Loading)
    val posterToday: StateFlow<ApiResponse<MovieCollectionResponse>> = _posterToday

    private var _posterTomorrow =
        MutableStateFlow<ApiResponse<MovieCollectionResponse>>(ApiResponse.Loading)
    val posterTomorrow: StateFlow<ApiResponse<MovieCollectionResponse>> = _posterTomorrow

    private var _posterOther =
        MutableStateFlow<ApiResponse<MovieCollectionResponse>>(ApiResponse.Loading)
    val posterOther: StateFlow<ApiResponse<MovieCollectionResponse>> = _posterOther

    init {
        getPosterInfo(SessionDate.Today)
        getPosterInfo(SessionDate.Tomorrow)
        getPosterInfo(SessionDate.Other)
    }

    fun getPosterInfo(date: SessionDate) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMovieCollection(true, date.name).collect {
                when (date) {
                    SessionDate.Today -> _posterToday.value = it
                    SessionDate.Tomorrow -> _posterTomorrow.value = it
                    SessionDate.Other -> _posterOther.value = it
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)

//                val repository = application.visitorModule.visitorRepository
                val repository = VisitorRepositoryMock(
                    Retrofit.Builder().baseUrl(MyApplication.URL)
                        .addConverterFactory(GsonConverterFactory.create()).build()
                        .create(VisitorService::class.java)
                )

                PosterViewModel(repository = repository)
            }
        }
    }
}



