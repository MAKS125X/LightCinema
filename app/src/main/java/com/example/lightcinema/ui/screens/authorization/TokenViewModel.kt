package com.example.lightcinema.ui.screens.authorization

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lightcinema.data.auth.models.TokenManager
import com.example.lightcinema.data.auth.models.User
import com.example.lightcinema.di.MyApplication

class TokenViewModel(private val tokenManager: TokenManager) : ViewModel() {
    val token = MutableLiveData<User?>()

    init {
        token.value = tokenManager.getToken()
    }

    fun saveToken(token: User) {
        tokenManager.saveToken(token)
    }

    fun deleteToken() {
        tokenManager.deleteToken()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as MyApplication)
                val tokenManager = application.tokenManager
                TokenViewModel(tokenManager)
            }
        }
    }
}