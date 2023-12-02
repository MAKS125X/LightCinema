package com.example.lightcinema.data.common.repository

import com.example.lightcinema.data.ApiResponse
import com.example.lightcinema.data.apiRequestFlow
import com.example.lightcinema.data.common.models.TokenManager
import com.example.lightcinema.data.common.network.AuthService
import com.example.lightcinema.data.visitor.network.requests.UserRequest
import com.example.lightcinema.data.visitor.network.responses.UserResponse
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