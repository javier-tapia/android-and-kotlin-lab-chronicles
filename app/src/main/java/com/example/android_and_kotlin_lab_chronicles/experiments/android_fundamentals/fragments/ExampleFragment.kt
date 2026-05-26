package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Fragmento de prueba para documentar el ciclo de vida y paso de datos.
 */
class ExampleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // En este lab usaremos vistas sencillas o ComposeView dentro del fragment
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        /**
         * Patrón recomendado para crear fragmentos con argumentos
         * evitando problemas de recreación por el sistema.
         */
        fun newInstance(id: String) = ExampleFragment().apply {
            arguments = Bundle().apply {
                putString("ARG_ID", id)
            }
        }
    }
}