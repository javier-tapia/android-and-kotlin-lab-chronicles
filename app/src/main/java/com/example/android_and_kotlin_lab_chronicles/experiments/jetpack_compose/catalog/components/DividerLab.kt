package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Experimentos con Dividers para separación visual de contenidos.
 */
@Composable
fun DividerLab() {
    HorizontalDivider(
        Modifier
            .fillMaxWidth()
            .padding(top = 16.dp), color = Color.Red
    )
}
