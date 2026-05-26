package com.example.android_and_kotlin_lab_chronicles.experiments.kotlin_pure.result_handling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para comparar el manejo de errores funcional con Either y la clase Result nativa.
 */
class ResultHandlingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaseLayoutScreen(title = "Result Handling Lab") {
                // UI para ejecutar los experimentos de EitherLab y KotlinResultLab
            }
        }
    }
}
