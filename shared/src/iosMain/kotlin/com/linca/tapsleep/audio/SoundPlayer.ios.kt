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
import platform.AVFAudio.setActive
import platform.Foundation.NSBundle
import platform.Foundation.NSNumber
import platform.Foundation.numberWithDouble
import platform.MediaPlayer.MPMediaItemPropertyPlaybackDuration
import platform.MediaPlayer.MPMediaItemPropertyTitle
import platform.MediaPlayer.MPNowPlayingInfoCenter
import platform.MediaPlayer.MPNowPlayingInfoPropertyElapsedPlaybackTime
import platform.MediaPlayer.MPNowPlayingInfoPropertyPlaybackRate
import platform.MediaPlayer.MPRemoteCommandCenter
import platform.MediaPlayer.MPRemoteCommandHandlerStatusSuccess

private const val CROSSFADE_SECS = 2.5
private const val FADE_STEPS = 40

@OptIn(ExperimentalForeignApi::class)
private class IosSoundPlayer : SoundPlayer {
    private var playerA: AVAudioPlayer? = null
    private var playerB: AVAudioPlayer? = null
    private var scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var targetVolume = 1f
    private var isPaused = false
    private var currentAudioId = ""

    override fun play(audioId: String) {
        stop()
        isPaused = false
        currentAudioId = audioId
        scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayback, error = null)
        AVAudioSession.sharedInstance().setActive(true, null)
        val url = NSBundle.mainBundle.URLForResource(audioId, withExtension = "mp3") ?: return
        playerA = AVAudioPlayer(contentsOfURL = url, error = null).apply {
            numberOfLoops = 0L
            volume = targetVolume
            prepareToPlay()
            play()
        }
        setupRemoteControls()
        updateNowPlaying(playing = true)
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
        updateNowPlaying(playing = false)
    }

    override fun resume() {
        isPaused = false
        playerA?.play()
        playerB?.play()
        updateNowPlaying(playing = true)
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
        clearRemoteControls()
    }

    private var remoteControlsSetUp = false

    private fun setupRemoteControls() {
        if (remoteControlsSetUp) return
        val cc = MPRemoteCommandCenter.sharedCommandCenter()
        cc.playCommand.addTargetWithHandler { _ -> resume(); MPRemoteCommandHandlerStatusSuccess }
        cc.pauseCommand.addTargetWithHandler { _ -> pause(); MPRemoteCommandHandlerStatusSuccess }
        cc.stopCommand.addTargetWithHandler { _ -> stop(); MPRemoteCommandHandlerStatusSuccess }
        cc.togglePlayPauseCommand.addTargetWithHandler { _ ->
            if (isPaused) resume() else pause()
            MPRemoteCommandHandlerStatusSuccess
        }
        cc.playCommand.enabled = true
        cc.pauseCommand.enabled = true
        cc.stopCommand.enabled = true
        cc.togglePlayPauseCommand.enabled = true
        remoteControlsSetUp = true
    }

    private fun updateNowPlaying(playing: Boolean) {
        val player = playerA
        val duration = player?.duration?.takeIf { it > 0 } ?: 0.0
        val elapsed = player?.currentTime ?: 0.0
        MPNowPlayingInfoCenter.defaultCenter().nowPlayingInfo = mapOf(
            MPMediaItemPropertyTitle to currentAudioId.replaceFirstChar { it.uppercase() },
            MPNowPlayingInfoPropertyPlaybackRate to NSNumber.numberWithDouble(if (playing) 1.0 else 0.0),
            MPMediaItemPropertyPlaybackDuration to NSNumber.numberWithDouble(duration),
            MPNowPlayingInfoPropertyElapsedPlaybackTime to NSNumber.numberWithDouble(elapsed),
        )
    }

    private fun clearRemoteControls() {
        val cc = MPRemoteCommandCenter.sharedCommandCenter()
        cc.playCommand.removeTarget(null)
        cc.pauseCommand.removeTarget(null)
        cc.stopCommand.removeTarget(null)
        cc.togglePlayPauseCommand.removeTarget(null)
        MPNowPlayingInfoCenter.defaultCenter().nowPlayingInfo = null
        remoteControlsSetUp = false
    }
}

@Composable
actual fun rememberSoundPlayer(): SoundPlayer {
    val player = remember { IosSoundPlayer() }
    DisposableEffect(Unit) { onDispose { player.stop() } }
    return player
}
