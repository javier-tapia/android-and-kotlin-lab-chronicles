package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.animations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para experimentar visualmente con las APIs de animación de Compose.
 */
class AnimationsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "Compose Animations Lab") {
                AnimationsLab()
            }
        }
    }
}
