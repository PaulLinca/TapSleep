package com.linca.tapsleep.audio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import platform.AVFAudio.AVAudioPlayer
import platform.Foundation.NSBundle

private const val CROSSFADE_SECS = 2.5
private const val FADE_STEPS = 40

@OptIn(ExperimentalForeignApi::class)
private class IosSoundPlayer : SoundPlayer {
    private var playerA: AVAudioPlayer? = null
    private var playerB: AVAudioPlayer? = null
    private var scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun play(audioId: String) {
        stop()
        scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        val url = NSBundle.mainBundle.URLForResource(audioId, withExtension = "mp3") ?: return
        playerA = AVAudioPlayer(contentsOfURL = url, error = null).apply {
            numberOfLoops = 0L
            volume = 1f
            prepareToPlay()
            play()
        }
        scope.launch { crossfadeLoop(audioId) }
    }

    private suspend fun crossfadeLoop(audioId: String) {
        while (true) {
            val current = playerA ?: return
            val duration = current.duration.takeIf { it > 0 } ?: return
            val fadeStart = duration - CROSSFADE_SECS

            while (current.playing && current.currentTime < fadeStart) {
                delay(80)
            }
            if (!current.playing) return

            val url = NSBundle.mainBundle.URLForResource(audioId, withExtension = "mp3") ?: return
            val incoming = AVAudioPlayer(contentsOfURL = url, error = null).apply {
                numberOfLoops = 0L
                volume = 0f
                prepareToPlay()
                play()
            }
            playerB = incoming

            val stepDelayMs = (CROSSFADE_SECS * 1000.0 / FADE_STEPS).toLong()
            repeat(FADE_STEPS) { i ->
                val t = (i + 1).toFloat() / FADE_STEPS
                current.volume = 1f - t
                incoming.volume = t
                delay(stepDelayMs)
            }

            current.stop()
            playerA = incoming
            playerB = null
        }
    }

    override fun pause() {
        playerA?.pause()
        playerB?.pause()
    }

    override fun resume() {
        playerA?.play()
        playerB?.play()
    }

    override fun stop() {
        scope.cancel()
        playerA?.stop(); playerA = null
        playerB?.stop(); playerB = null
    }
}

@Composable
actual fun rememberSoundPlayer(): SoundPlayer {
    val player = remember { IosSoundPlayer() }
    DisposableEffect(Unit) { onDispose { player.stop() } }
    return player
}
