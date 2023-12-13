package com.example.lightcinema.data.auth.network

import com.example.lightcinema.data.auth.models.UserResponse
import com.example.lightcinema.data.auth.models.UserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/Users/sign-in")
    suspend fun login(
        @Body user: UserRequest
    ): Response<UserResponse>

    @POST("/register")
    suspend fun register(
        @Body user: UserRequest
    ): Response<UserResponse>
}