package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.DatabasesViewModel
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.presentation.components.QuickMetricView
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.presentation.components.UserWithOrderRegister
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.RoomUserEntity

/**
 * ### RoomLabScreen (UI Container)
 *
 * Contenedor Composable principal para interactuar con el laboratorio de persistencia en Room.
 *
 * #### Principios de Arquitectura y UI
 * - ***State Hoisting***: No gestiona directamente conexiones a la base de datos ni lanza corrutinas.
 *   Observa la consulta plana [DatabasesViewModel.usersState] para métricas de cabecera y
 *   [DatabasesViewModel.usersWithOrdersState] para el flujo relacional 1:N mediante
 *   [collectAsStateWithLifecycle], delegando las operaciones CRUD (inserción, actualización,
 *   borrado) al ViewModel.
 * - ***Lifecycle Awareness***: Inyecta el [DatabasesViewModel] acoplado al ciclo de vida del
 *   Composable mediante [hiltViewModel].
 *
 * > **Nota sobre [DatabasesViewModel.usersState] y [DatabasesViewModel.usersWithOrdersState]:**
 * > Mientras que ``usersWithOrdersState`` ejercita consultas relacionales complejas 1:N mediante
 * > transacciones atómicas (``@Transaction``), ``usersState`` expone una consulta plana sobre una
 * > única entidad (``room_users``). Esto permite evidenciar cómo optimizar recursos de DB
 * > seleccionando flujos livianos para vistas simples (ej. listas desplegables o autocompletados)
 * > sin incurrir en el sobrecosto de consultar tablas secundarias.
 *
 * @param viewModel Instancia de [DatabasesViewModel] inyectada por Hilt.
 */
@Composable
fun RoomLabScreen(
    viewModel: DatabasesViewModel = hiltViewModel()
) {
    val users by viewModel.usersState.collectAsStateWithLifecycle()
    val usersWithOrders by viewModel.usersWithOrdersState.collectAsStateWithLifecycle()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Room ORM Engine (SQLite)", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        // --- CARD DE MÉTRICAS (Usa 'usersState' - Consulta Plana) ---
        QuickMetricView(users)

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (firstName.isNotBlank() && lastName.isNotBlank()) {
                    viewModel.insertUser(
                        RoomUserEntity(
                            firstName = firstName,
                            lastName = " $lastName"
                        )
                    )
                    firstName = ""
                    lastName = ""
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Agregar usuario")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        LazyColumn {
            items(usersWithOrders, key = { it.user.uid }) { item ->
                UserWithOrderRegister(item, viewModel)
            }
        }
    }
}
