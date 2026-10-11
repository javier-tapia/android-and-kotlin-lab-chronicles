package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services.components

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android_and_kotlin_lab_chronicles.core.utils.CustomLogger
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services.MusicPlayerHybridService
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * A este tipo de servicio "híbrido", también se lo conoce como ***Started & Bound***.
 *
 * Combina FGS (supervivencia por notificación) con *Bound* (observabilidad por ``StateFlow``).
 *
 * @see ForegroundServiceCard
 * @see BoundServiceCard
 */
@Composable
fun HybridServiceCard() {
    val context = LocalContext.current

    var serviceInstance by remember { mutableStateOf<MusicPlayerHybridService?>(null) }
    var isBound by remember { mutableStateOf(false) }

    val isPlaying by (serviceInstance?.isPlaying
        ?: MutableStateFlow(false)).collectAsStateWithLifecycle()
    val progress by (serviceInstance?.playbackProgress
        ?: MutableStateFlow(0)).collectAsStateWithLifecycle()

    val serviceConnection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as? MusicPlayerHybridService.LocalBinder
                serviceInstance = binder?.getService()
                isBound = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                serviceInstance = null
                isBound = false
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            MusicPlayerHybridService.start(context)
            if (!isBound) {
                val intent = Intent(context, MusicPlayerHybridService::class.java)
                context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            }
        }
    }

    // Cuando se compone la pantalla, se realiza el binding
    DisposableEffect(Unit) {
        val intent = Intent(context, MusicPlayerHybridService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        onDispose {
            if (isBound) {
                try {
                    context.unbindService(serviceConnection)
                } catch (e: IllegalArgumentException) {
                    CustomLogger.log(
                        "HybridServiceCard",
                        "Advertencia: El servicio ya estaba desvinculado (${e.message})"
                    )
                }
            }
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

            // Distribución vertical o espaciada para evitar que los botones se corten
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
                            // 1. Se asigna el servicio híbrido al pending launcher
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                MusicPlayerHybridService.start(context)
                            }

                            // 2. Se asegura la re-vinculación si el servicio había muerto por Stop
                            if (!isBound) {
                                val intent = Intent(context, MusicPlayerHybridService::class.java)
                                context.bindService(
                                    intent,
                                    serviceConnection,
                                    Context.BIND_AUTO_CREATE
                                )
                            }
                        }
                    ) {
                        Text("Play")
                    }
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = { MusicPlayerHybridService.pause(context) }
                    ) {
                        Text("Pause")
                    }
                }

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        // 1. Se rompe el enlace Bound para liberar al servicio de la UI
                        if (isBound) {
                            try {
                                context.unbindService(serviceConnection)
                            } catch (_: IllegalArgumentException) {
                            }
                            isBound = false
                            serviceInstance = null
                        }
                        // 2. Se ordena la destrucción del FGS Started
                        MusicPlayerHybridService.stop(context)
                    }
                ) {
                    Text("Stop (Destruir Servicio)")
                }
            }
        }
    }
}