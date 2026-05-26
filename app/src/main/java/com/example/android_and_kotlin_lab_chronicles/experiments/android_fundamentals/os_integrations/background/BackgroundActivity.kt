package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para monitorear y ejecutar experimentos de tareas en segundo plano y receptores de sistema.
 */
class BackgroundActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "Background Lab") {
                // UI para controlar: WorkManager, Services, AlarmManager y BroadcastReceiver
            }
        }
    }
}