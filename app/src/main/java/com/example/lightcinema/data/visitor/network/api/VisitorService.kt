package com.example.lightcinema.data.visitor.network.api

import com.example.lightcinema.data.visitor.network.requests.PlacesRequest
import com.example.lightcinema.data.visitor.network.responses.MovieCollectionResponse
import com.example.lightcinema.data.visitor.network.responses.MovieLongResponse
import com.example.lightcinema.data.visitor.network.responses.PlaceResponse
import com.example.lightcinema.data.visitor.network.responses.SeatsCollectionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VisitorService {

    //    @Headers("JWT")
    @POST("/profile")
    suspend fun getProfileInfo(
        @Header("Authorization") token: String
    )

    @GET("/movies")
    suspend fun getMoviesByDay(
        @Query("withSessions") withSessions: Boolean = true,
        @Query("date") date: String
    ): Response<MovieCollectionResponse>
//    ): Response<List<MovieResponse>>

    @GET("/movies/{id}")
    suspend fun getMovieById(
        @Path("id") id: Int
    ): Response<MovieLongResponse>

    @GET("/sessions/{id}/places")
    suspend fun getPlacesBySession(
        @Path("id") id: Int
    ): Response<List<PlaceResponse>>

    //    @Headers("JWT")
    @POST("/sessions/reserve")
    suspend fun reservePlaces(
//        @Header("Authorization") token: String,
        @Body placesRequest: PlacesRequest
    ): Response<String>

    //    @Headers("JWT")
    @POST("/sessions/unreserve")
    suspend fun unreservePlaces(
//        @Header("Authorization") token: String,
        @Body placesRequest: PlacesRequest
    ): Response<String>

    @GET("/sessions/{id}/seats")
    suspend fun getSessionSeats(): Response<SeatsCollectionResponse>


}