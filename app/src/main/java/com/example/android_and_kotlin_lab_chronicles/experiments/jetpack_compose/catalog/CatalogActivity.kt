package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen
import com.example.android_and_kotlin_lab_chronicles.core.BaseRawScreen
import com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.utils.CatalogItem

class CatalogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val catalogItems = CatalogProvider.getCatalogItems()
            var currentItem by rememberSaveable { mutableStateOf<CatalogItem?>(null) }

            // Manejo del botón atrás físico: si estamos en una sección, vuelve al menú
            BackHandler(enabled = currentItem != null) {
                currentItem = null
            }

            // Si el ítem requiere una pantalla "Raw" (sin Scaffold base), se la proveemos.
            // De lo contrario, se usa la estructura estándar con título y padding.
            if (currentItem?.requiresRawScreen == true) {
                BaseRawScreen {
                    currentItem?.content?.invoke()
                }
            } else {
                BaseLayoutScreen(title = currentItem?.name ?: "Compose Catalog") {
                    if (currentItem == null) {
                        CatalogMenu(catalogItems) { currentItem = it }
                    } else {
                        currentItem?.content?.invoke()
                    }
                }
            }
        }
    }
}

@Composable
fun CatalogMenu(items: List<CatalogItem>, onItemClick: (CatalogItem) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(items) { item ->
            Card(
                onClick = { onItemClick(item) },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
