package com.example.android_and_kotlin_lab_chronicles.experiments.dependency_injection.hilt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

/**
 * Se añade ``@AndroidEntryPoint`` para transformar esta Activity en un consumidor del grafo de Hilt.
 * Bajo el capot, Hilt genera una clase intermedia (mediante transformación de *bytecode*) que
 * intercepta el ciclo de vida (``onCreate``) para realizar la inyección de dependencias antes de
 * que se ejecute el código de la clase.
 */
@AndroidEntryPoint
class HiltLabActivity : ComponentActivity() {
    // Se inyecta el ViewModel delegando la creación al ciclo de vida de la Activity
    private val viewModel: HiltLabViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Validación en Logcat para certificar runtime sin romper la UI existente
        viewModel.logHiltFromActivity()

        enableEdgeToEdge()

        setContent {
            HiltLabScreen()
        }
    }
}
