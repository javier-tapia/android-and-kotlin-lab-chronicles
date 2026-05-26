package com.example.android_and_kotlin_lab_chronicles.experiments.integrations.firebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para interactuar con los diferentes servicios de Firebase integrados.
 */
class FirebaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaseLayoutScreen(title = "Firebase Integrations Lab") {
                // UI para probar Auth, FCM y Remote Config
            }
        }
    }
}
