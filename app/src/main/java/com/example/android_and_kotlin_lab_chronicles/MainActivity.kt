package com.example.android_and_kotlin_lab_chronicles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
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

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    groupedExperiments.forEach { (topic, experiments) ->
                        val visibilityState = remember {
                            MutableTransitionState(initialState = false)
                        }
                        // Header de la sección
                        Text(
                            text = topic.topicName,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .clickable(onClick = {
                                    visibilityState.targetState = !visibilityState.targetState
                                })
                        )

                        // Cards de experimentos colapsables
                        experiments.forEach { experiment ->
                            AnimatedVisibility(
                                visibleState = visibilityState,
                                enter = expandVertically(animationSpec = tween(50)),
                                exit = shrinkVertically(animationSpec = tween(50))
                            ) {
                                ExperimentCard(experiment)
                            }
                        }
                    }
                }
            }
        }
    }
}
