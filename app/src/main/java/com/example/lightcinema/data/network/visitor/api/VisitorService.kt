package com.example.lightcinema.data.network.visitor.api

import com.example.lightcinema.data.network.visitor.requests.PlacesRequest
import com.example.lightcinema.data.network.visitor.requests.UserRequest
import com.example.lightcinema.data.network.visitor.responses.MovieResponse
import com.example.lightcinema.data.network.visitor.responses.Place
import com.example.lightcinema.data.network.visitor.responses.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VisitorService {

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

    @Headers("JWT")
    @POST("/sessions/reserve")
    suspend fun reservePlaces(
        @Body placesRequest: PlacesRequest
    ): Response<String>

    @Headers("JWT")
    @POST("/sessions/unreserve")
    suspend fun unreservePlaces(
        @Body placesRequest: PlacesRequest
    ): Response<String>

    @Headers("JWT")
    @POST("/profile")
    suspend fun getProfileInfo(

    )
}