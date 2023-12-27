package com.example.lightcinema.ui.screens.visitor.reserving

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.lightcinema.ui.navigation.MainDestinations

class SuccessViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    val successString: String = checkNotNull(savedStateHandle[MainDestinations.SUCCESS])
}