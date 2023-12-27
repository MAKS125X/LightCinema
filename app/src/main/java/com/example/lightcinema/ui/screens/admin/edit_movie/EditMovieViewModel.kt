package com.example.lightcinema.ui.screens.admin.edit_movie

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lightcinema.data.admin.repository.AdminRepository
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.di.MyApplication
import com.example.lightcinema.ui.models.Country
import com.example.lightcinema.ui.models.Genre
import com.example.lightcinema.ui.models.MovieModel
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
import java.util.Calendar

class EditMovieViewModel(
    val repository: AdminRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val movieId: Int = savedStateHandle[MainDestinations.MOVIE_INFO] ?: -1

    private var _movie: MutableSharedFlow<ApiResponse<MovieModel>> =
        MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val movie: Flow<ApiResponse<MovieModel>>
        get() = _movie.distinctUntilChanged()


    private var _genres: MutableStateFlow<ApiResponse<List<GenreModifiedModel>>> =
        MutableStateFlow(ApiResponse.Loading)
    val genres: StateFlow<ApiResponse<List<GenreModifiedModel>>> = _genres.asStateFlow()

    private var _countries: MutableStateFlow<ApiResponse<List<CountryModifiedModel>>> =
        MutableStateFlow(ApiResponse.Loading)
    val countries: StateFlow<ApiResponse<List<CountryModifiedModel>>> = _countries.asStateFlow()

    private var _updateResponse: MutableSharedFlow<ApiResponse<ResponseBody>> =
        MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val updateResponse: Flow<ApiResponse<ResponseBody>>
        get() = _updateResponse.distinctUntilChanged()

    private var _deleteMovieResponse: MutableSharedFlow<ApiResponse<ResponseBody>> =
        MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val deleteMovieResponse: Flow<ApiResponse<ResponseBody>>
        get() = _deleteMovieResponse.distinctUntilChanged()

    private var _deleteSessionResponse: MutableSharedFlow<ApiResponse<ResponseBody>> =
        MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val deleteSessionResponse: Flow<ApiResponse<ResponseBody>>
        get() = _deleteSessionResponse.distinctUntilChanged()

    init {
        getGenresInfo()
        getCountriesInfo()
        getMovieInfo()
    }

    fun getMovieInfo() {
        if (movieId >= 0) {
            viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
                viewModelScope.launch(Dispatchers.Main) {
                    if (error is SocketTimeoutException) {
                        Log.d("asd", "$error")
                        _movie.tryEmit(
                            ApiResponse.Failure(
                                500,
                                "Произошла внутренняя ошибка. Повторите попытку позднее"
                            )
                        )
                    }
                }
            }) {
                repository.getMovieInfo(movieId).collect {
                    _movie.tryEmit(it)
                }
            }
        }
    }

    fun updateMovieInfo(
        name: String,
        posterLink: String,
        imageLink: String,
        description: String,
        createdYear: Long,
        ageLimit: Int,
        genres: List<Genre>,
        countries: List<Country>,
    ) {
        val c: Calendar = Calendar.getInstance()
        c.timeInMillis = createdYear

        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                if (error is SocketTimeoutException) {
                    Log.d("asd", "$error")
                    _movie.tryEmit(
                        ApiResponse.Failure(
                            500,
                            "Произошла внутренняя ошибка. Повторите попытку позднее"
                        )
                    )
                }
            }
        }) {
            repository.updateMovieById(
                movieId,
                name,
                posterLink,
                imageLink,
                description,
                c.get(Calendar.YEAR),
                ageLimit,
                genres,
                countries
            ).collect {
                _updateResponse.tryEmit(it)
            }
        }
    }

    fun addMovieInfo(
        name: String,
        posterLink: String,
        imageLink: String,
        description: String,
        createdYear: Long,
        ageLimit: Int,
        genres: List<Genre>,
        countries: List<Country>,
    ) {
        val c: Calendar = Calendar.getInstance()
        c.timeInMillis = createdYear

        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                if (error is SocketTimeoutException) {
                    Log.d("asd", "$error")
                    _movie.tryEmit(
                        ApiResponse.Failure(
                            500,
                            "Произошла внутренняя ошибка. Повторите попытку позднее"
                        )
                    )
                }
            }
        }) {
            repository.addMovie(
                name,
                posterLink,
                imageLink,
                description,
                c.get(Calendar.YEAR),
                ageLimit,
                genres,
                countries
            ).collect {
                _updateResponse.tryEmit(it)
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

    fun getGenresInfo() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                if (error is SocketTimeoutException) {
                    Log.d("asd", "$error")
                    _genres.tryEmit(
                        ApiResponse.Failure(
                            500,
                            "Произошла внутренняя ошибка. Повторите попытку позднее"
                        )
                    )
                }
            }
        }) {
            repository.getGenres().collect {
                _genres.tryEmit(it)
            }
        }
    }

    fun getCountriesInfo() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                if (error is SocketTimeoutException) {
                    Log.d("asd", "$error")
                    _countries.tryEmit(
                        ApiResponse.Failure(
                            500,
                            "Произошла внутренняя ошибка. Повторите попытку позднее"
                        )
                    )
                }
            }
        }) {
            repository.getCountries().collect {
                _countries.tryEmit(it)
            }
        }
    }

    fun deleteMovie() {
        if (movieId > 0) {
            viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
                viewModelScope.launch(Dispatchers.Main) {
                    if (error is SocketTimeoutException) {
                        Log.d("asd", "$error")
                        _deleteMovieResponse.tryEmit(
                            ApiResponse.Failure(
                                500,
                                "Произошла внутренняя ошибка. Повторите попытку позднее"
                            )
                        )
                    }
                }
            }) {
                repository.deleteMovie(movieId).collect {
                    _deleteMovieResponse.tryEmit(it)
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)

                val repository = application.adminModule.adminRepository

                val savedStateHandle = createSavedStateHandle()

                EditMovieViewModel(repository = repository, savedStateHandle)
            }
        }
    }
}


