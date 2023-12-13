package com.example.lightcinema.data.visitor.network.api

import com.example.lightcinema.data.common.SuccessResponse
import com.example.lightcinema.data.visitor.network.requests.PlacesRequest
import com.example.lightcinema.data.visitor.network.requests.UnreservedSeatsRequest
import com.example.lightcinema.data.visitor.network.responses.MovieCollectionResponse
import com.example.lightcinema.data.visitor.network.responses.MovieLongResponse
import com.example.lightcinema.data.visitor.network.responses.PlaceResponse
import com.example.lightcinema.data.visitor.network.responses.ProfileResponse
import com.example.lightcinema.data.visitor.network.responses.SeatsCollectionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VisitorService {

    //    @Headers("JWT")
    @POST("/Users/profile")
    suspend fun getProfileInfo(
//        @Header("Authorization") token: String
    ): Response<ProfileResponse>

    @GET("/Movies")
    suspend fun getMoviesByDay(
        @Query("date") date: String,
        @Query("withSessions") withSessions: Boolean = true,
    ): Response<MovieCollectionResponse>
//    ): Response<List<MovieResponse>>

    @GET("/Movies/{id}")
    suspend fun getMovieById(
        @Path("id") id: Int
    ): Response<MovieLongResponse>

    @GET("/Sessions/{id}/places")
    suspend fun getPlacesBySession(
        @Path("id") id: Int
    ): Response<List<PlaceResponse>>

    //    @Headers("JWT")
    @POST("/Sessions/reserve")
    suspend fun reservePlaces(
//        @Header("Authorization") token: String,
        @Body placesRequest: PlacesRequest
    ): Response<String>

    //    @Headers("JWT")
    @POST("/Sessions/unreserve")
    suspend fun unreservePlaces(
//        @Header("Authorization") token: String,
        @Body placeRequest: UnreservedSeatsRequest
    ): Response<SuccessResponse>

    @GET("/Sessions/{id}/seats")
    suspend fun getSessionSeats(
        @Path("id") id: Int
    ): Response<SeatsCollectionResponse>


}