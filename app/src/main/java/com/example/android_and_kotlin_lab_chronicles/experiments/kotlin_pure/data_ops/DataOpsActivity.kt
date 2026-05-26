package com.example.android_and_kotlin_lab_chronicles.experiments.kotlin_pure.data_ops

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para experimentar con operaciones de datos, JSON, Strings y Regex.
 */
class DataOpsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "Data Operations Lab") {
                // UI para invocar JsonOperationsLab, StringOperationsLab, etc.
            }
        }
    }
}