package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.preferences

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para probar la persistencia de preferencias de usuario.
 * Permite comparar el comportamiento síncrono de SharedPreferences 
 * frente al flujo asíncrono de DataStore.
 */
class PreferencesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BaseLayoutScreen(title = "Preferences Lab") {
                // UI con formularios simples (Switch, TextField) para guardar y leer preferencias
            }
        }
    }
}