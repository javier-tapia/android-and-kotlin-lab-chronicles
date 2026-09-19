package com.example.android_and_kotlin_lab_chronicles.experiments.dependency_injection.koin

import androidx.lifecycle.ViewModel
import com.example.android_and_kotlin_lab_chronicles.koin.KoinLabConfigurator
import com.example.android_and_kotlin_lab_chronicles.koin.KoinLabMessageFormatter

/**
 * ViewModel que demuestra la inyección de dependencias administrada por Koin.
 *
 * No requiere la anotación `@HiltViewModel`: Koin instanciará este ViewModel
 * resolviendo la dependencia [KoinLabConfigurator] directamente desde su contenedor de *runtime*.
 */
class KoinLabViewModel(
    private val configurator: KoinLabConfigurator,
    private val formatter: KoinLabMessageFormatter
) : ViewModel() {

    fun getLabStatus(): String {
        val originalText = "Resuelto por Koin ➔ ${configurator.getEnvironmentName()}"
        return formatter.format(originalText)
    }
}
