package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.broadcast_receivers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Pantalla de experimentación del laboratorio para visualizar en tiempo real el comportamiento
 * de los *receivers* dinámicos y estáticos.
 *
 * ###### Pruebas con *receivers* estáticos (*Boot Completed*)
 * Para simular el inicio del sistema sin reiniciar manualmente el emulador:
 *
 * 1. Habilitar privilegios de root en el emulador (requiere una imagen de Google APIs, no de Google Play):
 *    ```bash
 *    adb root
 *    ```
 * 2. Enviar el broadcast de inicio explícitamente mediante ADB:
 *    ```bash
 *    adb shell am broadcast -a android.intent.action.BOOT_COMPLETED -p com.example.android_and_kotlin_lab_chronicles
 *    ```
 * 3. Pulsar el botón **"Actualizar Registro de Boot"** en la UI para refrescar la lectura persistida.
 */
@Composable
fun BroadcastReceiverLab(
    viewModel: BroadcastReceiverViewModel = hiltViewModel()
) {
    val eventLog by viewModel.eventLog.collectAsStateWithLifecycle()
    val staticBootLog by viewModel.staticBootLog.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "BroadcastReceiverLab",
            style = MaterialTheme.typography.titleLarge
        )

        // Receiver Dinámico
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Estado del Sistema en Vivo (Receiver Dinámico)",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = eventLog,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Receiver Estático (Boot Completed)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Detección de Boot Completed (Receiver Estático)",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = staticBootLog,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        viewModel.refreshBootLog()
                    }
                ) {
                    Text(text = "Actualizar Registro de Boot")
                }
            }
        }
    }
}
