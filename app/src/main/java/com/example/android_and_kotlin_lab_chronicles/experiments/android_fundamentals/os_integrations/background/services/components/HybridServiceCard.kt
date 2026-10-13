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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * A este tipo de servicio "híbrido", también se lo conoce como ***Started & Bound***.
 *
 * Combina FGS (supervivencia por notificación) con *Bound* (observabilidad por ``StateFlow``).
 *
 * @see ForegroundServiceCard
 * @see BoundServiceCard
 */
@Composable
fun HybridServiceCard(
    viewModel: HybridServiceViewModel = hiltViewModel()
) {
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
    val progress by viewModel.playbackProgress.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startAndPlay()
        }
    }

    DisposableEffect(Unit) {
        viewModel.bindService()
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
                text = "3. Hybrid Service (Started & Bound)",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Combina FGS (supervivencia por notificación) con Bound (observabilidad por StateFlow).",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "${if (isPlaying) "🔊 Reproduciendo" else "⏸️ Pausado"} | Progreso: ${progress}s",
                style = MaterialTheme.typography.bodySmall
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                viewModel.startAndPlay()
                            }
                        }
                    ) {
                        Text("Play")
                    }
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.pause() }
                    ) {
                        Text("Pause")
                    }
                }

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.stopServiceCompletely() }
                ) {
                    Text("Stop (Destruir Servicio)")
                }
            }
        }
    }
}