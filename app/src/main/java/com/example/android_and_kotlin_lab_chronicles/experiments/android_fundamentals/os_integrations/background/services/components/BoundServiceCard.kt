package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * ### Capa de Presentación Pura
 *
 * Componente Composable que representa la interfaz visual del laboratorio de *Bound Service*.
 *
 * #### Aspectos Clave de Arquitectura y Funcionamiento
 * - **Delegación al *ViewModel***: Toda la lógica de negocio, control de estados reactivos y gestión
 *   del enlace (`ServiceConnection`) se encuentra encapsulada en [BoundServiceViewModel].
 * - **Ciclo de Vida con `DisposableEffect`**: Se utiliza un `DisposableEffect(Unit)` para asegurar que
 *   el servicio se desvincule automáticamente cuando el Composable abandona la pantalla (por ejemplo,
 *   al navegar hacia atrás o por reciclaje en una lista), previniendo fugas de recursos o referencias
 *   colgadas mediante la llamada a `viewModel.unbindService()`.
 *
 * @param viewModel Instancia del [BoundServiceViewModel] inyectada automáticamente mediante Hilt.
 */
@Composable
fun BoundServiceCard(
    viewModel: BoundServiceViewModel = hiltViewModel()
) {

    val isBound by viewModel.isBound.collectAsStateWithLifecycle()
    val downloadProgress by viewModel.progress.collectAsStateWithLifecycle()
    val isDownloading by viewModel.isDownloading.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.unbindService()
        }
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "2. Bound Service (File Downloader)",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Estado: ${if (isBound) "Vinculado (Active)" else "Desvinculado"}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Progreso: $downloadProgress% ${if (isDownloading) "⬇️ Descargando..." else ""}",
                style = MaterialTheme.typography.bodySmall
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.bindService() },
                        enabled = !isBound
                    ) {
                        Text("Bind")
                    }
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.unbindService() },
                        enabled = isBound
                    ) {
                        Text("Unbind")
                    }
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.startDownload() },
                enabled = isBound && !isDownloading
            ) {
                Text("Descargar")
            }
        }
    }
}
