package com.example.android_and_kotlin_lab_chronicles.experiments.multimedia.audio_only

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para experimentar con la grabación y reproducción de audio de bajo nivel.
 */
class AudioOnlyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "Audio Only Lab") {
                // UI para controlar: MediaPlayer, AudioRecord y monitoreo de AudioTrack
            }
        }
    }
}