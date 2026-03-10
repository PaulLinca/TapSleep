package com.linca.tapsleep.audio

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val CROSSFADE_MS = 2500L
private const val FADE_STEPS = 40

private class AndroidSoundPlayer(private val context: Context) : SoundPlayer {
    private var playerA: MediaPlayer? = null
    private var playerB: MediaPlayer? = null
    private var scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    @Volatile private var targetVolume = 1f

    override fun play(audioId: String) {
        stop()
        scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        val resId = context.resources.getIdentifier(audioId, "raw", context.packageName)
        if (resId == 0) return
        playerA = MediaPlayer.create(context, resId)?.apply {
            isLooping = false
            setVolume(targetVolume, targetVolume)
            start()
        }
        scope.launch { crossfadeLoop(resId) }
    }

    private suspend fun crossfadeLoop(resId: Int) {
        while (true) {
            val current = playerA ?: return
            val duration = current.duration.takeIf { it > 0 } ?: return
            val fadeStartMs = (duration - CROSSFADE_MS).coerceAtLeast(0)

            while (current.isPlaying && current.currentPosition < fadeStartMs) {
                delay(80)
            }
            if (!current.isPlaying) return

            val incoming = MediaPlayer.create(context, resId)?.apply {
                isLooping = false
                setVolume(0f, 0f)
                start()
            } ?: return
            playerB = incoming

            val stepDelay = CROSSFADE_MS / FADE_STEPS
            repeat(FADE_STEPS.toInt()) { i ->
                val t = (i + 1).toFloat() / FADE_STEPS
                val v = targetVolume
                current.setVolume(v * (1f - t), v * (1f - t))
                incoming.setVolume(v * t, v * t)
                delay(stepDelay)
            }

            current.stop()
            current.release()
            playerA = incoming
            playerB = null
        }
    }

    override fun pause() {
        playerA?.pause()
        playerB?.pause()
    }

    override fun resume() {
        playerA?.start()
        playerB?.start()
    }

    override fun setVolume(volume: Float) {
        targetVolume = volume
        // Only apply immediately when not mid-crossfade; crossfade loop picks up targetVolume each step
        if (playerB == null) playerA?.setVolume(volume, volume)
    }

    override fun stop() {
        scope.cancel()
        playerA?.stop(); playerA?.release(); playerA = null
        playerB?.stop(); playerB?.release(); playerB = null
    }
}

@Composable
actual fun rememberSoundPlayer(): SoundPlayer {
    val context = LocalContext.current.applicationContext
    val player = remember { AndroidSoundPlayer(context) }
    DisposableEffect(Unit) { onDispose { player.stop() } }
    return player
}
