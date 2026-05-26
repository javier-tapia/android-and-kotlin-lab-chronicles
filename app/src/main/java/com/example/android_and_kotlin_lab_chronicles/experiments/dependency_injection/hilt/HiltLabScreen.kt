package com.example.android_and_kotlin_lab_chronicles.experiments.dependency_injection.hilt

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

/**
 * Al instanciar el ViewModel con ``hiltViewModel()``, el Composable lo resuelve automáticamente
 * desde el ``@AndroidEntryPoint`` padre (en este caso, de [HiltLabActivity]).
 *
 * ##### ¿Por qué se justifica usar ``hiltViewModel()`` aunque la Activity instancie el ViewModel?
 *
 * Pasarle el ViewModel por parámetro desde la Activity (***Property Drilling***) rompe el
 * desacoplamiento de la UI. Mantener ``hiltViewModel()`` da tres ventajas estructurales:
 * - **Pureza de la Firma del Composable**: Si hubiera un Composable de más alto nivel, seguiría
 * siendo completamente autónomo. Si en el futuro se migra el proyecto a una arquitectura de
 * *Single Activity* (donde la activity principal solo inicialice la app y se navegue mediante
 * Jetpack Navigation), el Composable no va a requerir que se modifiquen sus parámetros; seguirá
 * resolviendo su dependencia de forma interna y transparente.
 * - **Aislamiento de Responsabilidades**: La Activity necesita el ViewModel para una tarea
 * puramente del framework de Android (por ejemplo, escuchar un log, lanzar un Intent, o reaccionar
 * a un ciclo de vida nativo). El Composable lo necesita para renderizar la UI. Que compartan el
 * nodo **no significa que deban acoplar sus firmas de código**.
 * - **Mantenimiento de Previews y Tests**: Al usar el parámetro por defecto
 * (``viewModel: HiltLabViewModel = hiltViewModel()``), el Composable se puede previsualizar en el IDE
 * de forma huérfana pasando un mock, sin importar la complejidad o las dependencias internas
 * de la activity principal.
 */
@Composable
fun HiltLabScreen(
    viewModel: HiltLabViewModel = hiltViewModel(),
) {
    viewModel.logHiltFromHiltLabScreen()

    Text("Texto de ejemplo")
}
