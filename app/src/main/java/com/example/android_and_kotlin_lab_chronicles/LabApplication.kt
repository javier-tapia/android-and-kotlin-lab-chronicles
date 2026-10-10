package com.example.android_and_kotlin_lab_chronicles

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.android_and_kotlin_lab_chronicles.experiments.dependency_injection.koin.appKoinModule
import com.example.android_and_kotlin_lab_chronicles.koin.labKoinModule
import com.example.android_and_kotlin_lab_chronicles.manual.AppContainer
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import javax.inject.Inject

/**
 * Punto de entrada global de la aplicación (`Application`).
 *
 * En esta clase coexisten dos enfoques estructurales de *Dependency Injection*:
 * 1. **Hilt (*Compile-time* / *Code Generation*):**
 *    Controlado por ``@HiltAndroidApp``, que instruye a KSP/Dagger para compilar el Grafo Dirigido
 *    Acíclico (DAG) a nivel de proceso (`SingletonComponent`).
 * 2. **Koin (*Runtime* / *Service Locator DSL*):**
 *    Inicializado explícitamente en [onCreate] mediante [startKoin]. No requiere procesamiento
 *    de anotaciones; resuelve dependencias bajo demanda mediante inspección de tipos
 *    en tiempo de ejecución.
 *
 * Inicialización del contenedor de Koin.
 *
 * - ``androidLogger``: Conecta el sistema de logs interno de Koin con Logcat para depurar
 * la resolución e inyección de dependencias.
 * - ``androidContext``: Inyecta el ``Context`` de la [Application] dentro del grafo de Koin,
 * permitiendo que cualquier módulo que requiera un contexto (ej. bases de datos,
 * ``SharedPreferences``) pueda resolverlo dinámicamente.
 * - ``modules``: Lista de módulos ([org.koin.core.module.Module]) que contienen las reglas
 * de inyección para el *Service Locator*.
 */
@HiltAndroidApp
class LabApplication : Application(), Configuration.Provider {
    // Contenedor de DI Manual accesible globalmente
    lateinit var appContainer: AppContainer
        private set

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        // Inicialización del contenedor de DI Manual
        appContainer = AppContainer(this)

        // Inicialización de Koin
        startKoin {
            androidLogger()
            androidContext(this@LabApplication)
            modules(
                labKoinModule, // Proviene de ':di-koin'
                appKoinModule  // Proviene de ':app'
            )
        }
    }

    /**
     * ``WorkManager`` **no puede instanciar *Workers* con constructores personalizados** sin un
     * `WorkerFactory`. Al usar Hilt, es obligatorio proveer `HiltWorkerFactory`
     * desde la clase `Application`.
     *
     * Además, se debe agregar un `<provider>` en el Manifest para desactivar el inicializador
     * automático de WorkManager.
     */
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
