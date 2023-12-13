package com.example.lightcinema.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.visitor.network.api.VisitorService
import com.example.lightcinema.data.visitor.repository.VisitorRepository
import com.example.lightcinema.data.visitor.repository.VisitorRepositoryMock
import com.example.lightcinema.di.MyApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileViewModel(private val visitorRepository: VisitorRepository) : ViewModel() {

    private val _profileInfo: MutableStateFlow<ApiResponse<ProfileModel>> =
        MutableStateFlow(ApiResponse.Loading)
    val profileInfo: StateFlow<ApiResponse<ProfileModel>> = _profileInfo.asStateFlow()

    init {
        updateProfileInfo()
    }

    fun updateProfileInfo() {
        viewModelScope.launch {
            visitorRepository.getProfileInfo().collect {
                _profileInfo.value = it
            }
        }
    }

    fun unreserve(id: Int) {

    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)

                val repository = application.visitorModule.visitorRepository
//                val repository = VisitorRepositoryMock(
//                    Retrofit.Builder()
//                        .baseUrl(MyApplication.URL)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build()
//                        .create(VisitorService::class.java)
//                )

                ProfileViewModel(visitorRepository = repository)
            }
        }
    }
}