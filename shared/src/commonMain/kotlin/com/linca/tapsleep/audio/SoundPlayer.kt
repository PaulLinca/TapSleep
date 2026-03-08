package com.linca.tapsleep.audio

import androidx.compose.runtime.Composable

interface SoundPlayer {
    fun play(audioId: String)
    fun pause()
    fun resume()
    fun stop()
}

@Composable
expect fun rememberSoundPlayer(): SoundPlayer
