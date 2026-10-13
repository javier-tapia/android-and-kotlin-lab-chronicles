package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.broadcast_receivers

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import com.example.android_and_kotlin_lab_chronicles.core.utils.CustomLogger.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Este ViewModel utiliza una "licencia" técnica al extender de [AndroidViewModel] y gestionar el
 * registro del *receiver* directamente, omitiendo un *Repository/DataSource* dedicado para
 * mantener la simplicidad didáctica sin perder el estándar de separación en la UI.
 *
 * En una arquitectura de producción estricta, la gestión de infraestructura de plataforma debería
 * delegarse a capas inferiores mediante flujos reactivos (como `callbackFlow`).
 *
 * Ejemplo conceptual con `callbackFlow` en una capa de *DataSource* o *Repository*:
 * ```kotlin
 * fun observeSystemState(): Flow<String> = callbackFlow {
 *     val receiver = DynamicSystemStateReceiver { message ->
 *         trySend(message)
 *     }
 *
 *     val intentFilter = IntentFilter(Intent.ACTION_BATTERY_LOW)
 *
 *     // Registro seguro de plataforma
 *     context.registerReceiver(receiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
 *
 *     // Limpieza automática cuando el flujo pierde sus observadores (adiós al manejo manual en
 *     // el 'onCleared()' del ViewModel)
 *     awaitClose {
 *         context.unregisterReceiver(receiver)
 *     }
 * }
 *
 * // Y en el ViewModel:
 * // El flujo se inicia automáticamente cuando hay observadores (la UI se muestra)
 * // y se detiene solo cuando la UI desaparece.
 * val eventLog: StateFlow<String> = systemStateRepository.observeSystemState()
 *     .stateIn(
 *         scope = viewModelScope,
 *         started = SharingStarted.WhileSubscribed(5000L), // Pausa 5s ante cambios de config (rotación)
 *         initialValue = "Monitoreando eventos dinámicos..."
 *     )
 * ```
 */
@HiltViewModel
class BroadcastReceiverViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    private val _eventLog = MutableStateFlow("Monitoreando eventos dinámicos...")
    val eventLog: StateFlow<String> = _eventLog.asStateFlow()

    private val _staticBootLog =
        MutableStateFlow(StaticBootReceiver.getLastBootRecord(application))
    val staticBootLog: StateFlow<String> = _staticBootLog.asStateFlow()

    private var dynamicReceiver: BroadcastReceiver? = null

    init {
        registerDynamicReceiver()
    }

    // Limpieza obligatoria del recurso cuando el ViewModel es destruido definitivamente
    override fun onCleared() {
        dynamicReceiver?.let { receiver ->
            try {
                application.unregisterReceiver(receiver)
            } catch (e: IllegalArgumentException) {
                // Previene crashes si el receiver ya fue des-registrado
                log("BroadcastReceiverViewModel", "El receiver fue des-registrado", e)
            }
        }
    }

    private fun registerDynamicReceiver() {
        dynamicReceiver = DynamicSystemStateReceiver { message ->
            _eventLog.value = message
        }

        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }

        // Registro seguro conforme a los estándares de Android Moderno
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            application.registerReceiver(
                dynamicReceiver,
                intentFilter,
                Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            application.registerReceiver(dynamicReceiver, intentFilter)
        }
    }

    /**
     * Refresca el estado leído de SharedPreferences
     */
    fun refreshBootLog() {
        _staticBootLog.value = StaticBootReceiver.getLastBootRecord(getApplication())
    }
}