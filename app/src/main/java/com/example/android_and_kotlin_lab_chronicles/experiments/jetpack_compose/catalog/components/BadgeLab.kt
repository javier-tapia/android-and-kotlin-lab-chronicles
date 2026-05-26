package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase

/**
 * Se utiliza para mostrar información dinámica, como puede ser el número de mensajes sin leer
 * de una *app* de *mail* o mensajería.
 */
@Composable
fun BadgeLab() {
    SamplesShowcase(
        { BadgeBoxSample() }
    )
}

@Composable
private fun BadgeBoxSample() {
    BadgedBox(
        modifier = Modifier.padding(16.dp),
        badge = {
            Badge {
                val badgeNumber = "8"
                Text(
                    badgeNumber,
                    modifier = Modifier.semantics {
                        contentDescription = "$badgeNumber new notifications"
                    }
                )
            }
        }
    ) {
        Icon(imageVector = Icons.Default.Star, contentDescription = "")
    }
}
