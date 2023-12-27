package com.example.lightcinema.ui.screens.admin.movie

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun MovieScreen(
    viewModel: MovieViewModel = viewModel(),
    upPress: () -> Unit
) {
    Box {
        Text(text = "MovieScreen")
    }
}