package com.example.metermaster.ui.theme

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

private val LightColors = lightColors(
    primary = Color(0xFF1976D2),
    secondary = Color(0xFF388E3C),
    background = Color(0xFFF2F2F2),
    surface = Color.White
)

private val DarkColors = darkColors(
    primary = Color(0xFF90CAF9),
    secondary = Color(0xFFA5D6A7),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E)
)

@Composable
fun MeterMasterTheme(content: @Composable () -> Unit) {
    // Simple system-following theme; DataStore override is handled in Settings
    val isDark = isSystemInDarkTheme()
    val colors = if (isDark) DarkColors else LightColors
    MaterialTheme(colors = colors) {
        content()
    }
}
