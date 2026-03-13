package com.linca.tapsleep.ui

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

private val SplashEaseOut = Easing { t -> 1f - (1f - t) * (1f - t) }

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var phase by remember { mutableStateOf(0) }   // 0 = invisible, 1 = visible, 2 = fading-out

    val alpha by animateFloatAsState(
        targetValue = if (phase == 1) 1f else 0f,
        animationSpec = tween(
            durationMillis = if (phase == 2) 700 else 900,
            easing = SplashEaseOut,
        ),
        label = "splash_alpha",
    )

    LaunchedEffect(Unit) {
        phase = 1
        delay(2_400)
        phase = 2
        delay(750)
        onFinished()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        StarfieldBackground(modifier = Modifier.fillMaxSize())
        TapSleepLogoStacked(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer { this.alpha = alpha },
            iconSize = 96.dp,
            showTagline = true,
        )
    }
}
