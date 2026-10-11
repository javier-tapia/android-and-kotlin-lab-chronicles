package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import kotlin.time.Duration.Companion.milliseconds

/**
 * ### Servicio Híbrido ("*Started and Bound*")
 * - ***Started***: Garantiza la persistencia en segundo plano mediante `startForeground` y notificación.
 * - ***Bound***: Permite a los clientes (como la UI) vincularse para observar estados reactivos
 * (`StateFlow`) y comandar acciones directamente.
 *
 * @see MusicPlayerForegroundService
 * @see FileDownloadBoundService
 */
class MusicPlayerHybridService : Service() {
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
    private var playbackJob: Job? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playbackProgress = MutableStateFlow(0)
    val playbackProgress: StateFlow<Int> = _playbackProgress.asStateFlow()

    class LocalBinder(service: MusicPlayerHybridService) : Binder() {
        private val serviceRef = WeakReference(service)
        fun getService(): MusicPlayerHybridService? = serviceRef.get()
    }

    override fun onBind(intent: Intent?): IBinder = LocalBinder(this)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> handlePlay()
            ACTION_PAUSE -> handlePause()
            ACTION_STOP -> {
                terminateService()
            }
        }

        return START_STICKY
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Reproductor Híbrido (Lab)",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Canal para Foreground + Bound Service combinado"
        }
        getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
    }

    private fun handlePlay() {
        _isPlaying.value = true
        val notification = buildNotification("Reproduciendo (Híbrido): Track #99")

        startForeground(NOTIFICATION_ID, notification)

        playbackJob?.cancel()
        playbackJob = serviceScope.launch {
            while (_isPlaying.value) {
                delay(1000.milliseconds)
                _playbackProgress.value = (_playbackProgress.value + 1) % 100
            }
        }
    }

    private fun handlePause() {
        _isPlaying.value = false
        playbackJob?.cancel()

        val notification = buildNotification("En pausa (Híbrido): Lab Track #99")
        getSystemService(NotificationManager::class.java)?.notify(NOTIFICATION_ID, notification)
    }

    private fun terminateService() {
        _isPlaying.value = false
        playbackJob?.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun buildNotification(statusText: String): Notification {
        val stopIntent = Intent(
            this,
            MusicPlayerHybridService::class.java
        ).apply { action = ACTION_PAUSE }

        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Lab Chronicles: Hybrid Service")
            .setContentText(statusText)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Pausar",
                stopPendingIntent
            )
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()
    }

    companion object {
        private const val CHANNEL_ID = "lab_hybrid_music_channel"
        private const val NOTIFICATION_ID = 3001

        const val ACTION_PLAY = "action_play"
        const val ACTION_PAUSE = "action_pause"
        const val ACTION_STOP = "action_stop"

        @SuppressLint("ObsoleteSdkInt")
        fun start(context: Context) {
            val intent = Intent(context, MusicPlayerHybridService::class.java).apply {
                action = ACTION_PLAY
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun pause(context: Context) {
            val intent = Intent(
                context,
                MusicPlayerHybridService::class.java
            ).apply {
                action = ACTION_PAUSE
            }
            context.startService(intent)
        }

        fun stop(context: Context) {
            val intent = Intent(
                context,
                MusicPlayerHybridService::class.java
            ).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }
}
