package com.example.android_and_kotlin_lab_chronicles.experiments.kotlin_pure.async_and_concurrency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para ejecutar y monitorear experimentos de asincronía y concurrencia.
 */
class AsyncActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "Async & Concurrency Lab") {
                // UI para probar Corrutinas, Flows y Mutex
            }
        }
    }
}