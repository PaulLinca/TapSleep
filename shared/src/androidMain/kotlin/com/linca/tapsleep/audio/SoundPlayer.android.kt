package com.linca.tapsleep.audio

import android.content.Context
import android.content.Intent
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
    @Volatile private var isPaused = false
    private var currentTitle = ""

    override fun play(audioId: String) {
        // Bug fix 3: don't call stop() here — it calls stopService(), which creates a
        // stop→start gap that causes "did not call startForeground in time" on API 34.
        scope.cancel()
        playerA?.stop(); playerA?.release(); playerA = null
        playerB?.stop(); playerB?.release(); playerB = null

        isPaused = false
        currentTitle = audioId.replaceFirstChar { it.uppercase() }
        scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

        val resId = context.resources.getIdentifier(audioId, "raw", context.packageName)
        if (resId == 0) return

        playerA = MediaPlayer.create(context, resId)?.apply {
            isLooping = false
            setVolume(targetVolume, targetVolume)
            start()
        }

        startService(isPlaying = true)
        scope.launch { crossfadeLoop(resId) }
        scope.launch { observeNotificationCommands() }
    }

    private suspend fun observeNotificationCommands() {
        PlaybackCommands.flow.collect { cmd ->
            when (cmd) {
                "stop"   -> stop()
                "pause"  -> pause()
                "resume" -> resume()
            }
        }
    }

    private suspend fun crossfadeLoop(resId: Int) {
        while (true) {
            val current = playerA ?: return
            val duration = current.duration.takeIf { it > 0 } ?: return
            val fadeStartMs = (duration - CROSSFADE_MS).coerceAtLeast(0)

            while (current.currentPosition < fadeStartMs) {
                delay(80)
                if (!current.isPlaying && !isPaused) return
            }

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
        isPaused = true
        playerA?.pause()
        playerB?.pause()
        updateService(isPlaying = false)
    }

    override fun resume() {
        isPaused = false
        playerA?.start()
        playerB?.start()
        updateService(isPlaying = true)
    }

    override fun setVolume(volume: Float) {
        targetVolume = volume
        if (playerB == null) playerA?.setVolume(volume, volume)
    }

    override fun stop() {
        isPaused = false
        scope.cancel()
        playerA?.stop(); playerA?.release(); playerA = null
        playerB?.stop(); playerB?.release(); playerB = null
        stopService()
    }

    // ── Service helpers ───────────────────────────────────────────────────────

    private fun serviceIntent(isPlaying: Boolean) =
        Intent(context, PlaybackService::class.java).apply {
            putExtra(PlaybackService.EXTRA_TITLE, currentTitle)
            putExtra(PlaybackService.EXTRA_PLAYING, isPlaying)
        }

    private fun startService(isPlaying: Boolean) {
        context.startForegroundService(serviceIntent(isPlaying))
    }

    private fun updateService(isPlaying: Boolean) {
        if (playerA != null) startService(isPlaying)
    }

    private fun stopService() {
        context.stopService(Intent(context, PlaybackService::class.java))
    }
}

@Composable
actual fun rememberSoundPlayer(): SoundPlayer {
    val context = LocalContext.current.applicationContext
    val player = remember { AndroidSoundPlayer(context) }
    DisposableEffect(Unit) { onDispose { player.stop() } }
    return player
}
