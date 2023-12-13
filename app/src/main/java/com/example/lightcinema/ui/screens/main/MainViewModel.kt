package com.example.lightcinema.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lightcinema.data.auth.models.TokenManager
import com.example.lightcinema.data.auth.models.User
import com.example.lightcinema.di.MyApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val _userResult = MutableStateFlow(tokenManager.getToken())
    val userResult: StateFlow<User?> = _userResult.asStateFlow()

    fun logout() {
        tokenManager.deleteToken()
    }

    fun getName(): String {
        return userResult.value?.userName ?: "Аноним"
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)

                val manager = application.tokenManager

                MainViewModel(manager)
            }
        }
    }
}