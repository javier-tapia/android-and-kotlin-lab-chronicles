package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.system

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para experimentar con integraciones de sistema y Google Play Services.
 */
class SystemActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "System Integrations Lab") {
                // UI para probar: Notificaciones, Shortcuts, Updates y Reviews
            }
        }
    }
}