package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.side_effects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen
import dagger.hilt.android.AndroidEntryPoint

/**
 * UI para experimentar y visualizar el ciclo de vida de los Side Effects en Compose.
 */
@AndroidEntryPoint
class SideEffectsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BaseLayoutScreen(title = "Compose Side Effects Lab") {
                SideEffectsLab()
            }
        }
    }
}
