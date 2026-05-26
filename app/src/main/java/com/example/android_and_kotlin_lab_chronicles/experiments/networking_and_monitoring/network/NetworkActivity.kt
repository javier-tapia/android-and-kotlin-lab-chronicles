package com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para ejecutar peticiones de red y comparar el rendimiento y sintaxis entre Retrofit y Ktor.
 */
class NetworkActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "Networking Lab (Retrofit & Ktor)") {
                // UI para: Disparar peticiones, ver JSON de respuesta y monitorear logs
            }
        }
    }
}