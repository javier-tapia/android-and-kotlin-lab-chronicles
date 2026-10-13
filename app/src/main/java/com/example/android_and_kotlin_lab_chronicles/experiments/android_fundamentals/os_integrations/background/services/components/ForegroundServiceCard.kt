package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services.components

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

/**
 * ### Capa de Presentación Pura
 *
 * Componente Composable que representa la interfaz visual del laboratorio de *Foreground Service*.
 *
 * #### Aspectos Clave de Arquitectura y Funcionamiento
 * - **Delegación al ViewModel**: Toda la lógica de negocio y emisión de comandos al servicio
 *   se delega a [ForegroundServiceViewModel], manteniendo este Composable libre de referencias
 *   directas a `Context`, `Intents` o lógica de infraestructura.
 * - **Gestión de Permisos (`permissionLauncher`)**: Utiliza `ActivityResultContracts.RequestPermission()`
 *   para solicitar dinámicamente el permiso de seguridad `Manifest.permission.POST_NOTIFICATIONS`
 *   (requerido en Android 13 / API 33+) antes de autorizar el inicio de la reproducción en primer plano.
 *
 * @param viewModel Instancia del [ForegroundServiceViewModel] inyectada automáticamente mediante Hilt.
 */
@Composable
fun ForegroundServiceCard(
    viewModel: ForegroundServiceViewModel = hiltViewModel()
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startPlayback()
        }
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "1. Foreground Service (Music Player)",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Simula reproducción multimedia continua. Requiere 'mediaPlayback' (Android 14+) y notificación persistente.",
                style = MaterialTheme.typography.bodySmall
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            viewModel.startPlayback()
                        }
                    }
                ) {
                    Text("Play")
                }
                OutlinedButton(
                    onClick = { viewModel.pausePlayback() }
                ) {
                    Text("Pause")
                }
                OutlinedButton(
                    onClick = { viewModel.stopService() }
                ) {
                    Text("Stop")
                }
            }
        }
    }
}