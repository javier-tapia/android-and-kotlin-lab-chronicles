package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_sharing.media_store

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para visualizar y gestionar medios del sistema.
 * Permite probar el escaneo de la galería y la visualización
 * de metadatos de archivos multimedia.
 */
class MediaStoreActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "Media Store Lab") {
                // UI para mostrar una lista o grid de imágenes/videos del dispositivo
            }
        }
    }
}