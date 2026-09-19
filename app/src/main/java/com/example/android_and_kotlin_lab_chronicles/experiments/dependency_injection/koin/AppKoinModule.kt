package com.example.android_and_kotlin_lab_chronicles.experiments.dependency_injection.koin

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * ① Opción Explícita (*Manual DSL*)
 *
 * Define la fábrica del ViewModel invocando su constructor de forma manual.
 * La llamada a [get] instruye a Koin a resolver e inyectar la dependencia
 * [com.example.android_and_kotlin_lab_chronicles.koin.KoinLabConfigurator]
 * previamente registrada en el módulo de infraestructura (``:di-koin``).
 *
 * Requiere especificar el tipo genérico ``<KoinLabViewModel>`` para evitar errores de inferencia
 * en el compilador de Kotlin.
 *
 * ② Opción Sintáctica (*Constructor Binding DSL*)
 *
 * Utiliza la función de extensión [viewModelOf] (introducida en Koin 3.2+) pasando la referencia
 * al constructor (``::KoinLabViewModel``).
 *
 * Koin resuelve e inyecta automáticamente los parámetros del constructor (como ``KoinLabConfigurator``)
 * de forma limpia. No utiliza reflexión (es muy costosa), sino funciones 'inline' con tipos 'reified'
 * nativas de Kotlin, manteniendo un rendimiento óptimo en tiempo de ejecución.
 *
 * @see com.example.android_and_kotlin_lab_chronicles.koin.labKoinModule
 */
val appKoinModule = module {
    // ① Opción Explícita (*Manual DSL*)
//    viewModel<KoinLabViewModel> {
//        KoinLabViewModel(configurator = get())
//    }

    // ② Opción Sintáctica (*Constructor Binding DSL*)
    viewModelOf(::KoinLabViewModel)
}
