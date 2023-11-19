package com.example.lightcinema.di

import com.example.lightcinema.data.network.visitor.api.VisitorService
import com.example.lightcinema.data.repository.RemoteDataSource
import com.example.lightcinema.data.repository.VisitorRepository
import retrofit2.Retrofit

const val URL: String = "https://test.com"

class AppContainer {
    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .build()
        .create(VisitorService::class.java)

    private val remoteDataSource = RemoteDataSource(retrofit)

    val visitorRepository = VisitorRepository(remoteDataSource)
}