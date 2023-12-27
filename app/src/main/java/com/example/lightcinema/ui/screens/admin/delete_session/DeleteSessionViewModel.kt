package com.example.lightcinema.ui.screens.admin.delete_session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lightcinema.data.admin.repository.AdminRepository
import com.example.lightcinema.di.MyApplication

class DeleteSessionViewModel(private val repository: AdminRepository) : ViewModel() {


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)

                val repository = application.adminModule.adminRepository

                DeleteSessionViewModel(repository = repository)
            }
        }
    }
}