package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.lifecycle

/**
 * Experimentos: Ciclo de vida del sistema y muerte de procesos.
 * 
 * Temas:
 * - Process Death: Simulación y recuperación de estado.
 * - Identificación de procesos (PID y DEFAULT_PID).
 * - Uso de SavedStateHandle para persistencia ante muerte de proceso.
 * - Callback de detección de recreación de proceso.
 */
object LifecycleLab {
    
    const val DEFAULT_PID = -1
    
    /**
     * Interfaz para gestionar la detección de la muerte del proceso.
     */
    interface ProcessDeathHandler {
        fun onProcessCreated(pid: Int)
        fun isFirstLaunch(currentPid: Int): Boolean
    }
}