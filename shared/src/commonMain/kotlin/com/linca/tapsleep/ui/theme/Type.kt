package com.linca.tapsleep.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ─── Font Families ───────────────────────────────────────────────────────────
//
// To use the real fonts, download from Google Fonts and place .ttf files in:
//   shared/src/commonMain/composeResources/font/
//
// Required files:
//   cormorant_garamond_light.ttf          (weight 300)
//   cormorant_garamond_light_italic.ttf   (weight 300, italic)
//   cormorant_garamond_regular.ttf        (weight 400)
//   cormorant_garamond_italic.ttf         (weight 400, italic)
//   dm_mono_light.ttf                     (weight 300)
//   dm_mono_regular.ttf                   (weight 400)
//
// Then replace these FontFamily definitions with:
//   val CormorantGaramond = FontFamily(
//       Font(Res.font.cormorant_garamond_light, FontWeight.Light),
//       Font(Res.font.cormorant_garamond_light_italic, FontWeight.Light, FontStyle.Italic),
//       Font(Res.font.cormorant_garamond_regular, FontWeight.Normal),
//       Font(Res.font.cormorant_garamond_italic, FontWeight.Normal, FontStyle.Italic),
//   )
//   val DmMono = FontFamily(
//       Font(Res.font.dm_mono_light, FontWeight.Light),
//       Font(Res.font.dm_mono_regular, FontWeight.Normal),
//   )
//
// Using system fallbacks until font files are added:
val CormorantGaramond = FontFamily.Serif
val DmMono = FontFamily.Monospace

// ─── Typography Scale ─────────────────────────────────────────────────────────
// Display / Hero   → Cormorant Garamond Light — wordmark-scale titles
// Headlines        → Cormorant Garamond Light Italic — section headings
// Titles           → Cormorant Garamond Italic — player / sound names
// Body / Label     → DM Mono Light — all UI metadata, tags, timers, labels

val TapSleepTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Light,
        fontSize = 96.sp,
        letterSpacing = (-1.5).sp,
        lineHeight = 86.sp,
    ),
    displayMedium = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Light,
        fontSize = 60.sp,
        letterSpacing = (-0.5).sp,
        lineHeight = 54.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Light,
        fontSize = 40.sp,
        letterSpacing = 0.sp,
        lineHeight = 44.sp,
    ),

    headlineLarge = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Light,
        fontStyle = FontStyle.Italic,
        fontSize = 32.sp,
        letterSpacing = 0.sp,
        lineHeight = 40.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Light,
        fontSize = 28.sp,
        letterSpacing = 0.sp,
        lineHeight = 36.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Light,
        fontStyle = FontStyle.Italic,
        fontSize = 22.sp,
        letterSpacing = 0.sp,
        lineHeight = 28.sp,
    ),

    titleLarge = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Light,
        fontStyle = FontStyle.Italic,
        fontSize = 22.sp,
        letterSpacing = 0.sp,
        lineHeight = 28.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        letterSpacing = 0.sp,
        lineHeight = 24.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        fontSize = 14.sp,
        letterSpacing = 0.sp,
        lineHeight = 20.sp,
    ),

    bodyLarge = TextStyle(
        fontFamily = DmMono,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        letterSpacing = 0.sp,
        lineHeight = 20.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = DmMono,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        letterSpacing = 0.sp,
        lineHeight = 16.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = DmMono,
        fontWeight = FontWeight.Light,
        fontSize = 9.sp,
        letterSpacing = 0.sp,
        lineHeight = 12.sp,
    ),

    labelLarge = TextStyle(
        fontFamily = DmMono,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        letterSpacing = 2.sp,
        lineHeight = 16.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = DmMono,
        fontWeight = FontWeight.Light,
        fontSize = 9.sp,
        letterSpacing = 2.sp,
        lineHeight = 12.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = DmMono,
        fontWeight = FontWeight.Light,
        fontSize = 8.sp,
        letterSpacing = 1.5.sp,
        lineHeight = 12.sp,
    ),
)
