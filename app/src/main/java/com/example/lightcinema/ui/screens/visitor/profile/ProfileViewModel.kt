package com.example.lightcinema.ui.screens.visitor.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.visitor.repository.VisitorRepository
import com.example.lightcinema.di.MyApplication
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ProfileViewModel(private val visitorRepository: VisitorRepository) : ViewModel() {

    private val _profileInfo: MutableStateFlow<ApiResponse<ProfileModel>> =
        MutableStateFlow(ApiResponse.Loading)
    val profileInfo: StateFlow<ApiResponse<ProfileModel>> = _profileInfo.asStateFlow()

    init {
        updateProfileInfo()
    }

    fun updateProfileInfo() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                if (error is SocketTimeoutException) {
                    Log.d("asd", "${error}")
                    _profileInfo.value = ApiResponse.Failure(500, "Отсутствие подключения к сети")
                }
            }
        }) {
            visitorRepository.getProfileInfo().collect {
                _profileInfo.value = it
            }
        }
    }

    fun unreserve(sessionId: Int, seatId: Int) {
        viewModelScope.launch {
            visitorRepository.unreserveSeatById(sessionId, seatId).collect {
                when (it) {
                    is ApiResponse.Failure -> {}
                    ApiResponse.Loading -> {}
                    is ApiResponse.Success -> updateProfileInfo()
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)

                val repository = application.visitorModule.visitorRepository

                ProfileViewModel(visitorRepository = repository)
            }
        }
    }
}