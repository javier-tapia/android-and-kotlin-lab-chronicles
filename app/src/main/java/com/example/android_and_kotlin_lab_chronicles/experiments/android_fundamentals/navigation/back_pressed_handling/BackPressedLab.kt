package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.navigation.back_pressed_handling

import androidx.activity.OnBackPressedCallback

/**
 * Experimento: Gestión del retroceso (Back Navigation) en Android moderno.
 * 
 * Temas:
 * - OnBackPressedDispatcher y OnBackPressedCallback.
 * - Habilitación/Deshabilitación dinámica del callback (isEnabled).
 * - Prioridad de callbacks en la pila del Dispatcher.
 */
object BackPressedLab {
    
    /**
     * Crea un callback básico para interceptar el botón de atrás.
     */
    fun createBackCallback(onBackPressed: () -> Unit): OnBackPressedCallback {
        return object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
    }
}
