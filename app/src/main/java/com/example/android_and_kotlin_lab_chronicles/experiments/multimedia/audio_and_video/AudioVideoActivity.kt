package com.example.android_and_kotlin_lab_chronicles.experiments.multimedia.audio_and_video

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para experimentar con la reproducción de streaming y captura de medios.
 */
class AudioVideoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "Audio & Video Lab") {
                // UI para: Player (ExoPlayer), Camera Preview (CameraX) y controles de grabación
            }
        }
    }
}
