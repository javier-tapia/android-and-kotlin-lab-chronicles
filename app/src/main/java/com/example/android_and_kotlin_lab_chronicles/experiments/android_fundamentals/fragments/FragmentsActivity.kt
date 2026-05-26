package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.fragments

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para experimentar con la manipulación dinámica de Fragments.
 * NOTA: Heredamos de FragmentActivity (o AppCompatActivity) para soporte de Fragments.
 */
class FragmentsActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "Fragments Lab") {
                // UI para ejecutar: Replace, Add, PopBackStack, etc.
            }
        }
    }
}
