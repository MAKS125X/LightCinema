package com.example.lightcinema.data.visitor.repository

import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.common.apiRequestFlow
import com.example.lightcinema.data.visitor.network.api.VisitorService
import com.example.lightcinema.data.visitor.network.responses.MovieLongResponse
import com.example.lightcinema.ui.mappers.MovieMapper
import com.example.lightcinema.ui.screens.filminfo.MovieModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VisitorRepositoryNetwork(
    override val remoteDataSource: VisitorService
) : VisitorRepository {

    override suspend fun getMovieCollection(
        withSession: Boolean,
        date: String
    ) = apiRequestFlow { remoteDataSource.getMoviesByDay(withSession, date) }

    override suspend fun getMovieInfo(
        id: Int
    ): Flow<ApiResponse<MovieModel>> =
        apiRequestFlow { remoteDataSource.getMovieById(id) }.toModel(MovieMapper())


}

fun Flow<ApiResponse<MovieLongResponse>>.toModel(movieMapper: MovieMapper): Flow<ApiResponse<MovieModel>> =
    map { value ->
        when (val a = value) {
            is ApiResponse.Failure -> return@map a
            is ApiResponse.Loading -> return@map a
            is ApiResponse.Success -> return@map ApiResponse.Success<MovieModel>(
                movieMapper.toModel(
                    a.data
                )
            )
        }
    }
