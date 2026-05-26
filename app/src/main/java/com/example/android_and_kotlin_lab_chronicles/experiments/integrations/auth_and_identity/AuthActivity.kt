package com.example.android_and_kotlin_lab_chronicles.experiments.integrations.auth_and_identity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para experimentar con flujos de identidad, Google Login y protocolos OAuth/PKCE.
 */
class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaseLayoutScreen(title = "Auth & Identity Lab") {
                // UI para ejecutar: Google Login y Debug de PKCE
            }
        }
    }
}
