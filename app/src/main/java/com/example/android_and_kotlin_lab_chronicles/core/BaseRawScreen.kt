package com.example.android_and_kotlin_lab_chronicles.core

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.android_and_kotlin_lab_chronicles.ui.theme.LabTheme

/**
 * Crea una "key" para acceder al Snackbar desde cualquier parte
 */
val LocalSnackbarHostState =
    staticCompositionLocalOf<SnackbarHostState> {
        error("No SnackbarHostState provided")
    }

/**
 * Control total del espacio (ideal para experimentos como animaciones o layouts complejos).
 *
 * Proporciona el SnackbarHostState vía CompositionLocal.
 */
@Composable
fun BaseRawScreen(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    content: @Composable (SnackbarHostState) -> Unit
) {
    LabTheme {
        // Se provee el estado para que cualquier hijo pueda usar LocalSnackbarHostState.current
        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
            content(snackbarHostState)
        }
    }
}
