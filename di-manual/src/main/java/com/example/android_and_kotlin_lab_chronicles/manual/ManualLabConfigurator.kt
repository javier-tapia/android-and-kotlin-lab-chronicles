package com.example.android_and_kotlin_lab_chronicles.manual

import android.content.Context
import android.content.pm.ApplicationInfo

/**
 * Contrato que define el acceso a la configuración e inspección del entorno
 * para el módulo de DI Manual.
 */
interface ManualLabConfigurator {
    fun getLabStatus(): String
}

/**
 * Implementación concreta del configurador de DI Manual.
 *
 * Determina el estado del entorno analizando las flags del [ApplicationInfo]
 * provisto por el [Context] de la aplicación.
 */
class CoreManualLabConfigurator(
    private val context: Context
) : ManualLabConfigurator {
    override fun getLabStatus(): String {
        val isDebuggable = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        val buildType = if (isDebuggable) "Debug" else "Release"

        return "Manual DI Active 🛠️ (Build: $buildType)"
    }
}
