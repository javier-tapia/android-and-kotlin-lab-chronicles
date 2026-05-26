package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.lifecycle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para experimentar con el ciclo de vida de la Activity y la muerte del proceso.
 * Permite visualizar el PID actual y probar la restauración de estados.
 */
class LifecycleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "Lifecycle & Process Death Lab") {
                // UI para mostrar PID actual y campos de texto para probar restauración
            }
        }
    }
}