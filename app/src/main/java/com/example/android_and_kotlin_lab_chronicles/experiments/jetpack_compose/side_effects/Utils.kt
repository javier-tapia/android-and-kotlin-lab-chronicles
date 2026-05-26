package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.side_effects

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// region Commons
@Composable
fun CustomTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.height(24.dp))
}
// endregion

// region ProduceStateSample
sealed interface ConnectionState {
    object Loading : ConnectionState
    object Available : ConnectionState
    object Unavailable : ConnectionState
}
//endregion

// region RememberUpdatedStateSample
/**
 * SIMULACIÓN DE ENGINE MULTIMEDIA ASÍNCRONO
 */
class MockAudioPlaybackEngine {
    interface PlaybackListener {
        fun onProgressUpdated(progress: Float)
        fun onTrackEnded()
    }

    private var listener: PlaybackListener? = null
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    fun setPlaybackListener(l: PlaybackListener) {
        this.listener = l
    }

    fun startTrackSimulation() {
        job = scope.launch {
            val totalDurationMs = 6000L
            val intervalMs = 100L
            var elapsedMs = 0L

            while (elapsedMs < totalDurationMs) {
                delay(intervalMs)
                elapsedMs += intervalMs
                val progress = elapsedMs.toFloat() / totalDurationMs
                listener?.onProgressUpdated(progress)
            }
            listener?.onTrackEnded()
        }
    }

    fun release() {
        job?.cancel()
        listener = null
    }
}

@Composable
fun AudioEngineBridge(
    onTrackFinished: () -> Unit,
    onProgressChanged: (Float) -> Unit,
    onResetRequired: () -> Unit
) {
    // 1. CAPTURA DEL ESTADO SEGURO (Antídoto contra el Stale State)
    // 'rememberUpdatedState' genera una referencia mutable intermedia vinculada al Snapshot System.
    // Permite que un bloque asíncrono estático lea el último puntero de memoria de una lambda
    // sin obligar al efecto contenedor a reiniciarse o redesplegarse.
    val currentOnTrackFinished by rememberUpdatedState(onTrackFinished)
    val currentOnProgressChanged by rememberUpdatedState(onProgressChanged)

    // 2. CONTROL ESTRICTO DE CLAVE (Clave fija 'Unit')
    // Se usa 'Unit' de forma deliberada para garantizar que el puente de hardware nazca UNA SOLA VEZ.
    // CRÍTICO: Si se pasara 'onTrackFinished' como key en lugar de 'Unit', el DisposableEffect se
    // reiniciaría en cada recomposición ejecutando su 'onDispose'. Esto destruiría y recrearía el
    // 'mockEngine' (provocando cortes bruscos, micro-lags o reinicios del buffer a mitad de la reproducción).
    DisposableEffect(Unit) {
        // Al regresar por scroll en el showcase, este callback limpia estados huérfanos en el ViewModel
        onResetRequired()

        val mockEngine = MockAudioPlaybackEngine()

        // Registro unidireccional en el SDK nativo de audio
        mockEngine.setPlaybackListener(object : MockAudioPlaybackEngine.PlaybackListener {
            override fun onProgressUpdated(progress: Float) {
                // El hilo del SDK actualiza el progreso en tiempo real
                currentOnProgressChanged(progress)
            }

            override fun onTrackEnded() {
                // Al finalizar la pista, el listener lee el contenedor de estado seguro.
                // Si el usuario alteró el Switch a mitad de los 6 segundos, esta línea ejecutará la
                // lógica actualizada, evadiendo la captura del estado viejo (Stale State).
                currentOnTrackFinished()
            }
        })

        mockEngine.startTrackSimulation()

        // 3. RECUPERACIÓN DE RECURSOS (Garbage Collection de ciclo de vida)
        // Se ejecuta de forma imperativa en el dispositivo cuando el nodo sale de la
        // pantalla (ej.: scroll fuera del showcase)
        onDispose {
            mockEngine.release()
        }
    }
}
// endregion
