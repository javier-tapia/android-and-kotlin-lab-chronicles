package com.example.android_and_kotlin_lab_chronicles.experiments.multimedia.streaming

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para experimentar con la reproducción de diferentes protocolos de Streaming.
 */
class StreamingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "Media Streaming Lab") {
                // UI para probar: HLS, DASH, WebRTC y monitoreo de Cache
            }
        }
    }
}