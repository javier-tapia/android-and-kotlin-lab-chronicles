package com.example.android_and_kotlin_lab_chronicles.experiments.dependency_injection.koin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

/**
 * Al instanciar el ViewModel con [koinViewModel], el Composable lo resuelve dinámicamente
 * desde el contenedor en tiempo de ejecución (*Service Locator*), respetando el ámbito (*Scope*)
 * actual del arbol de Compose o del ``NavBackStackEntry``.
 *
 * ##### ¿Por qué se justifica usar [koinViewModel] con valor por defecto en el Composable?
 *
 * Evita el ***Property Drilling*** desde la Activity/NavHost y otorga ventajas estructurales:
 * - **Autonomía y *Single Activity*:** En una arquitectura basada en Jetpack Navigation, el Composable
 *   resuelve sus propias dependencias sin depender de que una Activity o un contenedor padre se las
 *   transmita explícitamente.
 * - **Desacoplamiento sin Generación de Código:** A diferencia de Hilt (que requiere `@AndroidEntryPoint`
 *   en la Activity para propagar la inyección), Koin resuelve [KoinLabViewModel] directamente desde el
 *   ``CompositionLocalProvider`` de Compose en tiempo de ejecución, permitiendo que la Activity sea un
 *   contenedor completamente agnóstico a la DI.
 * - **Facilidad para Previews y Tests:** Mantener el parámetro por defecto
 * (`viewModel: KoinLabViewModel = koinViewModel()`) permite que en `@Preview` o tests unitarios
 * de UI se pueda inyectar un ViewModel *fake*, sin necesidad de inicializar el motor de
 * Koin (`startKoin`) en el entorno de pruebas.
 */
@Composable
fun KoinLabScreen(
    modifier: Modifier = Modifier,
    viewModel: KoinLabViewModel = koinViewModel(),
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = viewModel.getLabStatus())
        }
    }
}
