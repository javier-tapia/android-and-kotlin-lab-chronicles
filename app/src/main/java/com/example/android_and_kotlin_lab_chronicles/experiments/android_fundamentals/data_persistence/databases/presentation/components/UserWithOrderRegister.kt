package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.presentation.components

import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.DatabasesViewModel
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.RoomOrderEntity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.model.UserWithOrders

@Composable
fun UserWithOrderRegister(
    item: UserWithOrders,
    viewModel: DatabasesViewModel
) {
    // Estado de scroll independiente para cada registro del elemento.
//    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            // IMPORTANTE: 'horizontalScroll' otorga ancho infinito a los hijos y desactiva
            // el 'basicMarquee' del Text, ya que este requiere un contenedor de ancho acotado
            // (p. ej. mediante 'Modifier.weight') para detectar el desbordamiento (overflow)
            // necesario para activar la animación.
//          .horizontalScroll(scrollState)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                // 1. Restringe el ancho disponible
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text("ID ${item.user.uid}: ${item.user.firstName} ${item.user.lastName}")

            // 2. El Text con 'basicMarquee' ahora detectará que desborda el ancho asignado
            Text(
                modifier = Modifier.basicMarquee(
                    iterations = 1,
                    animationMode = MarqueeAnimationMode.Immediately,
                    initialDelayMillis = 1200,
                    velocity = 30.dp
                ),
                text = "Órdenes: ${
                    item.orders.joinToString { "$${it.amount}" }.ifEmpty { "Sin órdenes" }
                }",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Row {
            // Invocación a @Update vía ViewModel
            IconButton(onClick = {
                viewModel.updateUser(item.user.copy(firstName = "${item.user.firstName}*"))
            }) {
                Text("✏️")
            }

            // Invocación a insertOrder vía ViewModel
            IconButton(onClick = {
                viewModel.insertOrder(
                    RoomOrderEntity(userId = item.user.uid, amount = 100.0)
                )
            }) {
                Text("➕")
            }

            // Invocación a updateOrder vía ViewModel
            IconButton(
                onClick = {
                    // Modifica la última orden registrada de este usuario
                    item.orders.lastOrNull()?.let { lastOrder ->
                        viewModel.updateOrder(
                            // Mantiene el orderId intacto
                            lastOrder.copy(amount = 150.0)
                        )
                    }
                },
                // Opcional: deshabilita el botón si no hay órdenes
                enabled = item.orders.isNotEmpty()
            ) {
                Text("\uD83D\uDD04") // 🔄
            }

            // Invocación a @Delete vía ViewModel
            IconButton(onClick = { viewModel.deleteUser(item.user) }) {
                Text("❌")
            }
        }
    }
}
