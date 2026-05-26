package com.example.android_and_kotlin_lab_chronicles.experiments.kotlin_pure.design_patterns

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para visualizar la ejecución de diferentes patrones de diseño.
 * Permite comparar las variantes Clásica, Idiomática y DSL en tiempo real.
 */
class DesignPatternsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "Design Patterns Lab") {
                // UI con Tabs o Chips para alternar entre Creational, Structural y Behavioral
            }
        }
    }
}