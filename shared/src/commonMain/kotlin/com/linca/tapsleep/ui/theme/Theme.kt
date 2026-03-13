package com.linca.tapsleep.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp


private val DarkColorScheme = darkColorScheme(
    primary = Lavender,
    onPrimary = Night,
    primaryContainer = IndigoDark,
    onPrimaryContainer = Moon,
    secondary = Aurora,
    onSecondary = Night,
    secondaryContainer = Indigo,
    onSecondaryContainer = MoonWarm,
    tertiary = Sage,
    onTertiary = Night,
    tertiaryContainer = IndigoMid,
    onTertiaryContainer = MoonGlow,
    background = Night,
    onBackground = Moon,
    surface = Deep,
    onSurface = Moon,
    surfaceVariant = IndigoDark,
    onSurfaceVariant = MoonGlow,
    outline = Dusk,
    outlineVariant = Slate,
    inverseSurface = Cream,
    inverseOnSurface = Midnight,
    inversePrimary = Indigo,
    scrim = Night,
)

private val LightColorScheme = lightColorScheme(
    primary = Indigo,
    onPrimary = Cream,
    primaryContainer = LavenderPale,
    onPrimaryContainer = IndigoDark,
    secondary = Dusk,
    onSecondary = Cream,
    secondaryContainer = AuroraPale,
    onSecondaryContainer = Midnight,
    tertiary = Sage,
    onTertiary = Cream,
    tertiaryContainer = MoonWarm,
    onTertiaryContainer = IndigoDark,
    background = Cream,
    onBackground = Midnight,
    surface = MoonWarm,
    onSurface = Midnight,
    surfaceVariant = Moon,
    onSurfaceVariant = Slate,
    outline = Dusk,
    outlineVariant = MoonGlow,
    inverseSurface = IndigoDark,
    inverseOnSurface = Moon,
    inversePrimary = Lavender,
    scrim = Midnight,
)

private val TapSleepShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

@Composable
fun TapSleepTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = TapSleepTypography,
        shapes = TapSleepShapes,
        content = content,
    )
}
