package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services.components

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services.MusicPlayerForegroundService
import dagger.hilt.android.lifecycle.HiltViewModel
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
 * class MusicPlayerRepository @Inject constructor(
 *     @ApplicationContext private val context: Context
 * ) {
 *     /**
 *     * Encapsula el envío de comandos al Foreground Service, aislando el uso de Intents
 *     * y chequeos de versión de la capa superior.
 *     */
 *     fun dispatchCommand(action: String) {
 *         val intent = Intent(context, MusicPlayerForegroundService::class.java).apply {
 *             this.action = action
 *         }
 *         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
 *             context.startForegroundService(intent)
 *         } else {
 *             context.startService(intent)
 *         }
 *     }
 *
 *     /**
 *      * Ejemplo de cómo un DataSource podría exponer estados del servicio o del reproductor
 *      * mediante callbackFlow escuchando un listener interno en lugar de lógica de UI.
 *      */
 *     fun observePlayerEvents(): Flow<PlaybackEvent> = callbackFlow {
 *         val listener = object : MusicPlayerCallback {
 *             override fun onStateChanged(event: PlaybackEvent) {
 *                 trySend(event) // Emite el evento de forma reactiva
 *             }
 *         }
 *
 *         MusicPlayerServiceConnection.registerCallback(listener)
 *
 *         awaitClose {
 *             MusicPlayerServiceConnection.unregisterCallback(listener)
 *         }
 *     }
 * }
 * ```
 */
@HiltViewModel
class ForegroundServiceViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    fun startPlayback() {
        MusicPlayerForegroundService.start(application)
    }

    fun pausePlayback() {
        MusicPlayerForegroundService.pause(application)
    }

    fun stopService() {
        MusicPlayerForegroundService.stop(application)
    }
}