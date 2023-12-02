package com.example.lightcinema

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.lightcinema.ui.screens.authorization.AuthScreen
import com.example.lightcinema.ui.screens.cinemahall.CinemaScreen
import com.example.lightcinema.ui.theme.LightCinemaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LightCinemaTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
//                    CinemaScreen()
                    AuthScreen()
                }
            }
        }
    }
}

