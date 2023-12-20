package com.example.lightcinema.data.visitor.repository

import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.visitor.network.api.VisitorService
import com.example.lightcinema.data.visitor.network.responses.MovieCollectionResponse
import com.example.lightcinema.ui.screens.movie_info.MovieModel
import com.example.lightcinema.ui.screens.profile.ProfileModel
import com.example.lightcinema.ui.screens.reserving_screen.AnotherSessionModel
import com.example.lightcinema.ui.screens.reserving_screen.SeatsModelCollection
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface VisitorRepository {

    val remoteDataSource: VisitorService

    suspend fun getMovieCollection(
        withSession: Boolean,
        date: String
    ): Flow<ApiResponse<MovieCollectionResponse>>

    suspend fun getMovieInfo(id: Int): Flow<ApiResponse<MovieModel>>

    suspend fun getProfileInfo(): Flow<ApiResponse<ProfileModel>>

    suspend fun unreserveSeatById(sessionId: Int, seatId: Int): Flow<ApiResponse<ResponseBody>>

    suspend fun getSessionSeatsById(sessionId: Int): Flow<ApiResponse<SeatsModelCollection>>

    suspend fun getAnotherMovieSessions(sessionId: Int): Flow<ApiResponse<AnotherSessionModel>>

    suspend fun reserveSeats(sessionId: Int, seats: List<Int>): Flow<ApiResponse<ResponseBody>>
}