package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.utils

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Datos que usará el BaseSnackbarSample para pintar el Snackbar según el estilo
 */
data class SnackbarRecipe(
    val color: Color,
    val title: String,
    val message: String,
    val actionLabel: String? = null,
    val icon: ImageVector
)

/**
 * Define los tipos de custom snackbars
 */
enum class CustomSnackbarStyle { SUCCESS, ERROR, INFO }

/**
 * Implementación custom de los Snackbar Visuals para sumar estilos y un título
 */
class CustomSnackbarVisuals(
    val title: String = "",
    override val message: String = "",
    val style: CustomSnackbarStyle = CustomSnackbarStyle.INFO,
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val withDismissAction: Boolean = true
) : SnackbarVisuals
