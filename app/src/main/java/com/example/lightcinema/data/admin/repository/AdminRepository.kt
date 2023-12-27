package com.example.lightcinema.data.admin.repository

import com.example.lightcinema.data.admin.network.api.AdminService
import com.example.lightcinema.data.admin.network.responses.HallCollectionResponse
import com.example.lightcinema.data.admin.network.responses.MovieCollectionResponse
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.ui.models.Country
import com.example.lightcinema.ui.models.Genre
import com.example.lightcinema.ui.models.MovieModel
import com.example.lightcinema.ui.screens.admin.add_hall.SeatsModelCollection
import com.example.lightcinema.ui.screens.admin.edit_movie.CountryModifiedModel
import com.example.lightcinema.ui.screens.admin.edit_movie.GenreModifiedModel
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface AdminRepository {

    val remoteDataSource: AdminService

    suspend fun getMoviesByDay(
        date: String,
        withSession: Boolean = true,
    ): Flow<ApiResponse<MovieCollectionResponse>>

    suspend fun getHalls(): Flow<ApiResponse<HallCollectionResponse>>

    suspend fun addSession(
        movieId: Int,
        dateTime: String,
        hallNumber: Int,
        price: Int,
        increasedPrice: Int
    ): Flow<ApiResponse<ResponseBody>>

    suspend fun getGenres(
    ): Flow<ApiResponse<List<GenreModifiedModel>>>

    suspend fun getCountries(
    ): Flow<ApiResponse<List<CountryModifiedModel>>>

    suspend fun getMovieInfo(id: Int): Flow<ApiResponse<MovieModel>>

    suspend fun updateMovieById(
        id: Int,
        name: String,
        posterLink: String,
        imageLink: String,
        description: String,
        createdYear: Int,
        ageLimit: Int,
        genres: List<Genre>,
        countries: List<Country>,
    ): Flow<ApiResponse<ResponseBody>>

    suspend fun addMovie(
        name: String,
        posterLink: String,
        imageLink: String,
        description: String,
        createdYear: Int,
        ageLimit: Int,
        genres: List<Genre>,
        countries: List<Country>,
    ): Flow<ApiResponse<ResponseBody>>

    suspend fun addHall(
        hallNumber: Int,
        seatModelCollection: SeatsModelCollection
    ): Flow<ApiResponse<ResponseBody>>

    suspend fun deleteHall(
        hallNumber: Int,
    ): Flow<ApiResponse<ResponseBody>>

    suspend fun deleteMovie(
        movieId: Int,
    ): Flow<ApiResponse<ResponseBody>>

    suspend fun deleteSession(
        sessionId: Int,
    ): Flow<ApiResponse<ResponseBody>>
}