package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.file_storage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para gestionar y visualizar archivos creados en el laboratorio.
 * Permite probar la creación de archivos y verificar su persistencia 
 * en diferentes directorios del sistema.
 */
class FileStorageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BaseLayoutScreen(title = "File Storage Lab") {
                // UI para crear archivos .txt o imágenes y ver dónde se guardan
            }
        }
    }
}