package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScreenBContent(name: String, age: Int, onBack: () -> Unit) {
    Column(Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Text("Pantalla B", style = MaterialTheme.typography.headlineMedium)
        Text("Nombre: $name")
        Text("Edad: $age")
        OutlinedButton(onClick = onBack) {
            Text("Volver")
        }
    }
}
