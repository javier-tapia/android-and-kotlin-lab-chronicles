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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services.MusicPlayerForegroundService

/**
 * #### Componentes clave y su funcionamiento técnico
 * - `permissionLauncher` utiliza `ActivityResultContracts` para gestionar permisos de seguridad
 * del sistema (como el de las notificaciones, `Manifest.permission.POST_NOTIFICATIONS`).
 */
@Composable
fun ForegroundServiceCard() {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            MusicPlayerForegroundService.start(context)
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
                            MusicPlayerForegroundService.start(context)
                        }
                    }
                ) {
                    Text("Play")
                }
                OutlinedButton(
                    onClick = { MusicPlayerForegroundService.pause(context) }
                ) {
                    Text("Pause")
                }
                OutlinedButton(
                    onClick = { MusicPlayerForegroundService.stop(context) }
                ) {
                    Text("Stop")
                }
            }
        }
    }
}
