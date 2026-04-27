package com.example.personalexpensestracker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


private val LightColorScheme = lightColorScheme(
    primary          = Blue600,
    onPrimary        = White,
    primaryContainer = Blue50,
    onPrimaryContainer = Blue800,

    secondary        = Green600,
    onSecondary      = White,
    secondaryContainer = Green50,
    onSecondaryContainer = Green800,

    error            = Red600,
    onError          = White,
    errorContainer   = Red50,
    onErrorContainer = Red800,

    background       = Background,
    onBackground     = Gray900,

    surface          = Surface,
    onSurface        = Gray900,
    surfaceVariant   = Gray50,
    onSurfaceVariant = Gray600,

    outline          = Gray200,
)

private val DarkColorScheme = darkColorScheme(
    primary          = Blue200,
    onPrimary        = Blue900,
    primaryContainer = Blue800,
    onPrimaryContainer = Blue50,

    secondary        = Green100,
    onSecondary      = Green800,
    secondaryContainer = Green600,
    onSecondaryContainer = Green50,

    error            = Red100,
    onError          = Red800,
    errorContainer   = Red600,
    onErrorContainer = Red50,

    background       = DarkBackground,
    onBackground     = Gray50,

    surface          = DarkSurface,
    onSurface        = Gray50,
    surfaceVariant   = DarkCard,
    onSurfaceVariant = Gray200,

    outline          = Gray800,
)


@Composable
fun PersonalExpensesTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}