package com.linca.tapsleep.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.random.Random

private val NightBg = Color(0xFF0A0D1A)
private val LavGlowColors = listOf(Color(0x149B8FC4), Color(0x009B8FC4))
private val AuroraGlowColors = listOf(Color(0x127EB8C4), Color(0x007EB8C4))

private data class StarDot(
    val x: Float,
    val y: Float,
    val radiusDp: Float,
    val minOp: Float,
    val maxOp: Float,
    val duration: Float,
    val delay: Float,
)

private val stars: List<StarDot> = run {
    val rng = Random(seed = 42)
    List(80) { i ->
        val large = i < 16 // 20% large
        StarDot(
            x = rng.nextFloat(),
            y = rng.nextFloat(),
            radiusDp = if (large) rng.nextFloat() * 0.8f + 0.5f else rng.nextFloat() * 0.5f + 0.2f,
            minOp = rng.nextFloat() * 0.15f + 0.05f,
            maxOp = rng.nextFloat() * 0.50f + 0.30f,
            duration = rng.nextFloat() * 5000f + 3000f,
            delay = rng.nextFloat() * 8000f,
        )
    }
}

@Composable
fun StarfieldBackground(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "starfield")

    val rawTime by transition.animateFloat(
        initialValue = 0f,
        targetValue = 8000f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "rawTime",
    )

    val drift1 by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "drift1",
    )

    val drift2 by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "drift2",
    )

    Canvas(modifier = modifier) {
        drawRect(NightBg)

        // Lavender ambient glow — drifts slowly from top-left
        val lavCenterX = size.width * (0.10f + drift1 * 0.10f)
        val lavCenterY = size.height * (0.10f + drift1 * 0.08f)
        drawRect(
            brush = Brush.radialGradient(
                colors = LavGlowColors,
                center = Offset(lavCenterX, lavCenterY),
                radius = size.minDimension * 0.55f,
            ),
        )

        // Aurora ambient glow — drifts slowly from bottom-right
        val aurCenterX = size.width * (0.81f + drift2 * 0.08f)
        val aurCenterY = size.height * (0.81f + drift2 * 0.08f)
        drawRect(
            brush = Brush.radialGradient(
                colors = AuroraGlowColors,
                center = Offset(aurCenterX, aurCenterY),
                radius = size.minDimension * 0.50f,
            ),
        )

        // Stars
        val px = density
        stars.forEach { star ->
            val t = ((rawTime + star.delay) % star.duration) / star.duration
            val alphaFrac = ((1.0 - cos(t * 2.0 * PI)) / 2.0).toFloat()
            val alpha = star.minOp + (star.maxOp - star.minOp) * alphaFrac
            val scale = 1f + 0.3f * alphaFrac
            drawCircle(
                color = Color.White.copy(alpha = alpha),
                radius = star.radiusDp * px * scale,
                center = Offset(star.x * size.width, star.y * size.height),
            )
        }
    }
}
