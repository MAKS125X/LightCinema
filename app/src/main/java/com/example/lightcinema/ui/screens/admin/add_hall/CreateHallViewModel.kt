package com.example.lightcinema.ui.screens.admin.add_hall

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lightcinema.data.admin.repository.AdminRepository
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.di.MyApplication
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.net.SocketTimeoutException

class CreateHallViewModel(private val repository: AdminRepository) : ViewModel() {

    private var _creatingResponse: MutableSharedFlow<ApiResponse<ResponseBody>> =
        MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val creatingResponse: Flow<ApiResponse<ResponseBody>>
        get() = _creatingResponse.distinctUntilChanged()




    fun createHall(hallNumber: Int, seatModelCollection: SeatsModelCollection) {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                if (error is SocketTimeoutException) {
                    Log.d("asd", "$error")
                    _creatingResponse.tryEmit(
                        ApiResponse.Failure(
                            500,
                            "Произошла внутренняя ошибка. Повторите попытку позднее"
                        )
                    )
                }
            }
        }) {
            repository.addHall(hallNumber, seatModelCollection).collect {
                _creatingResponse.tryEmit(it)
            }
        }
    }




    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)

                val repository = application.adminModule.adminRepository

                CreateHallViewModel(repository = repository)
            }
        }
    }
}