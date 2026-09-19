package com.example.android_and_kotlin_lab_chronicles.experiments.dependency_injection.manual

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

/**
 * Punto de entrada para el laboratorio de Inyección de Dependencias Manual.
 *
 * Demuestra el patrón de *Service Locator/AppContainer* sin librerías externas.
 */
class ManualLabActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ManualLabScreen()
        }
    }
}
