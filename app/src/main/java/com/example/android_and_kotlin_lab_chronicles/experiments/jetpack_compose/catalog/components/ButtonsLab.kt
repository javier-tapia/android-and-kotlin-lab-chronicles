package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.core.LocalSnackbarHostState
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * ``Button`` es el ``Composable`` que renderiza un botón. Tiene la particularidad de que debe
 * implementar el ``onClick`` obligatoriamente.
 *
 * Este componente tiene otras variantes, como el ``OutlineButton`` o el ``TextButton``.
 */
@Composable
fun ButtonsLab() {
    val scope = rememberCoroutineScope()
    val snackbar = LocalSnackbarHostState.current

    SamplesShowcase(
        { ButtonSample() },
        { OutlinedButtonSample() },
        { TextButtonSample(scope, snackbar) }
    )
}

@Composable
private fun ButtonSample() {
    var enabled by rememberSaveable { mutableStateOf(true) }

    Button(
        onClick = { enabled = false },
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Magenta,
            contentColor = Color.Blue
        ),
        border = BorderStroke(5.dp, Color.Green)
    ) {
        Text(text = "Hola")
    }
}

@Composable
private fun OutlinedButtonSample() {
    var enabled by rememberSaveable { mutableStateOf(true) }

    OutlinedButton(
        onClick = { enabled = false },
        enabled = enabled,
        modifier = Modifier.padding(top = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Magenta,
            contentColor = Color.Blue,
            disabledContainerColor = Color.Blue,
            disabledContentColor = Color.Red
        )
    ) {
        Text(text = "Hola")
    }
}

@Composable
private fun TextButtonSample(
    scope: CoroutineScope,
    snackbar: SnackbarHostState
) {
    TextButton(onClick = {
        scope.launch { snackbar.showSnackbar("¡Funciona!") }
    }) {
        Text(text = "Hola")
    }
}
