package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_sharing.file_sharing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para experimentar con el intercambio seguro de archivos.
 * 
 * Aquí probaremos cómo generar Content URIs usando FileProvider para evitar
 * el error 'FileUriExposedException' en Android 7.0+.
 */
class FileSharingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BaseLayoutScreen(title = "File Sharing Lab") {
                // UI para crear un archivo interno y compartirlo mediante Intent.ACTION_SEND
            }
        }
    }
}