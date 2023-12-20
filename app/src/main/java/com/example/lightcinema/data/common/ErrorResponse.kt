package com.example.lightcinema.data.common

data class ErrorResponse(
    val statusCode: Int,
    val errorMessage: String,
    val stackTrace: String?
)