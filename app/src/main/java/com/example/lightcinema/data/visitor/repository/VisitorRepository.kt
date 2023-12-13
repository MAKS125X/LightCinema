package com.example.lightcinema.data.visitor.repository

import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.common.SuccessResponse
import com.example.lightcinema.data.visitor.network.api.VisitorService
import com.example.lightcinema.data.visitor.network.responses.MovieCollectionResponse
import com.example.lightcinema.ui.screens.cinemahall.SeatsModelCollection
import com.example.lightcinema.ui.screens.movie_info.MovieModel
import com.example.lightcinema.ui.screens.profile.ProfileModel
import kotlinx.coroutines.flow.Flow

interface VisitorRepository {

    val remoteDataSource: VisitorService

    suspend fun getMovieCollection(
        withSession: Boolean,
        date: String
    ): Flow<ApiResponse<MovieCollectionResponse>>

    suspend fun getMovieInfo(id: Int): Flow<ApiResponse<MovieModel>>

    suspend fun getProfileInfo(): Flow<ApiResponse<ProfileModel>>

    suspend fun unreserveSeatById(seatId: Int): Flow<ApiResponse<SuccessResponse>>
    suspend fun getSessionSeatsById(sessionId: Int): Flow<ApiResponse<SeatsModelCollection>>
}