package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.alarm_manager

/**
 * Contrato de infraestructura responsable de interactuar con el servicio nativo del sistema
 * [android.app.AlarmManager] para la programación de tareas de ejecución estricta (*Time-sensitive tasks*).
 */
interface AlarmScheduler {
    /**
     * Programa una alarma exacta en un timestamp Unix específico.
     *
     * @param triggerAtMillis Momento exacto de ejecución en milisegundos (*Epoch time*).
     * @param payload Objeto de transferencia que encapsula los metadatos del evento.
     */
    fun schedule(triggerAtMillis: Long, payload: AlarmPayload)

    /**
     * Cancela una alarma previamente programada asociada a un identificador único.
     *
     * @param payload Identificador del payload para invalidar el [android.app.PendingIntent] equivalente.
     */
    fun cancel(payload: AlarmPayload)

    /**
     * Verifica si el sistema operativo otorga el permiso especial requerido para programar
     * alarmas exactas (Android 12+ / API 31+).
     *
     * @return `true` si el sistema permite alarmas exactas o si la versión de Android es anterior a
     * la 12; `false` en caso contrario.
     */
    fun canScheduleExactAlarms(): Boolean
}