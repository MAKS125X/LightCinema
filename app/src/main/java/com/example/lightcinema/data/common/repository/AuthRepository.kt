package com.example.lightcinema.data.common.repository

import com.example.lightcinema.data.ApiResponse
import com.example.lightcinema.data.common.models.TokenManager
import com.example.lightcinema.data.common.network.AuthService
import com.example.lightcinema.data.visitor.network.api.VisitorService
import com.example.lightcinema.data.visitor.network.responses.UserResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val remoteDataSource: AuthService
    val tokenManager: TokenManager

    suspend fun login(login: String, password: String): Flow<ApiResponse<UserResponse>>

    suspend fun register(login: String, password: String): Flow<ApiResponse<UserResponse>>
}