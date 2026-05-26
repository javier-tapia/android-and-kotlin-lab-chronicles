package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.core.LocalSnackbarHostState
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase
import com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.utils.CustomSnackbarStyle
import com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.utils.CustomSnackbarVisuals
import com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.utils.SnackbarRecipe
import kotlinx.coroutines.launch

/**
 * Experimentos con Snackbars para mensajes informativos temporales.
 */
@Composable
fun SnackbarLab() {
    val context = LocalContext.current

    // Obtener el scope de corrutinas vinculado al ciclo de vida de este Composable
    val scope = rememberCoroutineScope()

    // Consumir el estado del Snackbar inyectado por la base (vía CompositionLocal)
    val snackbarHostState = LocalSnackbarHostState.current

    // Usar el scope para lanzar la función de suspensión showSnackbar
    val onNotify = { visuals: CustomSnackbarVisuals ->
        scope.launch {
            val result = snackbarHostState.showSnackbar(visuals)
            if (result == SnackbarResult.ActionPerformed) {
                // Esto se ejecuta cuando se llama a data.performAction()
                Toast.makeText(context, "Reintentando...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // El SnackbarHost se define en su propio slot.
        // Si no se colocara aquí y sí dentro del content (que tiene acceso al `innerPadding`), sería
        // necesario agregar un Modifier para calcular el bottom padding, por ejemplo:
        // `Modifier.padding(bottom = paddingValues.calculateBottomPadding() + 16.dp)`
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            ) { data -> // <--- Datos del snackbar actual
                BaseSnackbarSample(data)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SamplesShowcase(
                {
                    Button(onClick = {
                        onNotify(
                            CustomSnackbarVisuals(style = CustomSnackbarStyle.SUCCESS)
                        )
                    }) { Text("Probar Success") }
                },
                {
                    Button(onClick = {
                        onNotify(
                            CustomSnackbarVisuals(style = CustomSnackbarStyle.ERROR)
                        )
                    }) { Text("Probar Error") }
                },
                {
                    Button(onClick = {
                        onNotify(
                            CustomSnackbarVisuals(style = CustomSnackbarStyle.INFO)
                        )
                    }) { Text("Información") }
                }
            )
        }
    }
}

@Composable
private fun BaseSnackbarSample(data: SnackbarData) {
    val visuals = data.visuals as? CustomSnackbarVisuals

    // El style define la "receta" estética
    val recipe = when (visuals?.style) {
        CustomSnackbarStyle.SUCCESS -> SnackbarRecipe(
            color = Color(0xFF4CAF50),
            title = "¡Éxito!",
            message = "Operación OK",
            icon = Icons.Default.Check,
        )
        CustomSnackbarStyle.ERROR -> SnackbarRecipe(
            color = Color(0xFFF44336),
            title = "¡Error!",
            message = "Algo salió mal",
            icon = Icons.Default.Error,
            actionLabel = "Reintentar"
        )
        else -> SnackbarRecipe(
            color = Color(0xFF2196F3),
            title = "Info",
            message = "Aviso del sistema",
            icon = Icons.Default.Info
        )
    }

    // Para el texto final, se prioriza lo que viene de SnackbarLab si no está vacío.
    // Sino, se toma el mensaje de la receta
    val finalMessage = visuals?.message?.takeIf { it.isNotBlank() } ?: recipe.message

    Snackbar(
        modifier = Modifier.padding(16.dp),
        containerColor = recipe.color,
        shape = RoundedCornerShape(25),
        // Se puede usar el slot oficial de acción del Snackbar
        action = {
            recipe.actionLabel?.let { label ->
                Button(
                    onClick = { data.performAction() }
                ) {
                    Text(label)
                }
            }
        },
        // Y el del botón de cerrar si withDismissAction es true
        dismissAction = {
            if (visuals?.withDismissAction == true) {
                IconButton(
                    onClick = { data.dismiss() }
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar")
                }
            }
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(recipe.icon, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
            ) {
                Text(recipe.title, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text(finalMessage, color = Color.White)
            }
        }
    }
}
