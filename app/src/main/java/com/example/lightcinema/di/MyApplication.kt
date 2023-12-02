package com.example.lightcinema.di

import android.app.Application
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.lightcinema.data.common.network.AuthService
import com.example.lightcinema.data.common.repository.AuthRepository
import com.example.lightcinema.data.common.repository.AuthRepositoryNetwork
import com.example.lightcinema.data.visitor.VisitorModule
import com.example.lightcinema.data.visitor.VisitorModuleNetwork
import com.example.lightcinema.data.common.models.AuthInterceptor
import com.example.lightcinema.data.common.models.TokenManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MyApplication : Application() {

//    private lateinit var _authManager: AuthManager
//    val authManager: AuthManager
//        get() = _authManager

    private lateinit var _visitorModule: VisitorModule
    val visitorModule: VisitorModule
        get() = _visitorModule

    private lateinit var _authRepository: AuthRepository
    val authRepository: AuthRepository
        get() = _authRepository

    private lateinit var _tokenManager: TokenManager

    override fun onCreate() {
        super.onCreate()

        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            this,
            "token_pref",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        _tokenManager = TokenManager(sharedPreferences)

        val authInterceptor = AuthInterceptor(_tokenManager)
        val okHttpClient = provideOkHttpClient(authInterceptor)

        _authRepository = provideAuthRepository(okHttpClient)

        _visitorModule = provideVisitorModule(okHttpClient)
    }

    private fun provideAuthRepository(okHttpClient: OkHttpClient): AuthRepositoryNetwork {

        val retrofitBuilder: AuthService = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(AuthService::class.java)

        return AuthRepositoryNetwork(retrofitBuilder, _tokenManager)
    }

    private fun provideVisitorModule(okHttpClient: OkHttpClient) =
        VisitorModuleNetwork(okHttpClient)

    private fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
//            .addInterceptor(loggingInterceptor)
            .build()
    }

    companion object {
        const val URL: String = "https://test.com"
    }
}