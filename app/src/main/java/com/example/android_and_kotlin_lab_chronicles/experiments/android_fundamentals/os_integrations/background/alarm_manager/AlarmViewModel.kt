package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.alarm_manager

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Encargado de gestionar la lógica de presentación y el estado.
 *
 * Actúa como intermediario entre la interfaz de usuario ([AlarmManagerLab]) y la capa de
 * infraestructura a través de la abstracción [AlarmScheduler], cumpliendo con los principios
 * de Inversión de Dependencias mediante Hilt.
 *
 * @property alarmScheduler Abstracción inyectada para la programación y gestión de alarmas.
 */
@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    /**
     * Verifica si la aplicación cuenta con la capacidad de programar alarmas exactas,
     * delegando la consulta a la capa de infraestructura.
     *
     * @return `true` si el sistema permite programar alarmas exactas; `false` en caso contrario.
     */
    fun canScheduleExactAlarms(): Boolean =
        alarmScheduler.canScheduleExactAlarms()

    /**
     * Caso de uso simplificado del laboratorio: construye un *payload* de prueba y programa
     * una alarma exacta para dispararse exactamente en 5 segundos.
     */
    fun onScheduleAlarmClicked() {
        val triggerTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5)
        val payload = AlarmPayload(
            id = 42,
            title = "Alarma de Prueba [Lab]",
            message = "El disparador exacto ha funcionado correctamente tras 5s."
        )
        alarmScheduler.schedule(triggerTime, payload)
    }
}