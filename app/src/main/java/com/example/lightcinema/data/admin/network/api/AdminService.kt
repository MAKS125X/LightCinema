package com.example.lightcinema.data.admin.network.api

import com.example.lightcinema.data.admin.network.requests.HallRequest
import com.example.lightcinema.data.admin.network.requests.MovieUpdateRequest
import com.example.lightcinema.data.admin.network.requests.SessionRequest
import com.example.lightcinema.data.admin.network.responses.AllCountriesResponse
import com.example.lightcinema.data.admin.network.responses.AllGenresResponse
import com.example.lightcinema.data.admin.network.responses.HallCollectionResponse
import com.example.lightcinema.data.admin.network.responses.MovieCollectionResponse
import com.example.lightcinema.data.share.MovieLongResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AdminService {

    @GET("/Halls")
    suspend fun getHalls(): Response<HallCollectionResponse>

    @POST("/Halls")
    suspend fun addHall(
        @Body hall: HallRequest
    ): Response<ResponseBody>

    @DELETE("/Sessions/{id}")
    suspend fun deleteSessionById(
        @Path("id") id: Int
    ): Response<ResponseBody>

    @POST("/Sessions")
    suspend fun addSession(
        @Body session: SessionRequest
    ): Response<ResponseBody>

    @GET("/Movies")
    suspend fun getMoviesByDay(
        @Query("date") date: String,
        @Query("withSessions") withSessions: Boolean = true,
    ): Response<MovieCollectionResponse>

    @DELETE("/Movies/{id}")
    suspend fun deleteMovieById(
        @Path("id") id: Int,
    ): Response<ResponseBody>

    @GET("/Movies/genres")
    suspend fun getGenres(): Response<AllGenresResponse>

    @GET("/Movies/countries")
    suspend fun getCountries(): Response<AllCountriesResponse>

    @GET("/Movies/{id}")
    suspend fun getMovieById(
        @Path("id") id: Int
    ): Response<MovieLongResponse>

    @PUT("/Movies/{id}")
    suspend fun updateMovieById(
        @Path("id") id: Int,
        @Body movie: MovieUpdateRequest
    ): Response<ResponseBody>

    @POST("/Movies")
    suspend fun addMovie(
        @Body movie: MovieUpdateRequest
    ): Response<ResponseBody>

    @DELETE("/Halls/{number}")
    suspend fun deleteHallById(
        @Path("number") number: Int
    ): Response<ResponseBody>
}