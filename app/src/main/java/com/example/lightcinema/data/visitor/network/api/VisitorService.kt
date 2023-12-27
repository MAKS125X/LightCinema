package com.example.lightcinema.data.visitor.network.api

import com.example.lightcinema.data.visitor.network.requests.ReserveRequest
import com.example.lightcinema.data.visitor.network.requests.UnreserveRequest
import com.example.lightcinema.data.visitor.network.responses.MovieCollectionResponse
import com.example.lightcinema.data.share.MovieLongResponse
import com.example.lightcinema.data.visitor.network.responses.ProfileResponse
import com.example.lightcinema.data.visitor.network.responses.SeatsCollectionResponse
import com.example.lightcinema.data.visitor.network.responses.SessionsByMovieResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VisitorService {

    @GET("/Users/profile")
    suspend fun getProfileInfo(
    ): Response<ProfileResponse>

    @GET("/Movies")
    suspend fun getMoviesByDay(
        @Query("date") date: String,
        @Query("withSessions") withSessions: Boolean = true,
    ): Response<MovieCollectionResponse>

    @GET("/Movies/{id}")
    suspend fun getMovieById(
        @Path("id") id: Int
    ): Response<MovieLongResponse>

    @POST("/Sessions/{id}/reserve")
    suspend fun reservePlaces(
        @Path("id") id: Int,
        @Body placesRequest: ReserveRequest
    ): Response<ResponseBody>

    @POST("/Sessions/{id}/unreserve")
    suspend fun unreservePlaces(
        @Path("id") id: Int,
        @Body placeRequest: UnreserveRequest
    ): Response<ResponseBody>

    @GET("/Sessions/{id}")
    suspend fun getAnotherMovieSessions(
        @Path("id") id: Int
    ): Response<SessionsByMovieResponse>

    @GET("/Sessions/{id}/seats")
    suspend fun getSessionSeats(
        @Path("id") id: Int
    ): Response<SeatsCollectionResponse>
}