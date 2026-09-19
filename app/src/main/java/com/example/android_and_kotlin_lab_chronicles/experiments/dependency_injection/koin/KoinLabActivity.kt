package com.example.android_and_kotlin_lab_chronicles.experiments.dependency_injection.koin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

/**
 * Activity aislada para experimentar con Koin.
 *
 * A diferencia de Hilt, esta clase **NO** lleva la anotación `@AndroidEntryPoint`.
 */
class KoinLabActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KoinLabScreen()
        }
    }
}
