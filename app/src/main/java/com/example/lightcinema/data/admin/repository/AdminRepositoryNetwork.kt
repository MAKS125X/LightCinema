package com.example.lightcinema.data.admin.repository

import android.util.Log
import com.example.lightcinema.data.admin.mappers.AllCountriesMapper
import com.example.lightcinema.data.admin.mappers.AllGenresMapper
import com.example.lightcinema.data.admin.network.api.AdminService
import com.example.lightcinema.data.admin.network.requests.HallRequest
import com.example.lightcinema.data.admin.network.requests.MovieUpdateRequest
import com.example.lightcinema.data.admin.network.requests.SeatRequest
import com.example.lightcinema.data.admin.network.requests.SessionRequest
import com.example.lightcinema.data.admin.network.responses.HallCollectionResponse
import com.example.lightcinema.data.admin.network.responses.MovieCollectionResponse
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.common.apiRequestFlow
import com.example.lightcinema.data.common.toModel
import com.example.lightcinema.data.share.MovieMapper
import com.example.lightcinema.ui.models.Country
import com.example.lightcinema.ui.models.Genre
import com.example.lightcinema.ui.models.MovieModel
import com.example.lightcinema.ui.screens.admin.add_hall.SeatsModelCollection
import com.example.lightcinema.ui.screens.admin.edit_movie.CountryModifiedModel
import com.example.lightcinema.ui.screens.admin.edit_movie.GenreModifiedModel
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

class AdminRepositoryNetwork(override val remoteDataSource: AdminService) : AdminRepository {
    override suspend fun getMoviesByDay(
        date: String,
        withSession: Boolean
    ): Flow<ApiResponse<MovieCollectionResponse>> =
        apiRequestFlow { remoteDataSource.getMoviesByDay(date) }

    override suspend fun getHalls(): Flow<ApiResponse<HallCollectionResponse>> =
        apiRequestFlow { remoteDataSource.getHalls() }

    override suspend fun addSession(
        movieId: Int,
        dateTime: String,
        hallNumber: Int,
        price: Int,
        increasedPrice: Int
    ): Flow<ApiResponse<ResponseBody>> = apiRequestFlow {
        val hall = SessionRequest(
            movieId,
            dateTime,
            hallNumber,
            price,
            increasedPrice
        )
        Log.d("AbobaHall", hall.toString())
        remoteDataSource.addSession(
            hall
        )
    }

    override suspend fun getGenres(): Flow<ApiResponse<List<GenreModifiedModel>>> =
        apiRequestFlow {
            remoteDataSource.getGenres()
        }.toModel(AllGenresMapper)

    override suspend fun getCountries(): Flow<ApiResponse<List<CountryModifiedModel>>> =
        apiRequestFlow {
            remoteDataSource.getCountries()
        }.toModel(AllCountriesMapper)

    override suspend fun getMovieInfo(
        id: Int
    ): Flow<ApiResponse<MovieModel>> =
        apiRequestFlow { remoteDataSource.getMovieById(id) }.toModel(MovieMapper)

    override suspend fun updateMovieById(
        id: Int,
        name: String,
        posterLink: String,
        imageLink: String,
        description: String,
        createdYear: Int,
        ageLimit: Int,
        genres: List<Genre>,
        countries: List<Country>,
    ): Flow<ApiResponse<ResponseBody>> = apiRequestFlow {
        remoteDataSource.updateMovieById(
            id,
            MovieUpdateRequest(
                name,
                description,
                imageLink,
                posterLink,
                createdYear,
                ageLimit,
                genres.map { it.id },
                countries.map { it.id })
        )
    }

    override suspend fun addMovie(
        name: String,
        posterLink: String,
        imageLink: String,
        description: String,
        createdYear: Int,
        ageLimit: Int,
        genres: List<Genre>,
        countries: List<Country>
    ): Flow<ApiResponse<ResponseBody>> = apiRequestFlow {
        val model = MovieUpdateRequest(
            name,
            description,
            imageLink,
            posterLink,
            createdYear,
            ageLimit,
            genres.map { it.id },
            countries.map { it.id })
        Log.d("Aboba123", model.toString())
        remoteDataSource.addMovie(
            model
        )
    }

    override suspend fun addHall(
        hallNumber: Int,
        seatModelCollection: SeatsModelCollection
    ): Flow<ApiResponse<ResponseBody>> = apiRequestFlow {

        remoteDataSource.addHall(
            HallRequest(
                hallNumber,
                seatModelCollection.seats.flatten()
                    .map { SeatRequest(it.row, it.number, it.isIncreasedPrice.value) })
        )
    }

    override suspend fun deleteHall(hallNumber: Int): Flow<ApiResponse<ResponseBody>> =
        apiRequestFlow {
            remoteDataSource.deleteHallById(hallNumber)
        }

    override suspend fun deleteMovie(movieId: Int): Flow<ApiResponse<ResponseBody>> =
        apiRequestFlow { remoteDataSource.deleteMovieById(movieId) }

    override suspend fun deleteSession(sessionId: Int): Flow<ApiResponse<ResponseBody>> =
        apiRequestFlow {
            remoteDataSource.deleteSessionById(sessionId)
        }

}