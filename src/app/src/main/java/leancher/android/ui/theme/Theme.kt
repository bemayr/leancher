package leancher.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun LeancherTheme(
        content: @Composable () -> Unit,
        darkTheme: Boolean = isSystemInDarkTheme()
) {
    MaterialTheme(
            colors = if (darkTheme) DarkColors else LightColors,
            typography = LeancherTypography,
            content = content
    )
}