package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Definición de rutas y destinos para la navegación en Compose con Kotlin Serialization,
 * que permite una navegación *type-safe*.
 * 
 * En Kotlin moderno, para rutas de navegación, la recomendación es usar ``data object``.
 * - **Ventaja principal**: El ``data object`` genera automáticamente una implementación limpia
 * de ``toString()``, ``equals()`` y ``hashCode()``. Esto es vital para la navegación, ya que
 * Compose necesita comparar destinos para saber si debe recomponer o animar.
 * Un ``object`` normal imprime algo como ``Routes$ScreenA@hash``, mientras que un ``data object``
 * imprime ``ScreenA``.
 * - **Serialización**: Con Kotlin Serialization, ``data object`` se comporta de forma más predecible
 * al ser tratado como un objeto con estado único.
 * - **Desventaja**: Ninguna relevante en este contexto. Es simplemente la evolución del ``object``
 * para casos donde el valor semántico importa.
 */
sealed class Routes : NavKey {
    @Serializable
    // Pantalla sin parámetros
    data object ScreenA : Routes()

    // Pantalla con parámetros
    @Serializable
    data class ScreenB(
        val name: String,
        val age: Int
    ) : Routes()

    // Pantalla de error
    @Serializable
    data object Error : Routes()
}
