package com.linca.tapsleep.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linca.tapsleep.ui.theme.Aurora
import com.linca.tapsleep.ui.theme.Dusk
import com.linca.tapsleep.ui.theme.MoonGlow

private val privacySections = listOf(
    "Information We Collect" to
            "TapSleep does not collect personal information.",
    "Data Usage" to
            "The app does not require account creation, login, or internet access to function.",
    "Data Storage" to
            "Any sound preferences or settings are stored locally on your device. No data is transmitted to external servers.",
    "Third-Party Services" to
            "TapSleep does not use analytics services, advertising networks, or third-party tracking tools.",
    "Children's Privacy" to
            "TapSleep is suitable for all ages. No personal information is collected from children.",
)

private const val contactEmail = "vybes.apps@gmail.com"

@Composable
fun InfoScreen(onBack: () -> Unit, onReviewClick: () -> Unit) {
    val openUrl = rememberUrlOpener()

    Box(modifier = Modifier.fillMaxSize()) {
        StarfieldBackground(Modifier.fillMaxSize())
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(12.dp))
            BackButton(onClick = onBack)
            Spacer(Modifier.height(36.dp))

            // ── Review section ────────────────────────────────────────────────
            Text(
                "We're glad you're here.",
                style = MaterialTheme.typography.headlineLarge,
                color = MoonGlow,
                fontSize = 32.sp,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "If TapSleep is helping you sleep, a review helps others find rest too. It means more than you know.",
                style = MaterialTheme.typography.bodyMedium,
                color = MoonGlow.copy(alpha = 0.70f),
                lineHeight = 22.sp,
            )
            Spacer(Modifier.height(24.dp))
            ReviewButton(onClick = onReviewClick)

            Spacer(Modifier.height(48.dp))
            HorizontalDivider(color = MoonGlow.copy(alpha = 0.3f))
            Spacer(Modifier.height(36.dp))

            // ── Privacy policy ────────────────────────────────────────────────
            Text(
                "PRIVACY POLICY",
                style = MaterialTheme.typography.labelLarge,
                color = Dusk,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Last updated: March 10, 2026",
                style = MaterialTheme.typography.labelSmall,
                color = Dusk.copy(alpha = 0.6f),
            )
            Spacer(Modifier.height(20.dp))
            Text(
                "TapSleep helps you relax and fall asleep by playing soothing sounds locally on your device.",
                style = MaterialTheme.typography.bodySmall,
                color = MoonGlow.copy(alpha = 0.6f),
                lineHeight = 20.sp,
            )
            Spacer(Modifier.height(24.dp))

            privacySections.forEach { (heading, body) ->
                Text(
                    heading,
                    style = MaterialTheme.typography.labelMedium,
                    color = MoonGlow.copy(alpha = 0.9f),
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MoonGlow.copy(alpha = 0.6f),
                    lineHeight = 20.sp,
                )
                Spacer(Modifier.height(20.dp))
            }

            // ── Contact ───────────────────────────────────────────────────────
            Text(
                "Contact",
                style = MaterialTheme.typography.labelMedium,
                color = MoonGlow.copy(alpha = 0.9f),
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Questions about this Privacy Policy? Suggestions for improvements or new features? Reach us at",
                style = MaterialTheme.typography.bodyMedium,
                color = MoonGlow.copy(alpha = 0.6f),
                lineHeight = 20.sp,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                contactEmail,
                style = MaterialTheme.typography.bodyMedium,
                color = Aurora,
                modifier = Modifier.clickable { openUrl("mailto:$contactEmail") },
            )

            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
private fun ReviewButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val shape = RoundedCornerShape(20.dp)
    Text(
        "★ Leave a review",
        style = MaterialTheme.typography.labelLarge,
        color = Dusk,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .graphicsLayer { alpha = if (isPressed) 0.5f else 1f }
            .border(1.dp, Dusk.copy(alpha = 0.4f), shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 28.dp, vertical = 14.dp)
            .fillMaxWidth(),
    )
}
