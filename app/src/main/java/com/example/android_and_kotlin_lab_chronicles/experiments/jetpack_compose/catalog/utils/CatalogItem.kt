package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.utils

import android.os.Parcelable
import androidx.compose.runtime.Composable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class CatalogItem(
    val name: String,
    val requiresRawScreen: Boolean = false,
    // @RawValue le indica a Parcelize que no serialice la lambda (no se puede convertir a bytes).
    // Guarda solo una referencia de memoria, por lo que sobrevive a la rotación de la Activity,
    // pero fallará si Android mata el proceso de la app para liberar RAM.
    val content: @RawValue @Composable () -> Unit
): Parcelable
