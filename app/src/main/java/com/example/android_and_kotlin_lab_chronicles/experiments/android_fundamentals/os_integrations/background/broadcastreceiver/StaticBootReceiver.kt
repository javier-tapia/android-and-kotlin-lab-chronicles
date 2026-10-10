package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.edit
import com.example.android_and_kotlin_lab_chronicles.core.utils.CustomLogger.log
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * ## *Static Boot Receiver* (Receptor Estático de Inicio)
 *
 * Componente de infraestructura declarado en el *Manifest* optimizado para interceptar la señal global
 * de finalización de arranque del sistema (`ACTION_BOOT_COMPLETED`).
 *
 * ### Contexto Técnico y Restricciones del OS
 * - **Instanciación Efímera**: El sistema operativo instancia esta clase bajo demanda al coincidir
 * el filtro de intents, ejecutando [onReceive] en el *Main Looper* (*Main Thread*).
 * - **Restricción de Ciclo de Vida (Android 8.0+ / API 26+)**: Los receptores estáticos para eventos
 * implícitos fueron restringidos drásticamente por el *runtime* de Android para mitigar el consumo
 * de batería (*Background Execution Limits*).
 * - ***Timeouts* de Ejecución**: El procesamiento síncrono en [onReceive] está limitado a ~10 segundos.
 * Cualquier operación pesada, de red o persistencia compleja debe delegarse de inmediato a [androidx.work.WorkManager].
 *
 * ### Consideraciones de Seguridad (*Defense in Depth*)
 * - Expuesto (`exported = true`) exclusivamente para recibir el *broadcast* del *kernel* o Sistema Operativo.
 * - Requiere validación estricta de la acción recibida para prevenir ataques suplantación de
 * *intents* (*Intent Spoofing*).
 */
class StaticBootReceiver : BroadcastReceiver() {

    /**
     * Intercepta y procesa el evento del sistema de forma defensiva.
     *
     * @param context Contexto de la aplicación entregado por el sistema.
     * @param intent [Intent] entrante que contiene la acción validada por el filtro del *Manifest*.
     */
    override fun onReceive(context: Context, intent: Intent) {
        // Blindaje de seguridad: Validación estricta de la acción esperada.
        // Ambas deben estar declaradas en el Manifest
        if (intent.action != Intent.ACTION_BOOT_COMPLETED &&
            intent.action != "android.intent.action.QUICKBOOT_POWERON"
        ) {
            log(TAG, "Intento de invocación con acción no reconocida: ${intent.action}")
            return
        }

        // Formateo moderno de fecha y hora con 'java.time'
        val currentDateTime = ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"))
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))

        log(
            TAG,
            "Señal BOOT_COMPLETED capturada de forma segura. Inicializando tareas de fondo diferidas."
        )

        // Persiste el evento para poder renderizarlo luego en la UI del Lab
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putString(KEY_LAST_BOOT_TIME, "Boot detectado: $currentDateTime") }
    }

    companion object {
        private const val TAG = "StaticBootReceiver"
        private const val PREFS_NAME = "boot_receiver_prefs"
        private const val KEY_LAST_BOOT_TIME = "last_boot_timestamp"

        /**
         * Permite consultar el último registro de arranque guardado por el receptor estático.
         */
        fun getLastBootRecord(context: Context): String {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return prefs.getString(KEY_LAST_BOOT_TIME, "Ningún arranque registrado aún.")
                ?: "Sin registro"
        }
    }
}
