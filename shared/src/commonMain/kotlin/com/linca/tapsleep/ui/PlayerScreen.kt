package com.linca.tapsleep.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.linca.tapsleep.audio.rememberSoundPlayer

@Composable
fun PlayerScreen(sound: Sound, onBack: () -> Unit) {
    val player = rememberSoundPlayer()
    DisposableEffect(Unit) { onDispose { player.stop() } }

    PlayerLayout(
        sounds = listOf(sound),
        onStartPlayback = { player.play(sound.name.lowercase()) },
        onResume = { player.resume() },
        onPause = { player.pause() },
        onStop = { player.stop() },
        onBack = onBack,
    )
}
