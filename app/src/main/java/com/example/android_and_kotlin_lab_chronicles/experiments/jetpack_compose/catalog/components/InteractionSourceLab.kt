package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase

/**
 * Experimentos sobre la captura de estados de interacción (Press, Focus, Drag y Hover).
 *
 * Aprendizaje clave: Android diferencia entre el "Modo Táctil" y el "Modo Teclado/Puntero".
 * Muchos estados visuales de foco solo son visibles cuando el sistema entra en modo navegación física.
 */
@Composable
fun InteractionSourceLab() {
    SamplesShowcase(
        { PressInteractionSample() },
        { FocusInteractionSample() },
        { DragInteractionSample() },
        { HoverInteractionSample() }
    )
}

@Composable
private fun PressInteractionSample() {
    val interaction = remember { MutableInteractionSource() }
    val isPressed by interaction.collectIsPressedAsState()

    Box(
        modifier = Modifier
            .size(150.dp)
            .background(if (isPressed) Color.Red else Color.White)
            // clickable incluye internamente focusable(), pero en modo táctil el foco es invisible.
            .clickable(
                indication = LocalIndication.current,
                interactionSource = interaction
            ) { },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isPressed) "Pulsado" else "Sin pulsar",
            color = Color.Black
        )
    }
}

@Composable
private fun FocusInteractionSample() {
    val interaction = remember { MutableInteractionSource() }
    val isFocused by interaction.collectIsFocusedAsState()

    // LAB TIP: Para probar el foco en el emulador:
    // 1. El primer 'Tab' suele activar el "Keyboard Mode" del sistema.
    // 2. Los siguientes 'Tab' recorren los elementos que tengan el modificador .focusable().
    // 3. .focusable() es suficiente; incluye internamente la lógica de focusTarget().
    Box(
        modifier = Modifier
            .size(150.dp)
            .border(
                width = 4.dp,
                color = if (isFocused) Color.Blue else Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .focusable(interactionSource = interaction),
        contentAlignment = Alignment.Center
    ) {
        Text(if (isFocused) "En Foco" else "Hacé click para enfocar")
    }
}

@Composable
private fun DragInteractionSample() {
    val interaction = remember { MutableInteractionSource() }
    val isDragged by interaction.collectIsDraggedAsState()

    Box(
        modifier = Modifier
            .size(150.dp)
            .background(if (isDragged) Color.LightGray else Color.DarkGray)
            // Draggable detecta el gesto de arrastre
            .draggable(
                state = androidx.compose.foundation.gestures.rememberDraggableState { },
                orientation = androidx.compose.foundation.gestures.Orientation.Horizontal,
                interactionSource = interaction
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isDragged) "Arrastrando" else "Arrastra Horizontal",
            color = if (isDragged) Color.Black else Color.White
        )
    }
}

@Composable
private fun HoverInteractionSample() {
    val interaction = remember { MutableInteractionSource() }
    val isHovered by interaction.collectIsHoveredAsState()

    // LAB TIP: El Hover depende de que el SO detecte un puntero (Mouse/Stylus).
    // En emuladores de teléfono, el mouse se suele emular como un 'touch' (dedo),
    // por lo que el estado Hover no se dispara. Es funcional en Tablets/Desktops.
    Box(
        modifier = Modifier
            .size(150.dp)
            .background(if (isHovered) Color.Gray else Color.LightGray)
            .hoverable(interactionSource = interaction),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isHovered) "Mouse detectado" else "Hover (Requiere Mouse)",
            color = Color.Black
        )
    }
}
