package com.linca.tapsleep.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linca.tapsleep.ui.theme.Dusk
import com.linca.tapsleep.ui.theme.Lavender
import com.linca.tapsleep.ui.theme.Moon
import com.linca.tapsleep.ui.theme.MoonGlow

// ─── Moon Mark ───────────────────────────────────────────────────────────────
// SVG path from TapSleep brand identity (64×64 viewBox):
// M38 12 C27.5 12 19 20.5 19 31 C19 41.5 27.5 50 38 50
//        C32 46 27 39 27 31 C27 23 32 16 38 12 Z

private val moonPath = Path().apply {
    // M38 12
    moveTo(38f, 12f)
    // C27.5 12 19 20.5 19 31
    cubicTo(27.5f, 12f, 19f, 20.5f, 19f, 31f)
    // C19 41.5 27.5 50 38 50
    cubicTo(19f, 41.5f, 27.5f, 50f, 38f, 50f)
    // C32 46 27 39 27 31
    cubicTo(32f, 46f, 27f, 39f, 27f, 31f)
    // C27 23 32 16 38 12
    cubicTo(27f, 23f, 32f, 16f, 38f, 12f)
    close()
}

private fun DrawScope.drawMoon(brush: Brush, sizePx: Float) {
    val scaleFactor = sizePx / 64f
    scale(scaleFactor, pivot = Offset.Zero) {
        drawPath(moonPath, brush)
    }
}

@Composable
fun TapSleepLogoMark(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    // Gradient from Lavender → Dusk, matching the brand identity glow
    startColor: Color = Lavender,
    endColor: Color = Dusk,
) {
    Canvas(modifier = modifier.size(size)) {
        val brush = Brush.linearGradient(
            colors = listOf(startColor, endColor),
            start = Offset(this.size.width * 0.3f, 0f),
            end = Offset(this.size.width * 0.7f, this.size.height),
        )
        drawMoon(brush, this.size.width)
    }
}

// ─── Wordmark ─────────────────────────────────────────────────────────────────
// "Tap" in Moon white + "Sleep" in MoonGlow italic — brand identity spec

@Composable
fun TapSleepWordmark(
    modifier: Modifier = Modifier,
    fontSize: Int = 36,
    tapColor: Color = Moon,
    sleepColor: Color = MoonGlow,
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(SpanStyle(color = tapColor)) {
                append("Tap")
            }
            withStyle(SpanStyle(color = sleepColor, fontStyle = FontStyle.Italic)) {
                append("Sleep")
            }
        },
        style = MaterialTheme.typography.displaySmall.copy(fontSize = fontSize.sp),
    )
}

// ─── Full horizontal lockup ───────────────────────────────────────────────────

@Composable
fun TapSleepLogo(
    modifier: Modifier = Modifier,
    iconSize: Dp = 40.dp,
    fontSize: Int = 36,
    tapColor: Color = Moon,
    sleepColor: Color = MoonGlow,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TapSleepLogoMark(size = iconSize)
        Spacer(Modifier.width(12.dp))
        TapSleepWordmark(
            fontSize = fontSize,
            tapColor = tapColor,
            sleepColor = sleepColor,
        )
    }
}

// ─── Stacked lockup (icon over wordmark + tagline) ────────────────────────────

@Composable
fun TapSleepLogoStacked(
    modifier: Modifier = Modifier,
    iconSize: Dp = 64.dp,
    showTagline: Boolean = true,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TapSleepLogoMark(size = iconSize)
        Text(
            text = "TAPSLEEP",
            style = MaterialTheme.typography.headlineMedium.copy(
                letterSpacing = 6.sp,
                fontStyle = FontStyle.Normal,
            ),
            color = Moon,
        )
        if (showTagline) {
            Text(
                text = "sleep sounds",
                style = MaterialTheme.typography.labelMedium,
                color = Dusk,
            )
        }
    }
}
