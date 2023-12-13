package com.example.lightcinema.data.common

sealed class ApiResponse<out T> {
    object Loading : ApiResponse<Nothing>()

    data class Success<out T>(
        val data: T
    ) : ApiResponse<T>()

    data class Failure(
        val code: Int,
        val errorMessage: String?,
    ) : ApiResponse<Nothing>()
}
