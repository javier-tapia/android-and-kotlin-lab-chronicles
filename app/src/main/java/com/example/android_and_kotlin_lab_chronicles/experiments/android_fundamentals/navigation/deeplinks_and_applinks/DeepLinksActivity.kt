package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.navigation.deeplinks_and_applinks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI destino para probar la recepción de Deep Links y App Links.
 */
class DeepLinksActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "Deep Links Lab") {
                // UI para mostrar los datos recibidos a través de la URI
            }
        }
    }
}