package com.example.android_and_kotlin_lab_chronicles.experiments.multimedia.audio_only

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para experimentar con la grabación y reproducción de audio de bajo nivel.
 */
class AudioOnlyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // TODO: Validar si esto va acá
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED) {
            // Do something
        }

        setContent {
            BaseLayoutScreen(title = "Audio Only Lab") {
                // UI para controlar: MediaPlayer, AudioRecord y monitoreo de AudioTrack
            }
        }
    }
}