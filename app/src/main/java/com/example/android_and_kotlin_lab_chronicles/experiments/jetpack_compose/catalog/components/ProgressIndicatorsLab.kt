package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase

/**
 * Un componente de _progress_ se usa para dar _feedback_ al usuario cuando la aplicación está realizando
 * operaciones por detrás.
 *
 * Hay dos tipos de indicadores: ``LinearProgressIndicator`` y ``CircularProgressIndicator``.
 */
@Composable
fun ProgressIndicatorsLab() {
    SamplesShowcase(
        { ProgressIndicatorWithManualChangeSample() },
        { ProgressIndicatorLoadingSample() }
    )
}

@Composable
fun ProgressIndicatorWithManualChangeSample() {
    var progressStatus by rememberSaveable { mutableFloatStateOf(0f) }
    LinearProgressIndicator(progress = { progressStatus })

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = { progressStatus += 0.1f }) {
            Text(text = "Incrementar")
        }
        Button(onClick = { progressStatus -= 0.1f }) {
            Text(text = "Reducir")
        }
    }
}

@Composable
fun ProgressIndicatorLoadingSample() {
    var showLoading by rememberSaveable { mutableStateOf(false) }
    if (showLoading) {
        CircularProgressIndicator(color = Color.Red, strokeWidth = 10.dp)
        LinearProgressIndicator(
            modifier = Modifier.padding(top = 32.dp),
            color = Color.Red,
            trackColor = Color.Green
        )
    }

    Button(onClick = { showLoading = !showLoading }) {
        Text(text = "Cargar perfil")
    }
}
