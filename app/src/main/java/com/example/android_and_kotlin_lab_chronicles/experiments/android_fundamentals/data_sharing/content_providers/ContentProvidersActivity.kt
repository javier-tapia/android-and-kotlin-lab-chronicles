package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_sharing.content_providers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para interactuar con Content Providers.
 * Permite visualizar el flujo de datos entre el proveedor y el consumidor.
 */
class ContentProvidersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BaseLayoutScreen(title = "Content Providers Lab") {
                // UI para ejecutar queries y ver los resultados en una lista
            }
        }
    }
}