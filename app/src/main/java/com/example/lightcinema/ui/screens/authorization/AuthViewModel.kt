package com.example.lightcinema.ui.screens.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lightcinema.data.auth.models.User
import com.example.lightcinema.data.auth.repository.AuthRepository
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.di.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _nickname = MutableStateFlow<String>("")
    val nickname: StateFlow<String> = _nickname.asStateFlow()

    private val _loginPassword = MutableStateFlow<String>("")
    val loginPassword: StateFlow<String> = _loginPassword.asStateFlow()

    private val _registerPassword = MutableStateFlow<String>("")
    val registerPassword: StateFlow<String> = _registerPassword.asStateFlow()

    private val _registerRepeatPassword = MutableStateFlow<String>("")
    val registerRepeatPassword: StateFlow<String> = _registerRepeatPassword.asStateFlow()


    //    private val _token = MutableStateFlow<User?>(repository.tokenManager.getToken())
//    val token: StateFlow<User?> = _token.asStateFlow()
    private val _userResult = MutableSharedFlow<ApiResponse<User>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val userResult: Flow<ApiResponse<User>> = _userResult.distinctUntilChanged()

//    private val _registerResponse = MutableStateFlow<ApiResponse<User>>(ApiResponse.Loading)
//    val registerResult: StateFlow<ApiResponse<User>> = _registerResponse.asStateFlow()

    fun setEmail(email: String) {
        _nickname.value = email
    }

    fun setLoginPassword(loginPassword: String) {
        _loginPassword.value = loginPassword
    }

    fun setRegisterPassword(registerPassword: String) {
        _registerPassword.value = registerPassword
    }

    fun setRegisterRepeatPassword(registerRepeatPassword: String) {
        _registerRepeatPassword.value = registerRepeatPassword
    }

    fun register() {
        val currentEmail = nickname.value
        val currentPassword = registerPassword.value
        viewModelScope.launch(Dispatchers.IO) {
            repository.login(currentEmail, currentPassword).collect { result ->
                _userResult.tryEmit(result)
            }
        }
    }

    fun login() {
        val currentEmail = nickname.value
        val currentPassword = loginPassword.value
        viewModelScope.launch(Dispatchers.IO) {
            repository.login(currentEmail, currentPassword).collect { result ->
                _userResult.tryEmit(result)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MyApplication)
                val authRepository = application.authRepository
                AuthViewModel(repository = authRepository)
            }
        }
    }
}