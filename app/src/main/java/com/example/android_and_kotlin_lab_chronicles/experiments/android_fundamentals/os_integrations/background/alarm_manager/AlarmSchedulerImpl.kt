package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.alarm_manager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.android_and_kotlin_lab_chronicles.core.utils.CustomLogger.log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.jvm.java

/**
 * Implementación concreta de [AlarmScheduler] orientada a la plataforma Android,
 * responsable de interactuar directamente con el sistema operativo a través de [AlarmManager].
 *
 * #### Consideraciones de Arquitectura y Seguridad:
 * - ***Defensive Programming***: Valida [AlarmManager.canScheduleExactAlarms] en Android 12+ (API 31+)
 * para prevenir excepciones fatales de tipo [SecurityException].
 * - ***Security Compliance***: Emplea [PendingIntent.FLAG_IMMUTABLE] por defecto para cumplir con
 * los estándares de seguridad de Android 12+ (API 31+).
 *
 * @property context Contexto de aplicación seguro (inyectado con `@ApplicationContext`) utilizado
 * para la obtención de servicios del sistema y la creación de [PendingIntent].
 */
class AlarmSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AlarmScheduler {

    /**
     * Instancia del servicio del sistema [AlarmManager] obtenida a partir del contexto de la
     * aplicación, utilizada como el componente central de infraestructura para la programación,
     * gestión y cancelación de alarmas.
     */
    private val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(triggerAtMillis: Long, payload: AlarmPayload) {
        // Validación estricta de seguridad para Android 12+ (API 31+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            return
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.EXTRA_ALARM_ID, payload.id)
            putExtra(AlarmReceiver.EXTRA_ALARM_TITLE, payload.title)
            putExtra(AlarmReceiver.EXTRA_ALARM_MESSAGE, payload.message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            payload.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            // Programar para el tiempo exacto incluso en reposo profundo ('RTC_WAKEUP').
            // 'triggerAtMillis' es el tiempo futuro en milisegundos ('System.currentTimeMillis()' + delay)
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
            AlarmPreferencesDataSource(context).saveAlarm(payload, triggerAtMillis)
        } catch (e: SecurityException) {
            // Mitigación defensiva ante revocación dinámica del permiso en runtime
            log("AlarmSchedulerImpl", "Revocación del permiso en runtime: ${e.message}")
            // 1. Enviar evento a Crashlytics / Analytics
            // 2. Notificar al ViewModel / Capa de Dominio para actualizar UI
            // 3. Opcional: Fallback a AlarmManager inexacto si aplica
        }
    }

    override fun cancel(payload: AlarmPayload) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            payload.id,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        pendingIntent?.let {
            alarmManager.cancel(it)
            AlarmPreferencesDataSource(context).removeAlarm(payload.id)
        }
    }

    override fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }
}