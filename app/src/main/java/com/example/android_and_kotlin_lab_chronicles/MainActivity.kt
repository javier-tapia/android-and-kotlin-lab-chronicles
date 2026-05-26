package com.example.android_and_kotlin_lab_chronicles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen
import com.example.android_and_kotlin_lab_chronicles.core.ExperimentCard
import com.example.android_and_kotlin_lab_chronicles.core.ExperimentProvider.experiments

/**
 * Pantalla inicial y *router* del laboratorio.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaseLayoutScreen(title = "Lab Chronicles") {
                // Experimentos agrupados por tópico para mostrar los headers
                val groupedExperiments = experiments.groupBy { it.topic }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    groupedExperiments.forEach { (topic, experiments) ->
                        // Header de la sección
                        item {
                            Text(
                                text = topic.topicName,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        // Cards de experimentos
                        items(experiments) { exp ->
                            ExperimentCard(exp)
                        }
                    }
                }
            }
        }
    }
}
