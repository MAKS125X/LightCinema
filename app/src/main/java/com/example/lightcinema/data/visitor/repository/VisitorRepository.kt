package com.example.lightcinema.data.visitor.repository

import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.visitor.network.api.VisitorService
import com.example.lightcinema.data.visitor.network.responses.MovieCollectionResponse
import com.example.lightcinema.data.visitor.network.responses.MovieLongResponse
import com.example.lightcinema.ui.screens.filminfo.MovieModel
import kotlinx.coroutines.flow.Flow

interface VisitorRepository {

    val remoteDataSource: VisitorService

    suspend fun getMovieCollection(withSession: Boolean, date: String): Flow<ApiResponse<MovieCollectionResponse>>

    suspend fun getMovieInfo(id: Int): Flow<ApiResponse<MovieModel>>
}