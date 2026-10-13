package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.alarm_manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.android_and_kotlin_lab_chronicles.core.utils.CustomLogger.log

/**
 * Receptor del sistema encargado de interceptar eventos de arranque (`ACTION_BOOT_COMPLETED`,
 * `QUICKBOOT_POWERON` o `MY_PACKAGE_REPLACED`) para restaurar las alarmas programadas que se pierden
 * de la memoria del [android.app.AlarmManager] tras un reinicio del dispositivo.
 *
 * Consulta la fuente de persistencia local ([AlarmPreferencesDataSource]) para evaluar
 * qué alarmas siguen vigentes, reprogramarlas automáticamente y limpiar aquellas cuyo
 * tiempo de ejecución ya haya expirado mientras el dispositivo estuvo apagado.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON" ||
            intent.action == Intent.ACTION_MY_PACKAGE_REPLACED
        ) {

            log(TAG, "Reinicio detectado. Restaurando alarmas...")

            val scheduler = AlarmSchedulerImpl(context)
            val dataSource = AlarmPreferencesDataSource(context)

            // Recupera y reprograma todas las activas persistidas
            val activeAlarms = dataSource.getAllAlarms()
            val currentTime = System.currentTimeMillis()

            activeAlarms.values.forEach { alarm ->
                // Filtra las que ya expiraron durante el tiempo que el dispositivo estuvo apagado
                if (alarm.triggerTime > currentTime) {
                    val payload = AlarmPayload(alarm.id, alarm.title, alarm.message)
                    scheduler.schedule(alarm.triggerTime, payload)
                    log(TAG, "Alarma ID ${alarm.id} reprogramada exitosamente.")
                } else {
                    dataSource.removeAlarm(alarm.id)
                }
            }
        }
    }

    companion object {
        private const val TAG = "BootReceiver"
    }
}