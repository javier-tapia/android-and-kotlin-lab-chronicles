package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.side_effects.models

// El contenedor global del ViewModel aloja ambos contextos aislados
data class MediaUiState(
    val audioState: AudioPlaybackState = AudioPlaybackState(),
    val telemetryState: TelemetryState = TelemetryState()
)

// Sub-estado exclusivo para RememberUpdatedStateSample
data class AudioPlaybackState(
    val isAutoAdvanceEnabled: Boolean = false,
    val playbackLog: String = "🎵 Cargando buffer e iniciando pista de audio...",
    val currentProgress: Float = 0f
)

// Sub-estado exclusivo para SnapshotFlowSample
data class TelemetryState(
    val currentProgress: Float = 0f,
    val telemetryLog: String = "📊 Esperando fluctuaciones de estado para reportar..."
)
