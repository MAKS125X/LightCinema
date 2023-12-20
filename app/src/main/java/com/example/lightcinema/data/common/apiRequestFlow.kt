package com.example.lightcinema.data.common

import com.example.lightcinema.data.mappers.Mapper
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Response

fun <T> apiRequestFlow(call: suspend () -> Response<T>): Flow<ApiResponse<T>> = flow {
    emit(ApiResponse.Loading)

    withTimeoutOrNull(20000L) {
        val response = call()
        try {
            if (response.isSuccessful) {
                response.body()?.let { data ->
                    emit(ApiResponse.Success(data))
                }
            } else {
                response.errorBody()?.let { error ->
                    error.close()
                    val parsedError: ErrorResponse =
                        Gson().fromJson(error.charStream(), ErrorResponse::class.java)
                    emit(ApiResponse.Failure(parsedError.statusCode, parsedError.errorMessage))
                }
                //TODO: Привязаться к коду
            }
        } catch (e: Exception) {
            emit(ApiResponse.Failure(400, e.message ?: e.toString()))
        }
    } ?: emit(ApiResponse.Failure(408, "Timeout! Please try again."))
}.flowOn(Dispatchers.IO)

fun <ResponseType : Any, ModelType : Any> Flow<ApiResponse<ResponseType>>.toModel(mapper: Mapper<ResponseType, ModelType>): Flow<ApiResponse<ModelType>> =
    map { value ->
        when (value) {
            is ApiResponse.Failure -> return@map value
            is ApiResponse.Loading -> return@map value
            is ApiResponse.Success -> return@map ApiResponse.Success<ModelType>(
                mapper.toModel(
                    value.data
                )
            )
        }
    }

//fun <ItemResponseType : Any, ItemModelType : Any> Flow<ApiResponse<List<ItemResponseType>>>.toModel(
//    mapper: Mapper<ItemResponseType, ItemModelType>
//): Flow<ApiResponse<List<ItemModelType>>> =
//    map { value ->
//        when (val a = value) {
//            is ApiResponse.Failure -> return@map a
//            is ApiResponse.Loading -> return@map a
//            is ApiResponse.Success -> return@map ApiResponse.Success<List<ItemModelType>>(
//                a.data.map {
//                    mapper.toModel(
//                        it
//                    )
//                }
//            )
//        }
//    }