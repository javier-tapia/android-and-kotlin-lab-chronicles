package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase

/**
 * Experimentos con el componente Switch para estados binarios (On/Off).
 *
 * Se le debe ***indicar un estado*** y la ***acción a realizar***
 * cuando el estado del componente cambia.
 */
@Composable
fun SwitchLab() {
    SamplesShowcase(
        { SwitchSample() },
        { CustomThumbSwitchSample() }
    )
}

@Composable
fun SwitchSample() {
    var state by rememberSaveable { mutableStateOf(false) }

    Switch(
        checked = state,
        onCheckedChange = { state = it },
        // Si se quiere deshabilitar, hay que setear el enabled en false
        // enabled = false,
        colors = SwitchDefaults.colors(
            uncheckedThumbColor = Color.Red,
            uncheckedTrackColor = Color.Magenta,
            checkedThumbColor = Color.Green,
            checkedTrackColor = Color.Cyan
            // Se pueden setear colores para cuando está deshabilitado
            // disabledCheckedThumbColor = Color.Yellow,
            // disabledUncheckedThumbColor = Color.Yellow,
        )
    )
}

@Composable
fun CustomThumbSwitchSample() {
    var state by rememberSaveable { mutableStateOf(false) }

    Switch(
        checked = state,
        onCheckedChange = { state = it },
        thumbContent = {
            if (state) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            }
        }
    )
}