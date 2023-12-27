package com.example.lightcinema.ui.screens.admin.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lightcinema.data.admin.network.responses.HallCollectionResponse
import com.example.lightcinema.data.admin.repository.AdminRepository
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.di.MyApplication
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.net.SocketTimeoutException

class ProfileViewModel(private val repository: AdminRepository) : ViewModel() {

    private val _hallList: MutableStateFlow<ApiResponse<HallCollectionResponse>> =
        MutableStateFlow(ApiResponse.Loading)
    val hallList: StateFlow<ApiResponse<HallCollectionResponse>> = _hallList.asStateFlow()

    private var _deletingResponse: MutableSharedFlow<ApiResponse<ResponseBody>> =
        MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val deletingResponse: Flow<ApiResponse<ResponseBody>>
        get() = _deletingResponse.distinctUntilChanged()

    init {
        updateHallsList()
    }

    fun updateHallsList() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                if (error is SocketTimeoutException) {
                    Log.d("asd", "${error}")
                    _hallList.value = ApiResponse.Failure(
                        500,
                        "Отсутствие подключения к сети"
                    )
                }
            }
        }) {
            repository.getHalls().collect {
                _hallList.value = it
            }
        }
    }

    fun deleteHall(hallNumber: Int) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                if (error is SocketTimeoutException) {
                    Log.d("asd", "$error")
                    _deletingResponse.tryEmit(
                        ApiResponse.Failure(
                            500,
                            "Произошла внутренняя ошибка. Повторите попытку позднее"
                        )
                    )
                }
            }
        }) {
            repository.deleteHall(hallNumber).collect {
                _deletingResponse.tryEmit(it)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)

                val repository = application.adminModule.adminRepository

                ProfileViewModel(repository = repository)
            }
        }
    }
}