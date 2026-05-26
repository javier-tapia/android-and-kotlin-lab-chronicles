package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase
import com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.utils.CheckBoxState

/**
 * Se le debe ***indicar un estado*** y la ***acción a realizar*** cuando el estado del componente cambia.
 *
 * El ``CheckBox`` tiene la particularidad de que ***puede tener un tercer estado*** (`TriStateCheckbox`),
 * además de ``on`` y ``off``, llamado ``indeterminate``.
 */
@Composable
fun CheckBoxLab() {
    SamplesShowcase(
        { CheckBoxSample() },
        { TriStateCheckBoxSample() },
        horizontalAlignment = Alignment.Start,
    )
}

@Composable
private fun CheckBoxSample() {
    var state by remember {
        mutableStateOf(
            listOf(
                CheckBoxState(
                    id = "terms",
                    label = "Aceptar términos y condiciones"
                ),
                CheckBoxState(
                    id = "newsletter",
                    label = "Recibir newsletter",
                    checked = true
                ),
                CheckBoxState(
                    id = "updates",
                    label = "Recibir actualizaciones"
                )
            )
        )
    }

    state.forEach { checkBoxState ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                state = state.map {
                    if (it.id == checkBoxState.id) {
                        checkBoxState.copy(checked = !checkBoxState.checked)
                    } else {
                        it
                    }
                }
            }
        ) {
            Checkbox(
                enabled = true,
                checked = checkBoxState.checked,
                onCheckedChange = {
                    state = state.map {
                        if (it.id == checkBoxState.id) {
                            checkBoxState.copy(checked = !checkBoxState.checked)
                        } else {
                            it
                        }
                    }
                },
            )
            Spacer(Modifier.width(12.dp))
            Text(text = checkBoxState.label)
        }
    }
}

@Composable
private fun TriStateCheckBoxSample(modifier: Modifier = Modifier) {
    var child1 by remember { mutableStateOf(false) }
    var child2 by remember { mutableStateOf(false) }

    // El estado del padre se calcula en tiempo real durante la composición
    val parentState by remember {
        derivedStateOf {
            when {
                child1 && child2 -> ToggleableState.On
                !child1 && !child2 -> ToggleableState.Off
                else -> ToggleableState.Indeterminate
            }
        }
    }

    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TriStateCheckbox(state = parentState, onClick = {
                val newState = parentState != ToggleableState.On
                child1 = newState
                child2 = newState
            })
            Text("Seleccionar todo")
        }
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = child1, onCheckedChange = { child1 = it })
            Text("Ejemplo 1")
        }
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = child2, onCheckedChange = { child2 = it })
            Text("Ejemplo 2")
        }
    }
}
