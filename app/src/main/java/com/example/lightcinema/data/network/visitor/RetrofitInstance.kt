package com.example.lightcinema.data.network.visitor

import com.example.lightcinema.data.network.visitor.api.VisitorService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val URL: String = "https://test.com"

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api: VisitorService by lazy {
        retrofit.create(VisitorService::class.java)
    }
}