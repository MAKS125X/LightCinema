package com.example.lightcinema.data.auth.repository

import com.example.lightcinema.data.auth.models.User
import com.example.lightcinema.data.auth.models.UserRequest
import com.example.lightcinema.data.auth.models.UserResponse
import com.example.lightcinema.data.auth.models.UserRole
import com.example.lightcinema.data.auth.network.AuthService
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.common.apiRequestFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryNetwork(
    override val remoteDataSource: AuthService,
    ) : AuthRepository {

    override suspend fun login(login: String, password: String): Flow<ApiResponse<User>> =
        apiRequestFlow {
            remoteDataSource.login(UserRequest(login, password))
        }.toUser(login)

    override suspend fun register(
        login: String,
        password: String
    ): Flow<ApiResponse<User>> {
        return apiRequestFlow {
            remoteDataSource.login(UserRequest(login, password))
        }.toUser(login)
    }

}

fun UserResponse.toUser(nickname: String): User {
    return User(
        this.accessToken,
        nickname,
        if (this.role == UserRole.Admin.name)
            UserRole.Admin
        else
            UserRole.Visitor
    )
}

fun Flow<ApiResponse<UserResponse>>.toUser(nickname: String): Flow<ApiResponse<User>> =
    map { value ->
        when (value) {
            is ApiResponse.Failure -> return@map value
            is ApiResponse.Loading -> return@map value
            is ApiResponse.Success -> return@map ApiResponse.Success<User>(
                value.data.toUser(nickname)
            )
        }
    }