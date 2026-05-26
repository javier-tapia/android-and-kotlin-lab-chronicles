package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseRawScreen

/**
 * UI para experimentar con los flujos de navegación de Jetpack Compose.
 */
class NavigationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BaseRawScreen { NavigationHandlerLab() }
        }
    }
}
