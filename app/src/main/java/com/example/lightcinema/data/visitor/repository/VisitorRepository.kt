package com.example.lightcinema.data.visitor.repository

import com.example.lightcinema.data.ApiResponse
import com.example.lightcinema.data.visitor.network.api.VisitorService
import com.example.lightcinema.data.visitor.network.responses.UserResponse
import com.example.lightcinema.ui.models.Seat
import kotlinx.coroutines.flow.Flow

interface VisitorRepository {

    val remoteDataSource: VisitorService
//
//    suspend fun login(login: String, password: String): Flow<ApiResponse<UserResponse>>
//
//    suspend fun register(login: String, password: String): Flow<ApiResponse<UserResponse>>
}