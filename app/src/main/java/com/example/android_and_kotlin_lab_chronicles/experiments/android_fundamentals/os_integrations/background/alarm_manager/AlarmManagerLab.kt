package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.alarm_manager

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

/**
 * Gestiona de forma reactiva los flujos de permisos requeridos por la plataforma:
 * - El permiso especial de alarmas exactas (`SCHEDULE_EXACT_ALARM` en Android 12+).
 * - El permiso de *runtime* para notificaciones (`POST_NOTIFICATIONS` en Android 13+).
 *
 * Toda la lógica de negocio y ejecución de comandos de infraestructura se encuentra
 * estrictamente delegada al [AlarmViewModel], manteniendo la interfaz libre de acoplamientos directos.
 *
 * @param viewModel Instancia del [AlarmViewModel] encargada de proveer estado y manejar acciones,
 * inyectada automáticamente mediante Hilt.
 */
@Composable
fun AlarmManagerLab(
    viewModel: AlarmViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    /**
     * Estado reactivo local para reflejar si se posee el permiso especial de alarmas exactas.
     */
    var canScheduleExact by remember {
        mutableStateOf(viewModel.canScheduleExactAlarms())
    }

    /**
     * Launcher para redirigir al usuario a los Ajustes del Sistema (Android 12+).
     */
    val exactAlarmsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        // Se ejecuta al regresar de Ajustes, actualizando el estado de forma reactiva
        canScheduleExact = viewModel.canScheduleExactAlarms()
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.onScheduleAlarmClicked()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "AlarmManagerLab", style = MaterialTheme.typography.titleLarge)

        if (!canScheduleExact) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Se requiere el permiso especial de Alarmas Exactas (Android 12+).")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            val intent = Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                                data = "package:${context.packageName}".toUri()
                            }
                            exactAlarmsPermissionLauncher.launch(intent)
                        }
                    }) {
                        Text("Conceder Permiso en Ajustes")
                    }
                }
            }
        }

        Button(
            enabled = canScheduleExact,
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    viewModel.onScheduleAlarmClicked()
                }
            }
        ) {
            Text("Programar Alarma (5 segundos)")
        }
    }
}