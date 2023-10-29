package com.example.lightcinema

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.lightcinema.ui.screens.CinemaScreen
import com.example.lightcinema.ui.screens.SeatPreview
import com.example.lightcinema.ui.theme.LightCinemaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LightCinemaTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    CinemaScreen()
                }
            }
        }
    }
}

