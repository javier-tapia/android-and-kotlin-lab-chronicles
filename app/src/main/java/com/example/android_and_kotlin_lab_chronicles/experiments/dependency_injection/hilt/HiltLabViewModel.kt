package com.example.android_and_kotlin_lab_chronicles.experiments.dependency_injection.hilt

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.android_and_kotlin_lab_chronicles.hilt.LabConfigurator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para validar la infraestructura de Hilt en la raíz del proyecto (es decir, validar
 * que el Grafo de Hilt compila de extremo a extremo).
 *
 * - ``@HiltViewModel`` le indica al procesador de Hilt que genere el Factory intermedio necesario
 * para integrarse con el ciclo de vida de los ViewModels de Android Architecture Components.
 * - El constructor con ``@Inject`` marca este punto para la resolución automática de dependencias.
 * Si el constructor está vacío, Hilt lo resuelve de forma automática sin módulos.
 * Sin embargo, tranquilamente se podrían inyectar abstracciones de red, sensores o lo que fuera
 * mediante módulos dedicados de Hilt ([dagger.Module]).
 *
 * ###### CONCEPTO DE GRAFO EN DI
 * El "Grafo" es la estructura de datos dirigida (DAG) que Hilt construye en tiempo de compilación.
 * Contiene los nodos (clases instanciables) y las aristas (relaciones de dependencia en los constructores).
 * Al estar este constructor vacío, el nodo se registra de forma huérfana pero válida, permitiendo que
 * la [HiltLabActivity] lo consuma resolviendo el grafo de extremo a extremo sin dependencias transitivas.
 */
@HiltViewModel
class HiltLabViewModel @Inject constructor(
    private val labConfigurator: LabConfigurator
) : ViewModel() {
    private val tag = "HiltLabChronicles"

    fun logHiltFromActivity() {
        Log.d(
            tag,
            "[Activity] Interceptada con éxito. Mensaje del Grafo: ${labConfigurator.getEnvironmentName()}"
        )
    }

    fun logHiltFromHiltLabScreen() {
        Log.d(
            tag,
            "[Screen] Composable resuelto mediante hiltViewModel(). Mensaje: ${labConfigurator.getEnvironmentName()}"
        )
    }
}
