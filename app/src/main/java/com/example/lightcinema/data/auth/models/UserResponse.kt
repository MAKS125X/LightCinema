package com.example.lightcinema.data.auth.models

data class UserResponse(
    val accessToken: String,
    val refreshToken: String,
    val login: String,
    val role: String
)