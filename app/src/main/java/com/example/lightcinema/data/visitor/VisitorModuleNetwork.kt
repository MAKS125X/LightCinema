package com.example.lightcinema.data.visitor

import com.example.lightcinema.data.visitor.network.api.VisitorService
import com.example.lightcinema.data.visitor.repository.VisitorRepository
import com.example.lightcinema.data.visitor.repository.VisitorRepositoryNetwork
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class VisitorModuleNetwork(client: OkHttpClient) : VisitorModule {

    private val retrofitBuilder: VisitorService = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(VisitorService::class.java)

    override val visitorRepository: VisitorRepository = VisitorRepositoryNetwork(retrofitBuilder)

    companion object {
        const val URL: String = "http://10.0.2.2:5038"
    }
}