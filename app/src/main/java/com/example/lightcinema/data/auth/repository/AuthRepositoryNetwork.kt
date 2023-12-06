package com.example.lightcinema.data.auth.repository

import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.common.apiRequestFlow
import com.example.lightcinema.data.auth.models.TokenManager
import com.example.lightcinema.data.auth.models.UserResponse
import com.example.lightcinema.data.auth.network.AuthService
import com.example.lightcinema.data.auth.models.UserRequest
import kotlinx.coroutines.flow.Flow

class AuthRepositoryNetwork(
    override val remoteDataSource: AuthService,
    override val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(login: String, password: String): Flow<ApiResponse<UserResponse>> =
        apiRequestFlow {
            remoteDataSource.login(UserRequest(login, password))

        }

    override suspend fun register(
        login: String,
        password: String
    ): Flow<ApiResponse<UserResponse>> {
        return apiRequestFlow {
            remoteDataSource.login(UserRequest(login, password))
        }
    }


}