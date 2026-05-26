package com.example.android_and_kotlin_lab_chronicles.experiments.kotlin_pure.collections

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

class CollectionsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Estado para guardar lo que queremos imprimir en pantalla
            var logText by remember { mutableStateOf("Pulsa un botón para probar...") }

            BaseLayoutScreen(title = "Collections Lab") {
                // Fila de botones para diferentes pruebas
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    Button(onClick = { 
                        // Aquí invocas tu objeto
                        logText = runAddingExperiment() 
                    }) {
                        Text("Add/Remove")
                    }
                    // Añadirías más botones para Filtering, Grouping, etc.
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Nuestra "consola" en la pantalla
                Card(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = logText,
                        modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    // Función auxiliar para formatear la salida
    private fun runAddingExperiment(): String {
        val list = mutableListOf("Kotlin", "Java")
        list.add("Python")
        val result = "Lista inicial: $list\n"
        // Llamas a tu lógica del object
        // ...
        return result + "Operación completada."
    }
}