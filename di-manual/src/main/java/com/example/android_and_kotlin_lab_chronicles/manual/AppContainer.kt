package com.example.android_and_kotlin_lab_chronicles.manual

import android.content.Context

/**
 * Contenedor de dependencias manual (*Service Locator/Manual DI Container*).
 *
 * Administra la instanciación y el ciclo de vida de las dependencias compartidas
 * a nivel de aplicación sin depender de *frameworks* externos.
 */
class AppContainer(context: Context) {

    /**
     * Instancia de Singleton (Lazy) creada manualmente.
     * Se instancia la primera vez que se accede a ella y se reutiliza.
     */
    val manualLabConfigurator: ManualLabConfigurator by lazy {
        CoreManualLabConfigurator(context = context.applicationContext)
    }
}
