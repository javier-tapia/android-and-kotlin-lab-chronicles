package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.alarm_manager

/**
 * Modelo de datos inmutable que encapsula los atributos necesarios para identificar y renderizar
 * una alarma programada en el sistema.
 */
data class AlarmPayload(
    val id: Int,
    val title: String,
    val message: String
)