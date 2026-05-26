package com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.monitoring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para experimentar con el reporte de errores en tiempo real y analíticas.
 * Permite simular fallos controlados para verificar la integración con Sentry y Segment.
 */
class MonitoringActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "Monitoring Lab (Sentry & Segment)") {
                // UI para: Forzar Crash, Enviar error capturado y Trackear eventos
            }
        }
    }
}