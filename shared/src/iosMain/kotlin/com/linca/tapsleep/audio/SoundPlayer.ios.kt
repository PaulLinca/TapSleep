package com.linca.tapsleep.audio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioPlayer
import platform.Foundation.NSBundle

private class IosSoundPlayer : SoundPlayer {
    private var avPlayer: AVAudioPlayer? = null

    @OptIn(ExperimentalForeignApi::class)
    override fun play(audioId: String) {
        val url = NSBundle.mainBundle.URLForResource(audioId, withExtension = "mp3") ?: return
        avPlayer = AVAudioPlayer(contentsOfURL = url, error = null).apply {
            numberOfLoops = -1L
            prepareToPlay()
            play()
        }
    }

    override fun pause() {
        avPlayer?.pause()
    }

    override fun resume() {
        avPlayer?.play()
    }

    override fun stop() {
        avPlayer?.stop()
        avPlayer = null
    }
}

@Composable
actual fun rememberSoundPlayer(): SoundPlayer {
    val player = remember { IosSoundPlayer() }
    DisposableEffect(Unit) { onDispose { player.stop() } }
    return player
}
