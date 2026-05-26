package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para interactuar con los experimentos de persistencia en bases de datos.
 * Permite realizar operaciones CRUD y visualizar los datos en tiempo real.
 */
class DatabasesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaseLayoutScreen(title = "Databases Lab") {
                // UI para Insertar, Consultar y Borrar registros en Room/Realm
            }
        }
    }
}