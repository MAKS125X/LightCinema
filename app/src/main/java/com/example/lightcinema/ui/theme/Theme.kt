package com.example.lightcinema.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    secondary = SkyBlue,
    tertiary = LightBlue,
    tertiaryContainer = Gray,
    background = Color.White,
    surface = Color.White,
    surfaceTint = LightGray,
    error = Red,

    onPrimary = Color.White,
    onSecondary = Blue,
    onTertiary = Blue,
    onTertiaryContainer = Blue,
    onBackground = Blue,
    onSurface = Color.Black,
    onError = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary = Red,
    secondary = SkyBlue,
    tertiary = LightBlue,
    tertiaryContainer = Gray,
    background = Color.White,
    surface = Color.White,
    surfaceTint = LightGray,
    error = Red,

    onPrimary = Color.White,
    onSecondary = Blue,
    onTertiary = Blue,
    onTertiaryContainer = Blue,
    onBackground = Blue,
    onSurface = Color.Black,
    onError = Color.White,
)


@Composable
fun LightCinemaTheme(
    darkTheme: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = LightCinemaTypography,
        content = content
    )
}