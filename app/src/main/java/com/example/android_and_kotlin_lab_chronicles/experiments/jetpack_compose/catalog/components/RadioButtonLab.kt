package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase

/**
 * Experimentos con RadioButtons para selección única dentro de un grupo.
 *
 * Se le debe ***indicar un estado*** y la ***acción a realizar*** cuando el estado del componente cambia.
 */
@Composable
fun RadioButtonLab() {
    SamplesShowcase(
        { BasicRadioButtonSample() },
        { GroupRadioButtonSample() },
        horizontalAlignment = Alignment.Start,
    )
}

@Composable
fun BasicRadioButtonSample() {
    var selected by remember { mutableStateOf(false) }
    var selected2 by remember { mutableStateOf(false) }
    var selected3 by remember { mutableStateOf(false) }
    val interaction = remember { MutableInteractionSource() }
    val isPressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.5f else 1f,
        label = "ScaleAnimation"
    )

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected,
            onClick = { selected = true },
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.Red,
                unselectedColor = Color.Yellow
            )
        )

        Text(text = "Cambia de color...")
    }

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected2,
            onClick = { },
            // Para deshabilitarlo, hay que setear el enabled en false
            enabled = false,
            colors = RadioButtonDefaults.colors(disabledSelectedColor = Color.Green)
        )

        Text(text = "Deshabilitado...")
    }

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            modifier = Modifier.scale(scale), // <--- Aquí se usa el valor animado
            selected = selected3,
            onClick = { selected3 = true },
            interactionSource = interaction
        )

        Text(text = "Aumenta el tamaño al mantener apretado...")
    }
}

@Composable
fun GroupRadioButtonSample(modifier: Modifier = Modifier) {
    val programmingLanguageList = listOf("Kotlin", "Java", "Go", "JS")
    var selectedLanguage by remember { mutableStateOf("Kotlin") }

    // 1. Estado para disparar la animación
    var wrongChoice by remember { mutableStateOf(false) }

    // 2. Animación que reacciona a ese estado
    val scale by animateFloatAsState(
        targetValue = if (wrongChoice) 1.4f else 1f,
        // Spring hace que "rebote", ideal para llamar la atención
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy),
        finishedListener = { wrongChoice = false } // Reset para poder repetir
    )

    Text("Elegí tu lenguaje favorito")

    Column(modifier = modifier.padding(6.dp)) {
        programmingLanguageList.forEach { language ->
            // Solo se aplica el modificador de escala si es Kotlin
            val radioButtonModifier =
                if (language == "Kotlin") modifier.scale(scale) else modifier

            Row(
                modifier = modifier.padding(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    modifier = radioButtonModifier,
                    selected = selectedLanguage == language,
                    onClick = {
                        selectedLanguage = language
                        // Siempre que se necesite tomar una decisión lógica dentro de un evento
                        // (onClick, onValueChanged), usar el valor entrante o la variable local
                        // (language en este caso).
                        // No confiar en que la variable de estado (selectedLanguage) se haya
                        // actualizado en esa misma línea de ejecución; el estado es para que la UI
                        // reaccione, no para lógica secuencial inmediata
                        if (language != "Kotlin") wrongChoice = true
                    }
                )
                Text(text = language, color = Color.White)
            }
        }
    }
}
