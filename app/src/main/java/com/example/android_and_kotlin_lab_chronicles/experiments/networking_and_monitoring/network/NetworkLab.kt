package com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.ui.NetworkUiState

/**
 * Experimentos: Consumo de APIs REST con Retrofit y Ktor Client.
 * 
 * Temas:
 * - Retrofit: Configuración con OkHttp, Converters (Gson/Serialization) e Interceptors.
 * - Ktor Client: Configuración de Engine (CIO/Android), Plugins (ContentNegotiation, Logging).
 * - Comparativa: Gestión de timeouts, manejo de errores y tipado de respuestas.
 * - Autenticación: Inserción de Tokens dinámicos en las cabeceras.
 */
@Composable
fun NetworkLab(
    viewModel: NetworkViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.fetchUserWithRetrofit("1") },
                enabled = uiState !is NetworkUiState.Loading
            ) {
                Text("Probar Retrofit")
            }

            Button(
                onClick = { viewModel.fetchUserWithKtor("1") },
                enabled = uiState !is NetworkUiState.Loading
            ) {
                Text("Probar Ktor")
            }
        }

        when (val state = uiState) {
            is NetworkUiState.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkUiState.Success -> {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = "${state.clientName} respondió en ${state.latencyMs} ms",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("ID: ${state.user.id}", style = MaterialTheme.typography.labelMedium)
                        Text("Nombre: ${state.user.name}", style = MaterialTheme.typography.titleMedium)
                        Text("Email: ${state.user.email}")
                    }
                }
            }
            is NetworkUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            NetworkUiState.Idle -> {
                Text(
                    text = "Seleccioná un cliente HTTP para ejecutar la petición de prueba.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
