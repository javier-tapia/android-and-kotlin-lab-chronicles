package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.side_effects

import androidx.lifecycle.ViewModel
import com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.side_effects.models.AudioPlaybackState
import com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.side_effects.models.MediaUiState
import com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.side_effects.models.TelemetryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * ViewModel para el laboratorio de Side Effects.
 */
@HiltViewModel
class SideEffectsViewModel @Inject constructor() : ViewModel() {
    // region Properties
    private val _uiState = MutableStateFlow(MediaUiState())
    val uiState: StateFlow<MediaUiState> = _uiState.asStateFlow()
    // endregion

    // region ProduceStateSample
    // Simulación de una fuente de datos asíncrona externa (ejemplo: ConnectivityManager / Sockets)
    val networkStatusFlow: Flow<ConnectionState> = callbackFlow {
        // Pulso 1: Simula verificación inicial de red
        delay(3500)
        trySend(ConnectionState.Available)

        // Pulso 2: Simula caída de red
        delay(3500)
        trySend(ConnectionState.Unavailable)

        // Pulso 3: Simula restauración de red
        delay(3500)
        trySend(ConnectionState.Available)

        awaitClose {
            // Liberación de recursos de infraestructura (SDK de red o sensores nativos) cuando la
            // corrutina es cancelada
            // Ej.:
            // connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
    // endregion

    // region RememberUpdatedStateSample
    fun toggleAutoAdvance(enabled: Boolean) {
        _uiState.update {
            it.copy(audioState = it.audioState.copy(isAutoAdvanceEnabled = enabled))
        }
    }

    fun updateAudioLog(message: String) {
        _uiState.update {
            it.copy(audioState = it.audioState.copy(playbackLog = message))
        }
    }

    fun updateAudioProgress(progress: Float) {
        _uiState.update {
            it.copy(audioState = it.audioState.copy(currentProgress = progress))
        }
    }

    fun resetAudioPlaybackState() {
        _uiState.update {
            it.copy(audioState = AudioPlaybackState())
        }
    }
    // endregion

    // region SnapshotFlowSample
    fun updateTelemetryProgress(progress: Float) {
        _uiState.update {
            it.copy(telemetryState = it.telemetryState.copy(currentProgress = progress))
        }
    }

    fun resetTelemetryState() {
        _uiState.update {
            it.copy(telemetryState = TelemetryState())
        }
    }

    fun sendAnalyticsReport(progressPercentage: Int) {
        _uiState.update {
            it.copy(
                telemetryState = it.telemetryState.copy(
                    telemetryLog = "📊 [Telemetría] Reporte enviado al servidor. Progreso estabilizado en: $progressPercentage%"
                )
            )
        }
    }
    // endregion
}
