package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background

import androidx.compose.runtime.Composable
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.alarmmanager.AlarmManagerLab
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.broadcastreceiver.BroadcastReceiverLab
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services.ServicesLab
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.workmanager.WorkManagerLab

/**
 * ### Ejecución en segundo plano y comunicación con el sistema
 * 
 * #### Temas:
 * - ***WorkManager***: Tareas persistentes.
 * - ***BroadcastReceiver***: Escucha de eventos del sistema (Batería, Conectividad, *Boot*).
 * - ***Services***: Tareas en primer plano (*Foreground*) y vinculadas (*Bound*).
 * - ***AlarmManager***: Programación de tareas en momentos exactos (*Exact Alarms*).
 */
@Composable
fun BackgroundLab() {
    SamplesShowcase(
        { WorkManagerLab() },
        { BroadcastReceiverLab() },
        { ServicesLab() },
        { AlarmManagerLab() },
    )
}
