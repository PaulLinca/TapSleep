package com.linca.tapsleep.audio

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

private class AndroidSoundPlayer(private val context: Context) : SoundPlayer {
    private var mediaPlayer: MediaPlayer? = null

    override fun play(audioId: String) {
        val resId = context.resources.getIdentifier(audioId, "raw", context.packageName)
        mediaPlayer?.release()
        if (resId == 0) return  // audio file not added yet — add res/raw/<audioId>.mp3
        mediaPlayer = MediaPlayer.create(context, resId)?.apply {
            isLooping = true
            start()
        }
    }

    override fun pause() {
        mediaPlayer?.pause()
    }

    override fun resume() {
        mediaPlayer?.start()
    }

    override fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

@Composable
actual fun rememberSoundPlayer(): SoundPlayer {
    val context = LocalContext.current.applicationContext
    val player = remember { AndroidSoundPlayer(context) }
    DisposableEffect(Unit) { onDispose { player.stop() } }
    return player
}
