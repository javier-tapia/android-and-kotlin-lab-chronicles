package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services.components

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_and_kotlin_lab_chronicles.core.utils.CustomLogger.log
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services.FileDownloadBoundService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Este ViewModel utiliza una "licencia" técnica al extender de [AndroidViewModel] y gestionar el
 * servicio directamente, omitiendo un *Repository/DataSource* dedicado para mantener la simplicidad
 * didáctica sin perder el estándar de separación en la UI.
 *
 * En una arquitectura de producción estricta, la gestión de infraestructura de plataforma debería
 * delegarse a capas inferiores mediante flujos reactivos (como `callbackFlow`).
 *
 * Ejemplo conceptual con `callbackFlow` en una capa de *DataSource* o *Repository*:
 * ```kotlin
 * class FileDownloadRepository @Inject constructor(
 *     @ApplicationContext private val context: Context
 * ) {
 *     /**
 *      * Encapsula el ciclo de vida del ServiceConnection y expone los flujos de progreso
 *      * y estado de conexión mediante callbackFlow.
 *      */
 *     fun observeDownloadService(): Flow<DownloadServiceState> = callbackFlow {
 *         var boundService: FileDownloadBoundService? = null
 *
 *         val connection = object : ServiceConnection {
 *             override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
 *                 val binder = service as? FileDownloadBoundService.LocalBinder
 *                 boundService = binder?.getService()
 *
 *                 boundService?.let { serviceInstance ->
 *                     trySend(DownloadServiceState.Connected(serviceInstance))
 *                 }
 *             }
 *
 *             override fun onServiceDisconnected(name: ComponentName?) {
 *                 boundService = null
 *                 trySend(DownloadServiceState.Disconnected)
 *             }
 *         }
 *
 *         // Inicia el vínculo con el servicio
 *         val intent = Intent(context, FileDownloadBoundService::class.java)
 *         context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
 *
 *         // Se ejecuta automáticamente al cancelar el colector, asegurando una limpieza segura
 *         awaitClose {
 *             try {
 *                 context.unbindService(connection)
 *             } catch (_: IllegalArgumentException) {
 *                 // Ignorar si ya estaba desvinculado
 *             }
 *         }
 *     }
 * }
 * ```
 */
@HiltViewModel
class BoundServiceViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    private val _isBound = MutableStateFlow(false)
    val isBound: StateFlow<Boolean> = _isBound.asStateFlow()

    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> = _progress.asStateFlow()

    private val _isDownloading = MutableStateFlow(false)
    val isDownloading: StateFlow<Boolean> = _isDownloading.asStateFlow()

    /**
     * Actúa como el **estado de control de la conexión** y el puente seguro entre el ViewModel y el
     * mundo nativo del Servicio.
     */
    private var serviceRef: WeakReference<FileDownloadBoundService>? = null
    private var observeJob: Job? = null

    /**
     * El `ServiceConnection` gestiona el puente de enlace (el *binding*) en tiempo real para que el
     * *ViewModel* pueda conversar directamente con el *Bound Service*.
     *
     * La instancia del servicio se crea en el *callback* ``onServiceConnected``, el cual es
     * invocado luego de llamar a `application.bindService()`.
     */
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as? FileDownloadBoundService.LocalBinder
            val boundService = binder?.getService()

            serviceRef = boundService?.let { WeakReference(it) }
            _isBound.value = true

            boundService?.let { serviceInstance ->
                observeJob?.cancel()
                observeJob = viewModelScope.launch {
                    coroutineScope {
                        launch {
                            serviceInstance.progress.collect { _progress.value = it }
                        }
                        launch {
                            serviceInstance.isDownloading.collect { _isDownloading.value = it }
                        }
                    }
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            observeJob?.cancel()
            serviceRef = null
            _isBound.value = false
            _isDownloading.value = false
            _progress.value = 0
        }
    }

    fun bindService() {
        if (!_isBound.value) {
            val intent = Intent(application, FileDownloadBoundService::class.java)
            application.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun unbindService() {
        if (_isBound.value) {
            observeJob?.cancel()
            try {
                application.unbindService(serviceConnection)
            } catch (e: IllegalArgumentException) {
                log("FileDownloadBoundViewModel", "Error", e)
            }
            serviceRef = null
            _isBound.value = false
            _isDownloading.value = false
            _progress.value = 0
        }
    }

    fun startDownload() {
        serviceRef?.get()?.startDownload()
    }

    override fun onCleared() {
        unbindService()
    }
}