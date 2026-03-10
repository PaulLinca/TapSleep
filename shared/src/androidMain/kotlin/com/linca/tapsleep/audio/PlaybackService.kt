package com.linca.tapsleep.audio

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import kotlinx.coroutines.flow.MutableSharedFlow

/** Emits commands from the notification back to the active SoundPlayer(s). */
internal object PlaybackCommands {
    val flow = MutableSharedFlow<String>(extraBufferCapacity = 8)
}

class PlaybackService : Service() {

    companion object {
        const val ACTION_STOP   = "com.linca.tapsleep.STOP"
        const val ACTION_PAUSE  = "com.linca.tapsleep.PAUSE"
        const val ACTION_RESUME = "com.linca.tapsleep.RESUME"
        const val EXTRA_TITLE   = "title"
        const val EXTRA_PLAYING = "playing"
        private const val CHANNEL_ID      = "tapsleep_playback"
        private const val NOTIFICATION_ID = 1
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> {
                PlaybackCommands.flow.tryEmit("stop")
                stopSelf()
                return START_NOT_STICKY
            }
            ACTION_PAUSE  -> PlaybackCommands.flow.tryEmit("pause")
            ACTION_RESUME -> PlaybackCommands.flow.tryEmit("resume")
        }
        val title     = intent?.getStringExtra(EXTRA_TITLE) ?: "TapSleep"
        val isPlaying = intent?.getBooleanExtra(EXTRA_PLAYING, true) != false
        showNotification(title, isPlaying)
        return START_NOT_STICKY
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID, "Playback", NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "TapSleep sound controls"
            setSound(null, null)
            enableVibration(false)
        }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    private fun showNotification(title: String, isPlaying: Boolean) {
        fun pendingService(action: String, code: Int) = PendingIntent.getService(
            this, code,
            Intent(this, PlaybackService::class.java).setAction(action),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val toggleAction = if (isPlaying)
            NotificationCompat.Action(android.R.drawable.ic_media_pause,  "Pause",  pendingService(ACTION_PAUSE,  1))
        else
            NotificationCompat.Action(android.R.drawable.ic_media_play,   "Resume", pendingService(ACTION_RESUME, 1))

        val stopAction = NotificationCompat.Action(
            android.R.drawable.ic_menu_close_clear_cancel, "Stop", pendingService(ACTION_STOP, 2)
        )

        val openApp = packageManager.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(com.linca.tapsleep.R.drawable.ic_tapsleep_notification)
            .setContentTitle("TapSleep")
            .setContentText(title)
            .setContentIntent(openApp)
            .addAction(toggleAction)
            .addAction(stopAction)
            .setOngoing(isPlaying)
            .setSilent(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        // Bug fix 1: API 34 requires the foregroundServiceType to be passed explicitly
        ServiceCompat.startForeground(
            this, NOTIFICATION_ID, notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        )
    }

    override fun onDestroy() {
        // Bug fix 2: stopForeground(Int) overload is API 33+ — ServiceCompat handles compat
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }
}
