package com.linca.tapsleep.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import tapsleep.shared.generated.resources.Res
import tapsleep.shared.generated.resources.cormorant_garamond_italic
import tapsleep.shared.generated.resources.cormorant_garamond_light
import tapsleep.shared.generated.resources.cormorant_garamond_light_italic
import tapsleep.shared.generated.resources.cormorant_garamond_regular
import tapsleep.shared.generated.resources.dm_mono_light
import tapsleep.shared.generated.resources.dm_mono_regular

@Composable
fun cormorantGaramondFamily() = FontFamily(
    Font(Res.font.cormorant_garamond_light, FontWeight.Light),
    Font(Res.font.cormorant_garamond_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(Res.font.cormorant_garamond_regular, FontWeight.Normal),
    Font(Res.font.cormorant_garamond_italic, FontWeight.Normal, FontStyle.Italic),
)

@Composable
fun dmMonoFamily() = FontFamily(
    Font(Res.font.dm_mono_light, FontWeight.Light),
    Font(Res.font.dm_mono_regular, FontWeight.Normal),
)

@Composable
fun TapSleepTypography(): Typography {
    val cormorant = cormorantGaramondFamily()
    val dmMono = dmMonoFamily()
    return Typography(
    displayLarge = TextStyle(
        fontFamily = cormorant,
        fontWeight = FontWeight.Light,
        fontSize = 96.sp,
        letterSpacing = (-1.5).sp,
        lineHeight = 86.sp,
    ),
    displayMedium = TextStyle(
        fontFamily = cormorant,
        fontWeight = FontWeight.Light,
        fontSize = 60.sp,
        letterSpacing = (-0.5).sp,
        lineHeight = 54.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = cormorant,
        fontWeight = FontWeight.Light,
        fontSize = 40.sp,
        letterSpacing = 0.sp,
        lineHeight = 44.sp,
    ),

    headlineLarge = TextStyle(
        fontFamily = cormorant,
        fontWeight = FontWeight.Light,
        fontStyle = FontStyle.Italic,
        fontSize = 32.sp,
        letterSpacing = 0.sp,
        lineHeight = 40.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = cormorant,
        fontWeight = FontWeight.Light,
        fontSize = 28.sp,
        letterSpacing = 0.sp,
        lineHeight = 36.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = cormorant,
        fontWeight = FontWeight.Light,
        fontStyle = FontStyle.Italic,
        fontSize = 22.sp,
        letterSpacing = 0.sp,
        lineHeight = 28.sp,
    ),

    titleLarge = TextStyle(
        fontFamily = cormorant,
        fontWeight = FontWeight.Light,
        fontStyle = FontStyle.Italic,
        fontSize = 22.sp,
        letterSpacing = 0.sp,
        lineHeight = 28.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = cormorant,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        letterSpacing = 0.sp,
        lineHeight = 24.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = cormorant,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        fontSize = 14.sp,
        letterSpacing = 0.sp,
        lineHeight = 20.sp,
    ),

    bodyLarge = TextStyle(
        fontFamily = dmMono,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        letterSpacing = 0.sp,
        lineHeight = 20.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = dmMono,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        letterSpacing = 0.sp,
        lineHeight = 16.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = dmMono,
        fontWeight = FontWeight.Light,
        fontSize = 9.sp,
        letterSpacing = 0.sp,
        lineHeight = 12.sp,
    ),

    labelLarge = TextStyle(
        fontFamily = dmMono,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        letterSpacing = 2.sp,
        lineHeight = 16.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = dmMono,
        fontWeight = FontWeight.Light,
        fontSize = 9.sp,
        letterSpacing = 2.sp,
        lineHeight = 12.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = dmMono,
        fontWeight = FontWeight.Light,
        fontSize = 8.sp,
        letterSpacing = 1.5.sp,
        lineHeight = 12.sp,
    ),
)
}
