package com.example.lightcinema.data.network.api

import com.example.lightcinema.data.network.requests.UserRequest
import com.example.lightcinema.data.network.responses.MovieResponse
import com.example.lightcinema.data.network.responses.Place
import com.example.lightcinema.data.network.responses.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("/login")
    suspend fun login(
        @Body user: UserRequest
    ): Response<UserResponse>

    @POST("/login")
    suspend fun register(
        @Body user: UserRequest
    ): UserResponse

    @GET("/movies")
    suspend fun getMoviesInfoByDay(
        @Query("withSessions") withSessions: Boolean = true,
        @Query("date") date: String
    ): Response<List<MovieResponse>>

    @GET("/movies/{id}")
    suspend fun getFilmById(
        @Path("id") id: Int
    ): Response<MovieResponse>

    @GET("/sessions/{id}/places")
    suspend fun getPlacesBySession(
        @Path("id") id: Int
    ): Response<List<Place>>

    @POST("/sessions/reserve")
    suspend fun bookPlaces(
        @Body
    ): Response<String>
}