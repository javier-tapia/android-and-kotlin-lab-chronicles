package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.RoomUserEntity
import kotlin.collections.firstOrNull

@Composable
fun QuickMetricView(users: List<RoomUserEntity>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Total Usuarios Registrados: ${users.size}",
                style = MaterialTheme.typography.labelLarge
            )

            users.firstOrNull()?.let { lastUser ->
                Text(
                    text = "Último: ${lastUser.firstName}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
