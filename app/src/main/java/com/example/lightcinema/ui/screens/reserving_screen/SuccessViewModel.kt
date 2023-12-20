package com.example.lightcinema.ui.screens.reserving_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.lightcinema.ui.navigation.MainDestinations

class SuccessViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    val successString: String = checkNotNull(savedStateHandle[MainDestinations.SUCCESS])
}