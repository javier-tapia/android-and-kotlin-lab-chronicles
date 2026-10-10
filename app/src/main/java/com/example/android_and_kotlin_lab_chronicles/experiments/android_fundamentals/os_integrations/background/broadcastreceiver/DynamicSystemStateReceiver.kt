package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * ### *Dynamic System State Receiver* (Receptor Dinámico *Context-Registered*)
 *
 * Receptor acoplado al ciclo de vida del componente que lo registra (típicamente una *Activity* o
 * un *Composable* vía efecto).
 *
 * #### Consideraciones de Seguridad en IPC (Android 13+ / API 33+, AKA ``TIRAMISU``):
 * Es mandatorio especificar explícitamente la visibilidad del receptor mediante los *flags* de
 * seguridad: [Context.RECEIVER_EXPORTED] (si recibe *intents* de otras apps) o
 * [Context.RECEIVER_NOT_EXPORTED] (estrictamente privado para la propia aplicación). Omitir esto
 * genera excepciones fatales en *runtime* en APIs recientes.
 */
class DynamicSystemStateReceiver(
    private val onSystemStateChanged: (String) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BATTERY_LOW -> onSystemStateChanged("Alerta: Batería baja detectada.")
            Intent.ACTION_POWER_CONNECTED -> onSystemStateChanged("Estado: Alimentación conectada.")
            Intent.ACTION_POWER_DISCONNECTED -> onSystemStateChanged("Estado: Alimentación desconectada.")
        }
    }
}
