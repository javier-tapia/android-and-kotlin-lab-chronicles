package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.navigation.back_pressed_handling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para experimentar con la interceptación del botón de atrás/gesto de retroceso.
 */
class BackPressedActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "Back Pressed Lab") {
                // UI para habilitar/deshabilitar el callback y probar el comportamiento
            }
        }
    }
}