package com.example.android_and_kotlin_lab_chronicles.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components.DividerLab

/**
 * Contenedor utilitario diseñado para exhibir múltiples experimentos o variantes de componentes
 * de forma organizada y estandarizada.
 *
 * El **patrón *Slot* API** permite mantener la consistencia visual al mostrar distintos componentes similares,
 * sin necesidad de gestionar manualmente los márgenes o divisores en cada laboratorio.
 *
 * @param [samples] Lista de composables que se renderizarán secuencialmente
 * con espaciado y divisores automáticos.
 */
@Composable
fun SamplesShowcase(
    vararg samples: @Composable () -> Unit,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(80.dp), // Espaciado automático entre ítems
        horizontalAlignment = horizontalAlignment,
        contentPadding = PaddingValues(vertical = 40.dp) // Margen inicial y final
    ) {
        itemsIndexed(samples) { index, sample ->
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = horizontalAlignment
            ) {
                sample()
            }
            
            // Si no es el último ítem, se agrega el divisor
            if (index < samples.lastIndex) {
                Spacer(modifier = Modifier.height(40.dp))
                DividerLab()
            }
        }
    }
}
