package com.example.android_and_kotlin_lab_chronicles.experiments.multimedia.processing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para experimentar con algoritmos de procesamiento y modelos de ML Kit.
 */
class ProcessingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "Media Processing Lab") {
                // UI para: Procesamiento de video (FFmpeg), Filtros (OpenCV) y Análisis (ML Kit)
            }
        }
    }
}