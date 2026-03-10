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
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.Foundation.NSBundle

private const val CROSSFADE_SECS = 2.5
private const val FADE_STEPS = 40

@OptIn(ExperimentalForeignApi::class)
private class IosSoundPlayer : SoundPlayer {
    private var playerA: AVAudioPlayer? = null
    private var playerB: AVAudioPlayer? = null
    private var scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var targetVolume = 1f
    private var isPaused = false

    override fun play(audioId: String) {
        stop()
        isPaused = false
        scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayback, error = null)
        val url = NSBundle.mainBundle.URLForResource(audioId, withExtension = "mp3") ?: return
        playerA = AVAudioPlayer(contentsOfURL = url, error = null).apply {
            numberOfLoops = 0L
            volume = targetVolume
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

            // Wait until near the end, treating paused as still-alive
            while (current.currentTime < fadeStart) {
                delay(80)
                if (!current.playing && !isPaused) return // stopped externally
            }

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
                val v = targetVolume
                current.volume = v * (1f - t)
                incoming.volume = v * t
                delay(stepDelayMs)
            }

            current.stop()
            playerA = incoming
            playerB = null
        }
    }

    override fun pause() {
        isPaused = true
        playerA?.pause()
        playerB?.pause()
    }

    override fun resume() {
        isPaused = false
        playerA?.play()
        playerB?.play()
    }

    override fun setVolume(volume: Float) {
        targetVolume = volume
        if (playerB == null) playerA?.volume = volume
    }

    override fun stop() {
        isPaused = false
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
