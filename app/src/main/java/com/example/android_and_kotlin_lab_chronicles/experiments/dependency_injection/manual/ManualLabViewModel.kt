package com.example.android_and_kotlin_lab_chronicles.experiments.dependency_injection.manual

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android_and_kotlin_lab_chronicles.manual.ManualLabConfigurator

/**
 * ViewModel que expone el estado del laboratorio de DI Manual.
 *
 * Al no contar con librerías que generen código en compilación (``@HiltViewModel``) ni DSLs
 * de resolución en *runtime* (``viewModelOf``), este ViewModel es agnóstico al framework de DI.
 * Delega su instanciación a una [Factory] interna que recibe sus dependencias de forma explícita.
 */
class ManualLabViewModel(
    private val configurator: ManualLabConfigurator
) : ViewModel() {

    fun getLabStatus(): String = configurator.getLabStatus()

    /**
     * Factory manual para indicarle al framework de Android cómo instanciar [ManualLabViewModel]
     * inyectándole sus dependencias de constructor.
     */
    class Factory(
        private val configurator: ManualLabConfigurator
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ManualLabViewModel::class.java)) {
                return ManualLabViewModel(configurator) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
