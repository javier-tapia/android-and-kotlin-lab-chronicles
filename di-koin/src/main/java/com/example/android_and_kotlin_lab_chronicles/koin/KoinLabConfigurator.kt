package com.example.android_and_kotlin_lab_chronicles.koin

import android.content.Context
import android.content.pm.ApplicationInfo

/**
 * Contrato que define la configuración del entorno para la prueba de Koin.
 */
interface KoinLabConfigurator {
    fun isDebugMode(): Boolean
    fun getEnvironmentName(): String
}

/**
 * Implementación concreta que inspecciona las flags de la aplicación mediante el [Context].
 *
 * A diferencia de Hilt, donde el contexto se inyecta con `@ApplicationContext`, Koin resuelve
 * la referencia del [Context] mediante la función `get()` en tiempo de ejecución, siempre que
 * se haya declarado `androidContext(...)` en la inicialización de `startKoin`.
 */
class CoreKoinLabConfigurator(
    private val context: Context
) : KoinLabConfigurator {

    override fun isDebugMode(): Boolean {
        return (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

    override fun getEnvironmentName(): String {
        return if (isDebugMode()) "Entorno Koin: DEBUG" else "Entorno Koin: RELEASE"
    }
}
