package com.example.lightcinema.data.visitor.repository

import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.common.apiRequestFlow
import com.example.lightcinema.data.common.toModel
import com.example.lightcinema.data.mappers.AnotherSessionsMapper
import com.example.lightcinema.data.mappers.MovieMapper
import com.example.lightcinema.data.mappers.ProfileMapper
import com.example.lightcinema.data.mappers.SeatMapper
import com.example.lightcinema.data.visitor.network.api.VisitorService
import com.example.lightcinema.data.visitor.network.requests.ReserveRequest
import com.example.lightcinema.data.visitor.network.requests.UnreserveRequest
import com.example.lightcinema.ui.screens.visitor.movie_info.MovieModel
import com.example.lightcinema.ui.screens.visitor.profile.ProfileModel
import com.example.lightcinema.ui.screens.visitor.reserving_screen.AnotherSessionModel
import com.example.lightcinema.ui.screens.visitor.reserving_screen.SeatsModelCollection
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

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

    override suspend fun unreserveSeatById(
        sessionId: Int,
        seatId: Int
    ): Flow<ApiResponse<ResponseBody>> =
        apiRequestFlow {
            remoteDataSource.unreservePlaces(
                sessionId,
                UnreserveRequest(seatId)
            )
        }

    override suspend fun getSessionSeatsById(sessionId: Int): Flow<ApiResponse<SeatsModelCollection>> =
        apiRequestFlow { remoteDataSource.getSessionSeats(sessionId) }.toModel(SeatMapper)

    override suspend fun getAnotherMovieSessions(sessionId: Int): Flow<ApiResponse<AnotherSessionModel>> =
        apiRequestFlow { remoteDataSource.getAnotherMovieSessions(sessionId) }
            .toModel(AnotherSessionsMapper)

    override suspend fun reserveSeats(
        sessionId: Int,
        seats: List<Int>
    ): Flow<ApiResponse<ResponseBody>> =
        apiRequestFlow { remoteDataSource.reservePlaces(sessionId, ReserveRequest(seats)) }
}


