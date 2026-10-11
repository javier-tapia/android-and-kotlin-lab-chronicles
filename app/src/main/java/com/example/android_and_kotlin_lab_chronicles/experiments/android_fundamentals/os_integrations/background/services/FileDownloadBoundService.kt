package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
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
 * ### *Bound Service* (*in-process*)
 * - Permiten comunicación **bidireccional síncrona y directa** mediante llamadas a métodos en memoria
 * a través del ``Binder`` (ej. ``serviceInstance?.startDownload()``).
 * - Actúan como una interfaz cliente-servidor, **normalmente dentro del mismo proceso y espacio
 * de memoria** de la app que lo invoca (**_in-process_**), permitiendo que otros componentes (como
 * una *Activity*) se vinculen a ellos para **interactuar o enviar comandos** mediante referencias a
 * objetos de Kotlin.
 *     - También pueden ser *out-process*, como un gestor de contraseñas (como *Bitwarden*) o un
 *     servicio de accesibilidad, los cuales deben correr en un **proceso completamente separado y
 *     aislado** en el sistema operativo por motivos de seguridad y estabilidad. Como no comparten
 *     memoria, la comunicación no puede hacerse con un ``Binder`` local tradicional, por lo que
 *     utilizan mecanismos de comunicación entre procesos (IPC) como **AIDL** (_Android Interface
 *     Definition Language_) o ``Messenger`` para serializar y enviar los datos de un proceso a otro.
 * - Este servicio **vive mientras haya clientes vinculados**: cuando el último cliente se desvincula,
 * el sistema destruye el servicio. Es decir, no habrá ejecución “silenciosa” en *background*, ya
 * que tiene **cero privilegios de persistencia**.
 * - **Ejemplos de uso**: Reproducción de audio con controles en UI; Descarga de archivos con
 * progreso en tiempo real; Comunicación con *hardware* (Bluetooth, sensores, etc.).
 *
 * Este lab simula un motor de descarga de archivos y expone su progreso en tiempo real hacia los
 * clientes vinculados.
 */
class FileDownloadBoundService : Service() {

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
     * Tener este ``Job`` permite un control más estricto sobre el ciclo de vida de las corrutinas que
     * se inicien. Es decir, para el **control lógico en tiempo de ejecución** (como cancelar la
     * descarga actual si el usuario decide iniciar otra antes de que termine, o abortarla de forma
     * aislada sin tener que destruir todo el servicio).
     *
     * Por ejemplo, habilita a que en un futuro se pueda agregar una función que se
     * llame desde la UI para cancelar la descarga:
     * ```kotlin
     * fun cancelDownload() {
     *     downloadJob?.cancel()          // Mata la corrutina en curso
     *     _isDownloading.value = false   // Resetea el estado
     * }
     * ```
     */
    private var downloadJob: Job? = null

    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> = _progress.asStateFlow()

    private val _isDownloading = MutableStateFlow(false)
    val isDownloading: StateFlow<Boolean> = _isDownloading.asStateFlow()

    /**
     * [Binder] local desacoplado mediante [WeakReference] para evitar fugas de memoria (*Memory Leaks*).
     *
     * Evita que la referencia fuerte implícita (`this$0`) de una `inner class` retenga al [Service]
     * en la capa nativa de ``Binder`` tras su destrucción, permitiendo una recolección inmediata por el GC.
     *
     * ```kotlin
     * inner class LocalBinder : Binder() {
     *     fun getService(): FileDownloadBoundService = this@FileDownloadBoundService
     * }
     * ```
     */
    class LocalBinder(service: FileDownloadBoundService) : Binder() {
        private val serviceRef = WeakReference(service)

        /**
         * Devuelve la instancia del servicio de forma segura si aún se encuentra en memoria,
         * o `null` si ya ha sido recolectado por el *Garbage Collector*.
         */
        fun getService(): FileDownloadBoundService? = serviceRef.get()
    }

    override fun onBind(intent: Intent?): IBinder = LocalBinder(this)

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    /**
     * Inicia una simulación de descarga por bloques, actualizando el StateFlow de progreso.
     */
    fun startDownload() {
        if (_isDownloading.value) return

        downloadJob?.cancel()
        downloadJob = serviceScope.launch {
            _isDownloading.value = true
            _progress.value = 0

            // Simula la recepción de paquetes de red
            for (i in 1..10) {
                delay(400.milliseconds)
                _progress.value = i * 10
            }

            _isDownloading.value = false
        }
    }
}
