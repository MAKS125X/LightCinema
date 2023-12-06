package com.example.lightcinema.data.auth.repository

import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.auth.models.TokenManager
import com.example.lightcinema.data.auth.models.UserResponse
import com.example.lightcinema.data.auth.network.AuthService
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val remoteDataSource: AuthService
    val tokenManager: TokenManager

    suspend fun login(login: String, password: String): Flow<ApiResponse<UserResponse>>

    suspend fun register(login: String, password: String): Flow<ApiResponse<UserResponse>>
}