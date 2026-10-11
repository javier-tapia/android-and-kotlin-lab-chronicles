package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services

import android.R.drawable.ic_media_play
import android.R.drawable.ic_menu_close_clear_cancel
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.android_and_kotlin_lab_chronicles.core.utils.CustomLogger.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * ### *Foreground Service*
 *
 * - Permiten comunicación **unidireccional asíncrona** mediante ``Intents`` (ej. ``ACTION_PAUSE``,
 * ``ACTION_NEXT``, enviando comandos a través de ``context.startService(intent)``).
 * - Tareas **activamente perceptibles** por el usuario mientras se ejecutan.
 * - Muestran una **notificación persistente** gracias a la cual tiene los privilegios del sistema
 * operativo para vivir en segundo plano, evitar que Android lo mate por falta de memoria y
 * mantenerse activo.
 * - **Ejemplos de uso**: Navegación GPS; Reproducción de música.
 *
 * #### Contexto Técnico y Restricciones (Android 14+)
 *
 * - ***Foreground Service Type****: Declara `android:foregroundServiceType="mediaPlayback"` en el
 * Manifest, respaldado por el permiso `FOREGROUND_SERVICE_MEDIA_PLAYBACK`.
 * - **Supervivencia y Notificación**: Garantiza que el sistema operativo no mate la aplicación al
 * pasar a segundo plano, mostrando controles de transporte directamente en la notificación persistente.
 * - **Seguridad**: `android:exported="false"` para evitar inyecciones de comandos externos no autorizados.
 *
 * #### Ciclo de vida, inicio dual y gestión de un FGS
 *
 * ##### 1. El inicio dual de un *Foreground Service*: [Context.startForegroundService] + [Service.startForeground]
 * El arranque de un FGS requiere un proceso de dos fases para cumplir con las políticas estrictas
 * de Android y evitar excepciones fatales por tiempo de espera (`ForegroundServiceDidNotStartInTimeException`):
 * - **Fase 1 (Desde la UI / Cliente):** Se invoca [Context.startService] (o [Context.startForegroundService]
 * a partir de Android 8 / API 26+) transmitiendo un [Intent] con una acción específica como
 * `ACTION_PLAY`. Esto le indica al sistema que el servicio va a reclamar estatus prioritario,
 * activando un cronómetro interno de pocos segundos.
 * - **Fase 2 (Desde el propio *Service*):** Dentro del método `onStartCommand` (evaluando el intent
 * recibido, por ejemplo, si `intent?.action == ACTION_PLAY`), el servicio **debe obligatoriamente**
 * invocar [Service.startForeground] pasando un ID único y una notificación persistente antes de que
 * expire el cronómetro del sistema. Esto otorga estatus protegido frente a la escasez de memoria RAM
 * y muestra feedback visual obligatorio al usuario.
 *
 * ##### 2. Detención y limpieza con [Service.stopForeground]
 * Permite retirar el estatus de primer plano al servicio. Su parámetro de configuración determina
 * qué sucede con la notificación visual en la barra de estado del dispositivo:
 * - [Service.STOP_FOREGROUND_REMOVE]: Desvincula al servicio del primer plano **y elimina/borra**
 * automáticamente la notificación persistente. Se utiliza cuando la tarea finaliza por completo
 * (ej. fin de reproducción o cierre).
 * - [Service.STOP_FOREGROUND_DETACH]: Desvincula al servicio del primer plano, **pero mantiene
 * visible** la notificación en la pantalla del usuario. Es el comportamiento clásico de un reproductor
 * de música cuando el usuario presiona "Pausa": el servicio se relaja (pierde privilegios de FGS),
 * pero la notificación sigue ahí para permitir reanudar la acción rápidamente de forma interactiva.
 *
 * @see <a href="https://developer.android.com/develop/background-work/services/foreground-services">Android Developers: Foreground Services</a>
 */
class MusicPlayerForegroundService : Service() {
    /**
     * Se usa un ``SupervisorJob`` porque en un servicio en segundo plano es muy común que se lancen
     * múltiples tareas independientes (por ejemplo: una corrutina que descarga un archivo, otra que
     * sincroniza datos con una base de datos local, y otra que escucha eventos de red).
     *
     * Si por un error de red la descarga falla, **no se quiere que eso apague todo el servicio** ni
     * mate las otras tareas que están corriendo en paralelo. El ``SupervisorJob`` da esa resiliencia.
     */
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    /**
     * Estado interno privado (no se expone a la UI)
     */
    private var isPlaying = false

    /**
     * Este ``Job`` almacena la referencia a la corrutina activa de reproducción en segundo plano.
     *
     * Su utilización directa en [handlePlay] (cancelando instancias previas antes de iniciar una
     * nueva iteración) y en [handlePause] (deteniendo el bucle de ejecución) permite un control
     * preciso del ciclo de vida de la tarea sin necesidad de destruir el servicio ni perder
     * su estatus de primer plano.
     */
    private var playbackJob: Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    /**
     * ##### Sobre el retorno: ``START_NOT_STICKY`` vs ``START_STICKY``
     *
     * - `START_NOT_STICKY`:
     *     - Si el sistema operativo mata el servicio por escasez de RAM después de que
     *     ``onStartCommand`` haya retornado, **Android NO recrea el servicio automáticamente**.
     *     - El servicio permanece destruido y apagado hasta que la aplicación vuelva a iniciarlo
     *     explícitamente desde la UI o mediante otro componente.
     *     - **Ejemplos de uso:** Es ideal para **tareas puntuales** (como una descarga o sincronización
     *     única) donde no tiene sentido revivir el proceso a medias si el sistema ya tuvo que
     *     purgarlo por falta de recursos.
     * - `START_STICKY`:
     *     - Si el sistema operativo mata el servicio por escasez de RAM después de que
     *     ``onStartCommand`` haya retornado, **Android RECREA el servicio automáticamente** y vuelve
     *     a invocar ``onStartCommand`` (pasando un ``intent`` nulo si no hay comandos pendientes).
     *     - **Ejemplos de uso:** Es ideal para **servicios que deben mantenerse activos y listos
     *     para operar de forma continua** a pesar de los altibajos de memoria del sistema (como un
     *     reproductor de música o un servicio de monitoreo en tiempo real), asegurando que el proceso
     *     reviva tan pronto como el sistema tenga recursos disponibles.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> handlePlay()
            ACTION_PAUSE -> handlePause()
            ACTION_STOP -> {
                isPlaying = false
                playbackJob?.cancel()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    /**
     * En una aplicación real, en lugar de un bucle vacío con un ``delay(2000)``, ahí se pondrían
     * tareas en segundo plano reales como:
     * - Transmitir o descargar fragmentos de audio vía streaming desde una URL o base de datos local.
     * - Leer secuencialmente un archivo de música pesado desde el almacenamiento del teléfono.
     * - Actualizar contadores de tiempo transcurrido para sincronizar la barra de progreso de una pista.
     * - Sincronizar telemetría o estadísticas de reproducción con un servidor backend.
     */
    private fun handlePlay() {
        if (isPlaying) return
        isPlaying = true

        val notification = buildNotification("Reproduciendo: Track #1")
        startForeground(NOTIFICATION_ID, notification)

        // Tarea en segundo plano controlada por el estado interno
        playbackJob?.cancel()
        playbackJob = serviceScope.launch {
            while (isPlaying) {
                delay(2000.milliseconds)
                log("MusicForegroundService", "Ejecutando tarea en segundo plano...")
            }
        }
    }

    private fun handlePause() {
        isPlaying = false
        playbackJob?.cancel()

        val notification = buildNotification("En pausa: Lab Track #1")
        getSystemService(NotificationManager::class.java)
            ?.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Reproductor de Música (Lab)",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Canal para Foreground Service de reproducción multimedia"
        }
        getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
    }

    /**
     * ### `PendingIntent`
     * Es como un *ticket* o vale que se le entrega a un agente externo (en este
     * caso, el sistema operativo de Android y su barra de notificaciones). Le da permiso al sistema
     * operativo para que ejecute una acción en nombre de la app en el futuro, utilizando los
     * permisos y el contexto de la app, incluso si está cerrada o minimizada.
     *
     * #### `PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT`
     * Las dos *flags* que recibe **resuelven dos problemas completamente distintos** (la seguridad
     * y la actualización de datos), por lo que se combinan utilizando el operador *bitwise* ``or`` (``|``).
     *
     * ###### ``PendingIntent.FLAG_IMMUTABLE`` (Seguridad ante Modificaciones Externas)
     * - **Qué hace**: Indica que el ``Intent`` que va dentro del ``PendingIntent`` es **inmutable**
     * (no se puede modificar). Una vez creado, ninguna aplicación externa (ni siquiera el sistema
     * operativo) puede cambiar sus acciones, datos o extras.
     * - **Por qué es crucial**: A partir de **Android 12 (API 31)**, Google hizo que esto sea
     * **obligatorio** por motivos de seguridad. Antes de esta norma, las aplicaciones maliciosas
     * podían interceptar ``PendingIntents`` mutables de otras apps y modificar los datos ocultos
     * para realizar ataques suplantación de identidad (*Intent Hijacking*). Al marcarlo como
     * ``IMMUTABLE``, se garantiza que nadie altere el contenido de la orden.
     *
     * ###### ``PendingIntent.FLAG_UPDATE_CURRENT`` (Actualización Interna de Datos)
     * - **Qué hace**: Si el sistema operativo ya tenía registrado un ``PendingIntent`` anterior con
     * el mismo código de solicitud (el ``requestCode``, que en este caso es ``0``), esta *flag* le
     * dice a Android: "No crees uno nuevo desde cero; **mantén el existente, pero actualiza sus extras
     * o datos si algo cambió**".
     * - **Por qué es crucial**: En un reproductor multimedia, la notificación cambia constantemente
     * (por ejemplo, los metadatos de la canción, el estado de reproducción, o los parámetros
     * internos). Si se actualiza la notificación varias veces, esta *flag* asegura que los botones
     * sigan apuntando a los datos más recientes sin acumular notificaciones duplicadas en la caché
     * del sistema.
     */
    private fun buildNotification(statusText: String): Notification {
        val stopIntent = Intent(
            this,
            MusicPlayerForegroundService::class.java
        ).apply { action = ACTION_PAUSE }

        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Lab Chronicles: Music Player")
            .setContentText(statusText)
            .setSmallIcon(ic_media_play)
            .setOngoing(true)
            .addAction(
                ic_menu_close_clear_cancel,
                "Pausar",
                stopPendingIntent
            )
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()
    }

    companion object {
        private const val CHANNEL_ID = "lab_music_playback_channel"
        private const val NOTIFICATION_ID = 2001

        const val ACTION_PLAY = "action_play"
        const val ACTION_PAUSE = "action_pause"
        const val ACTION_STOP = "action_stop"

        @SuppressLint("ObsoleteSdkInt")
        fun start(context: Context) {
            val intent = Intent(
                context,
                MusicPlayerForegroundService::class.java
            ).apply {
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
                MusicPlayerForegroundService::class.java
            ).apply {
                action = ACTION_PAUSE
            }
            context.startService(intent)
        }

        fun stop(context: Context) {
            val intent = Intent(
                context,
                MusicPlayerForegroundService::class.java
            ).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }
}
