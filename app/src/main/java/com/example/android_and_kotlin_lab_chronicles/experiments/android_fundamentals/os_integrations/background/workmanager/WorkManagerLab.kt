package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.workmanager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo

/**
 * Renderiza los controles visuales para disparar tareas únicas bajo diferentes políticas
 * de control de concurrencia ([ExistingWorkPolicy.KEEP], [ExistingWorkPolicy.REPLACE]) y cadenas
 * de ejecución secuencial.
 *
 * Observa de forma reactiva el ciclo de vida del [WorkInfo] actual
 * mediante [collectAsStateWithLifecycle] para traducir los estados internos del sistema a
 * mensajes claros en tiempo real dentro de la interfaz.
 *
 * @see WorkManagerLabViewModel
 * @see SyncDataWorker
 */
@Composable
fun WorkManagerLab(
    viewModel: WorkManagerLabViewModel = hiltViewModel()
) {
    val workInfoState by viewModel.workInfoState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "WorkManager: Patrones Avanzados & Hilt",
            style = MaterialTheme.typography.titleMedium
        )

        // 1. Trabajo único con KEEP
        Button(
            onClick = { viewModel.scheduleUniqueSyncTask(ExistingWorkPolicy.KEEP) }
        ) {
            Text("Encolar Único (KEEP)")
        }

        // 2. Trabajo único con REPLACE
        OutlinedButton(
            onClick = { viewModel.scheduleUniqueSyncTask(ExistingWorkPolicy.REPLACE) }
        ) {
            Text("Encolar Único (REPLACE)")
        }

        // 3. Cadena de trabajos secuenciales
        Button(
            onClick = { viewModel.scheduleChainedTasks() }
        ) {
            Text("Ejecutar Cadena (beginWith -> then)")
        }

        Text(
            text = "Estado del Worker actual:",
            style = MaterialTheme.typography.labelMedium
        )

        val statusText = when (workInfoState?.state) {
            WorkInfo.State.ENQUEUED -> "Encolada / Esperando turno..."
            WorkInfo.State.RUNNING -> "🔄 Ejecutándose en segundo plano..."
            WorkInfo.State.SUCCEEDED -> "✅ Completado con éxito (Result.success)"
            WorkInfo.State.FAILED -> "❌ Fallido (Result.failure)"
            WorkInfo.State.BLOCKED -> "🔒 Bloqueado (Esperando tarea previa)"
            WorkInfo.State.CANCELLED -> "🚫 Cancelado"
            null -> "Sin ejecuciones activas"
        }

        Text(
            text = statusText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
