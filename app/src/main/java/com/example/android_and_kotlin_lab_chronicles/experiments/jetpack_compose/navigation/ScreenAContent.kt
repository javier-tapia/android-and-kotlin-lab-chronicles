package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScreenAContent(
    onNavigateToScreenB: () -> Unit,
    onNavigateToErrorScreen: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Esta es la Pantalla A", style = MaterialTheme.typography.headlineMedium)
        Button(onClick = onNavigateToScreenB) {
            Text("Ir a Pantalla B con parámetros")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onNavigateToErrorScreen) {
            Text("Ir a Pantalla de Error")
        }
    }
}
