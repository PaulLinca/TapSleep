package com.linca.tapsleep.ui

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linca.tapsleep.ui.theme.DmMono
import com.linca.tapsleep.ui.theme.Dusk
import com.linca.tapsleep.ui.theme.Moon
import com.linca.tapsleep.ui.theme.MoonGlow
import kotlinx.coroutines.delay

private val OrbCenter = Color(0x669B8FC4)
private val OrbEdge = Color(0x266B7DB3)
private val OrbBorder = Color(0x4D9B8FC4)

/**
 * Shared player UI used by both [PlayerScreen] and [BlendPlayerScreen].
 *
 * Owns all timer/animation state. Audio lifecycle (start, pause, resume, stop)
 * is delegated to the caller via lambdas so each screen can wire its own players.
 */
@Composable
fun PlayerLayout(
    sounds: List<Sound>,
    onStartPlayback: () -> Unit,
    onResume: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit,
    onBack: () -> Unit,
) {
    val preset by remember { mutableStateOf(TimerPreset.MIN_45) }
    var secondsLeft by remember { mutableStateOf(preset.totalSeconds) }
    var isPlaying by remember { mutableStateOf(true) }
    var hasStarted by remember { mutableStateOf(false) }

    LaunchedEffect(isPlaying) {
        when {
            isPlaying && !hasStarted -> { onStartPlayback(); hasStarted = true }
            isPlaying -> onResume()
            else -> onPause()
        }
    }

    LaunchedEffect(preset) { secondsLeft = preset.totalSeconds }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(1000L)
            val s = secondsLeft ?: continue
            if (s <= 0) { onStop(); onBack(); break }
            secondsLeft = s - 1
        }
    }

    // ── Breathing animation ───────────────────────────────────────────────────
    val infiniteTransition = rememberInfiniteTransition(label = "orb")

    val breatheScale by infiniteTransition.animateFloat(
        initialValue = 1.0f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "breathe",
    )
    val glow1 by infiniteTransition.animateFloat(
        initialValue = 0.0f, targetValue = 0.20f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glow1",
    )
    val glow2 by infiniteTransition.animateFloat(
        initialValue = 0.20f, targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glow2",
    )

    val playingFraction by animateFloatAsState(
        targetValue = if (isPlaying) 1f else 0f,
        animationSpec = tween(700, easing = EaseInOutSine),
        label = "playingFraction",
    )
    val orbScale = 1f + (breatheScale - 1f) * playingFraction
    val glowAlpha1 = glow1 * playingFraction
    val glowAlpha2 = glow2 * playingFraction

    val timerDisplay = when {
        preset == TimerPreset.NONE || secondsLeft == null -> "∞"
        else -> {
            val m = secondsLeft!! / 60
            val s = secondsLeft!! % 60
            "$m:${s.toString().padStart(2, '0')}"
        }
    }

    // ── UI ────────────────────────────────────────────────────────────────────
    Box(modifier = Modifier.fillMaxSize()) {
        StarfieldBackground(Modifier.fillMaxSize())
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp),
        ) {
            BackButton(onClick = onBack, modifier = Modifier.align(Alignment.TopStart))

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Sound header — single or blend
                if (sounds.size == 1) {
                    val sound = sounds[0]
                    Icon(
                        imageVector = sound.icon,
                        contentDescription = sound.name,
                        tint = sound.tint.copy(alpha = 0.85f),
                        modifier = Modifier.size(36.dp),
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        sound.name,
                        style = MaterialTheme.typography.headlineSmall.copy(fontStyle = FontStyle.Italic),
                        color = MoonGlow,
                    )
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(25.dp)) {
                        sounds.forEach { sound ->
                            Icon(
                                imageVector = sound.icon,
                                contentDescription = sound.name,
                                tint = sound.tint.copy(alpha = 0.85f),
                                modifier = Modifier.size(28.dp),
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        sounds.joinToString(" · ") { it.name },
                        style = MaterialTheme.typography.headlineSmall.copy(fontStyle = FontStyle.Italic),
                        color = MoonGlow,
                    )
                }

                Spacer(Modifier.height(70.dp))

                // Orb
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(230.dp)
                            .clip(CircleShape)
                            .border(1.dp, OrbBorder.copy(alpha = glowAlpha2), CircleShape),
                    )
                    Box(
                        modifier = Modifier
                            .size(198.dp)
                            .clip(CircleShape)
                            .border(1.dp, OrbBorder.copy(alpha = glowAlpha1), CircleShape),
                    )
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .graphicsLayer { scaleX = orbScale; scaleY = orbScale }
                            .clip(CircleShape)
                            .background(Brush.radialGradient(colors = listOf(OrbCenter, OrbEdge)))
                            .border(1.dp, OrbBorder, CircleShape)
                            .clickable { isPlaying = !isPlaying },
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isPlaying) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Moon.copy(alpha = 0.85f)),
                            )
                        } else {
                            Canvas(Modifier.size(38.dp)) {
                                val path = Path().apply {
                                    moveTo(size.width * 0.1f, 0f)
                                    lineTo(size.width, size.height / 2f)
                                    lineTo(size.width * 0.1f, size.height)
                                    close()
                                }
                                drawPath(path, Moon.copy(alpha = 0.85f))
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    timerDisplay,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = DmMono,
                        fontSize = 32.sp,
                        letterSpacing = 1.5.sp,
                    ),
                    color = Moon,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    if (preset == TimerPreset.NONE) "NO TIMER" else if (isPlaying) "UNTIL SILENCE" else "PAUSED",
                    style = MaterialTheme.typography.labelLarge,
                    color = Dusk,
                )
            }
        }
    }
}
