package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.broadcastreceiver

import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * Pantalla de experimentación del laboratorio para visualizar en tiempo real el comportamiento
 * de los *receivers* dinámicos y su correcta gestión de recursos para evitar fugas de memoria
 * (*Context Leaks*), como también de los *receivers* estáticos.
 *
 * ###### Pruebas con *receivers* estáticos
 * Para no tener que reiniciar el dispositivo manualmente:
 *
 * 1°: Reiniciarlo con `adb`:
 * ```bash
 * # Iniciar el emulador como root (depende de que use una imagen de Google APIs en lugar de Google Play)
 * adb root
 *
 * # Reiniciar el demonio de ADB con privilegios de root para emitir el comando sin restricciones
 * adb shell am broadcast -a android.intent.action.BOOT_COMPLETED -p com.example.android_and_kotlin_lab_chronicles
 * ```
 * 2°: Refrescar la UI con el botón "Actualizar Registro de Boot"
 */
@Composable
fun BroadcastReceiverLab() {
    val context = LocalContext.current
    var eventLog by rememberSaveable { mutableStateOf("Monitoreando eventos dinámicos...") }
    var staticBootLog by rememberSaveable { mutableStateOf(StaticBootReceiver.getLastBootRecord(context)) }

    // Ciclo de vida seguro acoplado al scope del Composable para el receptor dinámico
    DisposableEffect(context) {
        val receiver = DynamicSystemStateReceiver { message ->
            eventLog = message
        }

        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }

        // Registro seguro conforme a los estándares de seguridad de Android Moderno
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, intentFilter, RECEIVER_NOT_EXPORTED)
        } else {
            context.registerReceiver(receiver, intentFilter)
        }

        // Limpieza obligatoria del recurso al salir de la composición
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                        // Refresca el estado leído de SharedPreferences
                        staticBootLog = StaticBootReceiver.getLastBootRecord(context)
                    }
                ) {
                    Text(text = "Actualizar Registro de Boot")
                }
            }
        }
    }
}
