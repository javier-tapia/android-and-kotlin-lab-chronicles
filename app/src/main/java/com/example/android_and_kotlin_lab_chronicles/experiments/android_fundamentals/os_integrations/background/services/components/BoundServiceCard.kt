package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services.components

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
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
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services.FileDownloadBoundService
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * ###### Componentes clave y su funcionamiento técnico
 * - `ServiceConnection`: gestiona el puente de enlace (el *binding*) en tiempo real para que la UI
 * pueda conversar directamente con un *Bound Service*.
 * - Se utliza un `DisposableEffect` para desconectar el servicio cuando el Composable abandona la
 * pantalla (por ejemplo, al navegar hacia atrás o por reciclaje en una lista).
 */
@Composable
fun BoundServiceCard() {
    val context = LocalContext.current

    /**
     * El servicio aún no existe en este punto, sino que se crea en el *callback* ``onServiceConnected``
     * del ``ServiceConnection`` (invocado luego de llamar a `context.bindService()`) y es ahí donde se
     * asigna a esta variable.
     *
     * Guardar la ``serviceInstance`` en un estado no solo sirve para colectar los flujos de progreso;
     * también se necesita para mandarle órdenes al servicio desde los botones (`serviceInstance?.startDownload()`).
     */
    var serviceInstance by remember { mutableStateOf<FileDownloadBoundService?>(null) }
    var isBound by remember { mutableStateOf(false) }

    val downloadProgress by (serviceInstance?.progress
        ?: MutableStateFlow(0)).collectAsStateWithLifecycle()
    val isDownloading by (serviceInstance?.isDownloading
        ?: MutableStateFlow(false)).collectAsStateWithLifecycle()

    val serviceConnection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as? FileDownloadBoundService.LocalBinder
                serviceInstance = binder?.getService()
                isBound = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                serviceInstance = null
                isBound = false
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (isBound) {
                try {
                    context.unbindService(serviceConnection)
                } catch (e: IllegalArgumentException) {
                    CustomLogger.log(
                        "BoundServiceCard",
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
                        onClick = {
                            if (!isBound) {
                                val intent = Intent(
                                    context,
                                    FileDownloadBoundService::class.java
                                )
                                context.bindService(
                                    intent,
                                    serviceConnection,
                                    Context.BIND_AUTO_CREATE
                                )
                            }
                        },
                        enabled = !isBound
                    ) {
                        Text("Bind")
                    }
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (isBound) {
                                context.unbindService(serviceConnection)
                                isBound = false
                                serviceInstance = null
                            }
                        },
                        enabled = isBound
                    ) {
                        Text("Unbind")
                    }
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    serviceInstance?.startDownload()
                },
                enabled = isBound && !isDownloading
            ) {
                Text("Descargar")
            }
        }
    }
}
