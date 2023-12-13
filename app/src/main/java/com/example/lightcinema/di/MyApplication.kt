package com.example.lightcinema.di

import android.app.Application
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.lightcinema.data.auth.network.AuthService
import com.example.lightcinema.data.auth.repository.AuthRepository
import com.example.lightcinema.data.auth.repository.AuthRepositoryNetwork
import com.example.lightcinema.data.visitor.VisitorModule
import com.example.lightcinema.data.visitor.VisitorModuleNetwork
import com.example.lightcinema.data.auth.models.AuthInterceptor
import com.example.lightcinema.data.auth.models.TokenManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MyApplication : Application() {


    private lateinit var _visitorModule: VisitorModule
    val visitorModule: VisitorModule
        get() = _visitorModule

    private lateinit var _authRepository: AuthRepository
    val authRepository: AuthRepository
        get() = _authRepository

    private lateinit var _tokenManager: TokenManager
    val tokenManager: TokenManager
        get() = _tokenManager

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

        return AuthRepositoryNetwork(retrofitBuilder)
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
//        const val URL: String = "http://192.168.1.65:5038"
        const val URL: String = "http://10.0.2.2:5038"
    }
}