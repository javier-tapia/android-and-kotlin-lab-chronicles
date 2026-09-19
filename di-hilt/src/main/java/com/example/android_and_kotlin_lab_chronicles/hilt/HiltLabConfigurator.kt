package com.example.android_and_kotlin_lab_chronicles.hilt

interface HiltLabConfigurator {
    fun getEnvironmentName(): String
}

/**
 * Implementación concreta que simula ser un componente complejo de configuración.
 */
class CoreHiltLabConfigurator(
    private val isDebug: Boolean
) : HiltLabConfigurator {
    override fun getEnvironmentName(): String {
        val mode = if (isDebug) "DEBUG (Desarrollo)" else "RELEASE (Producción)"
        return "Entorno de Laboratorio Activo: $mode"
    }
}
