package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.alarm_manager

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Módulo de Hilt encargado de configurar las reglas de inyección de dependencias para
 * el subsistema de alarmas.
 *
 * Utiliza anotaciones de Dagger para enlazar interfaces abstractas con sus implementaciones
 * concretas de infraestructura, cumpliendo estrictamente con el Principio de Inversión de Dependencias.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmModule {

    /**
     * Vincula la implementación concreta [AlarmSchedulerImpl] con su contrato abstracto [AlarmScheduler].
     *
     * Esto le indica a Hilt que cada vez que un componente requiera un [AlarmScheduler],
     * debe proveer una instancia de [AlarmSchedulerImpl].
     *
     * @param impl La implementación concreta basada en infraestructura de Android.
     * @return El contrato abstracto consumido por la capa de presentación/dominio.
     */
    @Binds
    abstract fun bindAlarmScheduler(
        impl: AlarmSchedulerImpl
    ): AlarmScheduler
}