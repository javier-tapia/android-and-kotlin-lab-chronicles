package com.example.android_and_kotlin_lab_chronicles

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Se incorpora la anotación [@HiltAndroidApp] para disparar la generación de código de Hilt.
 * Este componente actúa como el contenedor de dependencias a nivel de aplicación (SingletonComponent)
 * y es el encargado de adjuntar el ciclo de vida del grafo de DI al ciclo de vida del proceso de la App.
 * * Sin esta anotación, KSP no generará los componentes base y cualquier intento de inyección 
 * en cascada ([dagger.hilt.android.AndroidEntryPoint]) fallará en tiempo de compilación o runtime.
 */
@HiltAndroidApp
class LabApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Punto de inicialización global de infraestructura si fuera necesario en el futuro.
    }
}
