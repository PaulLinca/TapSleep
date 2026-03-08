package com.linca.tapsleep.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.linca.tapsleep.audio.rememberSoundPlayer

@Composable
fun BlendPlayerScreen(sounds: List<Sound>, onBack: () -> Unit) {
    val p0 = rememberSoundPlayer()
    val p1 = rememberSoundPlayer()
    val p2 = rememberSoundPlayer()
    val players = remember(sounds) { listOf(p0, p1, p2).take(sounds.size) }

    DisposableEffect(Unit) { onDispose { players.forEach { it.stop() } } }

    PlayerLayout(
        sounds = sounds,
        onStartPlayback = { players.forEachIndexed { i, p -> p.play(sounds[i].name.lowercase()) } },
        onResume = { players.forEach { it.resume() } },
        onPause = { players.forEach { it.pause() } },
        onStop = { players.forEach { it.stop() } },
        onBack = onBack,
    )
}
