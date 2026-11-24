package com.yet.template.resources

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** The brand palette: change these and every screen follows. */
object AppColors {
    val Primary = Color(0xFF3D5AFE)
    val PrimaryDark = Color(0xFF8C9EFF)
}

private val LightColors = lightColorScheme(primary = AppColors.Primary)
private val DarkColors = darkColorScheme(primary = AppColors.PrimaryDark)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
