package com.example.lightcinema.data.admin

import com.example.lightcinema.data.admin.network.api.AdminService
import com.example.lightcinema.data.admin.repository.AdminRepository
import com.example.lightcinema.data.admin.repository.AdminRepositoryNetwork
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AdminModuleNetwork(client: OkHttpClient) : AdminModule {

    private val retrofitBuilder: AdminService = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(AdminService::class.java)

    override val adminRepository: AdminRepository = AdminRepositoryNetwork(retrofitBuilder)

    companion object {
        const val URL: String = "http://10.0.2.2:5038"
    }
}