package com.example.android_and_kotlin_lab_chronicles.experiments.geolocation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI unificada para experimentos de Geolocalización y Mapas.
 */
class GeolocationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "Geolocation & Maps Lab") {
                // UI para alternar entre:
                // 1. Rastreo de ubicación (Coordenadas)
                // 2. Visualización de Mapa (Google Maps)
                // 3. Búsqueda de Lugares (Places API)
            }
        }
    }
}