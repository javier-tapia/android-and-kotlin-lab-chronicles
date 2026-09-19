package com.example.android_and_kotlin_lab_chronicles.koin

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Helper de formateo efímero para ejemplificar el comportamiento de ``factory``.
 */
class KoinLabMessageFormatter {
    fun format(status: String): String = "🧪 Koin Lab Status: $status"
}

/**
 * Módulo de Koin que registra las definiciones de dependencias del laboratorio.
 *
 * Registra dos componentes clave diferenciados por su ciclo de vida:
 * - ``single``: Crea una **única instancia compartida (Singleton) retenida en memoria**. Ejemplos
 * de uso: Repositorios, clientes HTTP, bases de datos, configuradores globales.
 * - ``factory``: Crea una **instancia nueva e independiente cada vez que se solicita la
 * dependencia**. Ejemplos de uso: Casos de uso (*Interactors*), formateadores, validadores,
 * *helpers* sin estado.
 *
 * La llamada `get()` instruye a Koin a resolver e inyectar automáticamente las dependencias
 * previas registradas en el contenedor (como el `Context` global provisto por `androidContext(...)`).
 *
 * @see AppKoinModule
 */
val labKoinModule = module {
    // ① Opción Explícita (*Manual DSL*)
    // Registra el configurador como un Singleton (mantiene estado/configuración global)
//    single<KoinLabConfigurator> {
//        CoreKoinLabConfigurator(context = get())
//    }

    // Registra el formateador como Factory (crea una nueva instancia efímera en cada inyección)
//    factory {
//        KoinLabMessageFormatter()
//    }

    // ② Opción Sintáctica (*Constructor Binding DSL*)
    // Se usa 'bind' para enlazar el tipo concreto con su interfaz/contrato
    singleOf(::CoreKoinLabConfigurator) bind KoinLabConfigurator::class

    // Para KoinLabMessageFormatter no hace falta 'bind' porque no implementa ninguna interfaz
    factoryOf(::KoinLabMessageFormatter)
}
