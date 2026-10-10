package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.workmanager

import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

/**
 * Repositorio simulado que ejecuta una operación de larga duración (3 segundos)
 * y retorna un resultado booleano para evaluar el comportamiento de los workers en segundo plano.
 */
@Singleton
class SyncRepository @Inject constructor() {
    suspend fun performRemoteSync(): Boolean {
        delay(3000L.milliseconds)
        return true
    }
}
