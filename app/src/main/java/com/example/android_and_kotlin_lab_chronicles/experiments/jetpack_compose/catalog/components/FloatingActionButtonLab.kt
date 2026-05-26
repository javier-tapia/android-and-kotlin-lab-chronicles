package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase

/**
 * Experimentos con las variantes de Floating Action Buttons (Small, Large, Extended).
 *
 * En MD3, el FAB es un elemento independiente. A diferencia de MD2, la NavigationBar no admite
 * el "docking" (recorte) para incrustar el FAB: este debe flotar sobre la UI.
 */
@Composable
fun FloatingActionButtonLab() {
    SamplesShowcase(
        { FloatingActionButtonSample() },
        { SmallFloatingActionButtonSample() },
        { LargeFloatingActionButtonSample() },
        { ExtendedFloatingActionButtonSample() },
    )
}

@Composable
fun FloatingActionButtonSample() {
    FloatingActionButton(
        onClick = { },
        containerColor = Color.DarkGray,
        contentColor = Color.White
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = null)
    }
}

@Composable
fun SmallFloatingActionButtonSample() {
    SmallFloatingActionButton(
        onClick = { },
        containerColor = Color.DarkGray,
        contentColor = Color.White
    ) {
        Icon(Icons.Filled.Add, contentDescription = null)
    }
}

@Composable
fun LargeFloatingActionButtonSample() {
    LargeFloatingActionButton(
        onClick = { },
        containerColor = Color.DarkGray,
        contentColor = Color.White
    ) {
        // El icono en el Large FAB suele verse mejor si se escala o es más prominente
        Icon(Icons.Filled.Add, contentDescription = null)
    }
}

@Composable
fun ExtendedFloatingActionButtonSample() {
    ExtendedFloatingActionButton(
        onClick = { },
        icon = {
            Icon(Icons.Filled.Add, contentDescription = null)
        },
        text = {
            Text(text = "Añadir Ítem")
        },
        containerColor = Color.DarkGray,
        contentColor = Color.White
    )
}
