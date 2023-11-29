package com.example.lightcinema.data.common.network

import com.example.lightcinema.data.visitor.network.requests.UserRequest
import com.example.lightcinema.data.visitor.network.responses.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/login")
    suspend fun login(
        @Body user: UserRequest
    ): Response<UserResponse>

    @POST("/register")
    suspend fun register(
        @Body user: UserRequest
    ): UserResponse
}