package com.example.lightcinema.data.visitor.repository

import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.common.SuccessResponse
import com.example.lightcinema.data.common.apiRequestFlow
import com.example.lightcinema.data.common.toModel
import com.example.lightcinema.data.mappers.MovieMapper
import com.example.lightcinema.data.mappers.ProfileMapper
import com.example.lightcinema.data.mappers.SeatMapper
import com.example.lightcinema.data.visitor.network.api.VisitorService
import com.example.lightcinema.data.visitor.network.requests.UnreservedSeatsRequest
import com.example.lightcinema.ui.screens.cinemahall.SeatsModelCollection
import com.example.lightcinema.ui.screens.movie_info.MovieModel
import com.example.lightcinema.ui.screens.profile.ProfileModel
import kotlinx.coroutines.flow.Flow

class VisitorRepositoryNetwork(
    override val remoteDataSource: VisitorService
) : VisitorRepository {

    override suspend fun getMovieCollection(
        withSession: Boolean,
        date: String
    ) = apiRequestFlow { remoteDataSource.getMoviesByDay(date, withSession) }

    override suspend fun getMovieInfo(
        id: Int
    ): Flow<ApiResponse<MovieModel>> =
        apiRequestFlow { remoteDataSource.getMovieById(id) }.toModel(MovieMapper)

    override suspend fun getProfileInfo(): Flow<ApiResponse<ProfileModel>> =
        apiRequestFlow { remoteDataSource.getProfileInfo() }.toModel(ProfileMapper)

    override suspend fun unreserveSeatById(seatId: Int): Flow<ApiResponse<SuccessResponse>> =
        apiRequestFlow { remoteDataSource.unreservePlaces(UnreservedSeatsRequest(seatId)) }

    override suspend fun getSessionSeatsById(sessionId: Int): Flow<ApiResponse<SeatsModelCollection>> =
        apiRequestFlow { remoteDataSource.getSessionSeats(sessionId) }.toModel(SeatMapper)
}


