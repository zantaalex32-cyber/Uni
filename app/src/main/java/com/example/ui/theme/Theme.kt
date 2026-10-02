package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ZantaPrimaryLight,
    onPrimary = Color.White,
    primaryContainer = ZantaPrimaryContainer,
    onPrimaryContainer = ZantaOnPrimaryContainer,
    secondary = ZantaSecondary,
    onSecondary = Color.White,
    secondaryContainer = ZantaDarkSurfaceVariant,
    onSecondaryContainer = ZantaSecondaryContainer,
    tertiary = ZantaAccent,
    background = ZantaDarkBg,
    onBackground = TextPrimaryDark,
    surface = ZantaDarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = ZantaDarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = ZantaDarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = ZantaPrimary,
    onPrimary = Color.White,
    primaryContainer = ZantaPrimaryContainer,
    onPrimaryContainer = ZantaOnPrimaryContainer,
    secondary = ZantaSecondary,
    onSecondary = Color.White,
    secondaryContainer = ZantaSecondaryContainer,
    onSecondaryContainer = ZantaOnSecondaryContainer,
    tertiary = ZantaAccent,
    background = ZantaLightBg,
    onBackground = TextPrimaryLight,
    surface = ZantaLightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = ZantaLightSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    outline = ZantaLightOutline
)

@Composable
fun ZantaCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent branding colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) = ZantaCampusTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)


