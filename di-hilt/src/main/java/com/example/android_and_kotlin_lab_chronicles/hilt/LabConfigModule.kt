package com.example.android_and_kotlin_lab_chronicles.hilt

import android.content.Context
import android.content.pm.ApplicationInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para la provisión de configuraciones globales del laboratorio.
 *
 * - ``@Module``: Registra esta clase como una fábrica proveedora de nodos (dependencias)
 * para el grafo dirigido de Hilt.
 * - ``@InstallIn(SingletonComponent::class)``: Determina el alcance (*scope*) y el ciclo de vida
 * de este sub-grafo. Al usar [SingletonComponent], las dependencias aquí declaradas nacerán
 * con el proceso de la aplicación y vivirán en memoria hasta que el sistema operativo
 * destruya el APK.
 */
@Module
@InstallIn(SingletonComponent::class)
object LabConfigModule {
    /**
     * Provee la instancia única y centralizada para la interfaz [LabConfigurator].
     *
     * ##### ¿Por qué es obligatorio el uso de un `@Module` con `@Provides` acá?
     *
     * 1. **POLIMORFISMO**: El consumidor (ej. ViewModels en el módulo `:app`) solicita la abstracción
     * [LabConfigurator]. Dado que las interfaces no tienen constructor inyectable (`@Inject`), este método
     * le indica explícitamente a Hilt qué implementación concreta instanciar ([CoreLabConfigurator]).
     * 2. **CONSTRUCCIÓN CON CONTEXTO DINÁMICO (Inversión de Control)**: La clase requiere una bandera lógica
     * para identificar el entorno. En lugar de acoplar este módulo de infraestructura a la clase ``BuildConfig``
     * (lo que forzaría a activar características de generación de código pesadas en este submódulo),
     * se delega la responsabilidad al SDK de Android analizando el estado del Manifest en tiempo de ejecución.
     *
     * @param context El contexto global de la aplicación, provisto de forma segura por Hilt
     * mediante la anotación ``@ApplicationContext``. Al ser el contexto del ciclo de vida de la App
     * (y no de una Activity), se garantiza la total inmunidad contra fugas de memoria (*memory leaks*).
     * @return Una implementación singleton de [LabConfigurator] parametrizada según el entorno de ejecución.
     */
    @Provides
    @Singleton
    fun provideLabConfigurator(
        @ApplicationContext context: Context
    ): LabConfigurator {
        // Inspección dinámica de flags: Se verifica si la bandera 'android:debuggable="true"'
        // está activa en el Manifest final fusionado.
        val isDebuggable = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

        return CoreLabConfigurator(isDebug = isDebuggable)
    }
}
