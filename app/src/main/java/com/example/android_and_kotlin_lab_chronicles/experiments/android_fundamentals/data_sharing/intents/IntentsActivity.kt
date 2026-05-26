package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_sharing.intents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para experimentar con el envío y recepción de mensajes mediante Intents.
 */
class IntentsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "Intents Lab") {
                // Botones para probar:
                // 1. Abrir navegador (Implícito)
                // 2. Compartir texto (Implícito)
                // 3. Llamar a otra actividad propia (Explícito)
            }
        }
    }
}