package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase

/**
 * Experimentos con variantes de Card (Elevated, Filled, Outlined).
 * Una ``Card`` es simplemente una ``Surface`` con valores por defecto (``elevation``, ``shape``, etc).
 */
@Composable
fun CardLab() {
    SamplesShowcase(
        { CardSample() }
    )
}

@Composable
private fun CardSample() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp),
                spotColor = Color.Red
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Blue
        ),
        border = BorderStroke(5.dp, Color.Green)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Ejemplo 1")
            Text(text = "Ejemplo 2")
            Text(text = "Ejemplo 3")
        }
    }
}
