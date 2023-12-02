package com.example.lightcinema.ui.screens.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lightcinema.data.ApiResponse
import com.example.lightcinema.data.common.models.User
import com.example.lightcinema.data.common.repository.AuthRepository
import com.example.lightcinema.data.visitor.network.responses.UserResponse
import com.example.lightcinema.di.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> = _email

    private val _loginPassword = MutableStateFlow<String>("")
    val loginPassword: StateFlow<String> = _loginPassword

    private val _registerPassword = MutableStateFlow<String>("")
    val registerPassword: StateFlow<String> = _registerPassword

    private val _registerRepeatPassword = MutableStateFlow<String>("")
    val registerRepeatPassword: StateFlow<String> = _registerRepeatPassword


    private val _token = MutableStateFlow<User?>(repository.tokenManager.getToken())
    val token: StateFlow<User?> = _token


    private val _registerResult = MutableStateFlow<ApiResponse<UserResponse>>(ApiResponse.Loading)
    val registerResult: StateFlow<ApiResponse<UserResponse>> = _registerResult

    fun setEmail(email: String) {
        _email.value = email
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


    fun saveToken() {

    }

    fun register() {
        val currentEmail = email.value
        val currentPassword = registerPassword.value
        if (currentEmail != null && currentPassword != null) {

            viewModelScope.launch(Dispatchers.IO) {
                repository.register(currentEmail, currentPassword).collect { result ->

                    _registerResult.value = result

//                    when (result) {
//                        is ApiResponse.Success -> repository.tokenManager.saveToken(result.data)
//                        is ApiResponse.Loading -> {}
//                        is ApiResponse.Failure -> {}
//                    }
                }
            }


        }
    }

    fun login() {

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