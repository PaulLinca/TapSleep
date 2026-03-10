package com.linca.tapsleep.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linca.tapsleep.ui.theme.Dusk
import com.linca.tapsleep.ui.theme.Moon
import com.linca.tapsleep.ui.theme.MoonGlow
import org.jetbrains.compose.resources.painterResource
import tapsleep.shared.generated.resources.Res
import tapsleep.shared.generated.resources.ic_tapsleep_moon

// ─── Moon Mark ───────────────────────────────────────────────────────────────
// Renders ic_tapsleep_moon.svg from compose resources — single source of truth
// for the crescent shape across the app and launcher icons.

@Composable
fun TapSleepLogoMark(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
) {
    Image(
        painter = painterResource(Res.drawable.ic_tapsleep_moon),
        contentDescription = null,
        modifier = modifier.size(size),
    )
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
        TapSleepWordmark(
            fontSize = fontSize,
            tapColor = tapColor,
            sleepColor = sleepColor,
        )
    }
}

// ─── Stacked lockup (used on splash screen) ───────────────────────────────────

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
        TapSleepWordmark()
        if (showTagline) {
            Text(
                text = "sleep sounds",
                style = MaterialTheme.typography.labelMedium,
                color = Dusk,
            )
        }
    }
}
