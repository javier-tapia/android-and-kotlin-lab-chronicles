package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.workmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import java.util.UUID
import javax.inject.Inject

/**
 * *ViewModel* encargado de gestionar el ciclo de vida y la ejecución de tareas en segundo plano
 * mediante *WorkManager* en un entorno de *Clean Architecture* con Hilt.
 *
 * Utiliza [AndroidViewModel] para acceder de manera directa al contexto de la aplicación,
 * necesario para inicializar *WorkManager*, y expone flujos reactivos basados en [StateFlow]
 * para monitorear el estado actual de los trabajos en tiempo real.
 *
 * ###### Componentes clave y su funcionamiento técnico
 *
 * - `WorkInfo`: Representa la información de estado en tiempo real de una [androidx.work.WorkRequest].
 *     - **Qué es**: Una estructura de datos provista por *WorkManager* que encapsula el estado actual
 *       de una tarea, su identificador único ([UUID]), los datos de salida ([androidx.work.Data]),
 *       las etiquetas asociadas y el contador de reintentos (``runAttemptCount``).
 *     - **Para qué sirve**: Permite observar de manera reactiva el ciclo de vida de un *worker*
 *       (a través de flujos como `getWorkInfoByIdFlow`), traduciendo los estados internos del sistema
 *       ([WorkInfo.State.RUNNING], [WorkInfo.State.SUCCEEDED], [WorkInfo.State.FAILED], etc.) para
 *       reflejar el progreso o la finalización de las tareas directamente en la interfaz de usuario.
 * - `Constraints`: Define las condiciones ambientales y de *hardware* requeridas para que un
 * *Worker* pueda ejecutarse.
 *     - **Qué es**: Un objeto de configuración construido mediante [Constraints.Builder] que especifica
 *       requisitos estrictos como tipo de red ([NetworkType]), estado de carga de la batería
 *       (``setRequiresCharging``), nivel óptimo de batería (``setRequiresBatteryNotLow``), espacio
 *       de almacenamiento o estado de reposo (``setRequiresDeviceIdle``).
 *     - **Para qué sirve**: Optimiza el consumo energético y de recursos del dispositivo. Si el entorno
 *       no cumple con las condiciones establecidas, *WorkManager* retiene la tarea de manera
 *       indefinida en la cola bajo el estado [WorkInfo.State.ENQUEUED] hasta que el entorno sea propicio.
 * - `OneTimeWorkRequestBuilder`: Constructor especializado para crear solicitudes de trabajo de
 * ejecución única ([androidx.work.OneTimeWorkRequest]).
 *     - **Qué es**: Un builder que permite empaquetar un [androidx.work.Worker] específico (por
 *     ejemplo, [SyncDataWorker]), configurando sus restricciones, datos de entrada, políticas de
 *     reintento exponencial y retardos iniciales para que la tarea se ejecute una única vez.
 *
 * Otros *Builders* disponibles en el ecosistema de *WorkManager*:
 * - [androidx.work.PeriodicWorkRequestBuilder]: Se utiliza para programar tareas recurrentes que
 * se repiten de forma periódica a intervalos regulares (por ejemplo, sincronizaciones de datos cada
 * 2 horas). Está sujeto a un intervalo mínimo impuesto por el sistema operativo (generalmente de
 * 15 minutos) y maneja ejecuciones automatizadas de manera recurrente.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class WorkManagerLabViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val workManager = WorkManager.getInstance(application)

    /**
     * Identificador único ([UUID]) mutable de la tarea seleccionada actualmente para su monitoreo.
     * Actúa como fuente reactiva para que el flujo de estado se actualice dinámicamente
     * al cambiar de tarea.
     */
    private val _selectedWorkId = MutableStateFlow<UUID?>(null)

    /**
     * Flujo de estado expuesto a la UI que emite de forma reactiva los cambios de [WorkInfo]
     * de la tarea actualmente seleccionada.
     *
     * - Utiliza `flatMapLatest` para cancelar la observación del ID anterior y suscribirse
     *   automáticamente al nuevo flujo provisto por `workManager.getWorkInfoByIdFlow(id)`.
     * - Se configura con `SharingStarted.WhileSubscribed(5000L)` para mantener la subscripción
     *   activa durante 5 segundos tras un cambio de configuración o pérdida de foco (como una rotación),
     *   evitando cancelaciones y re-suscripciones innecesarias.
     */
    val workInfoState: StateFlow<WorkInfo?> = _selectedWorkId
        .flatMapLatest { id ->
            if (id == null) flowOf(null)
            else workManager.getWorkInfoByIdFlow(id)
        }
        .stateIn(
            scope = viewModelScope,
            // Mantiene activo 5 segundos tras rotación o pérdida de foco
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    /**
     * Encola una tarea única de sincronización aplicando una política de control de concurrencia.
     *
     * Configura restricciones estrictas de hardware (conexión de red activa y dispositivo cargando)
     * y utiliza ``enqueueUniqueWork`` para evitar duplicidad según la [policy] elegida:
     * - [ExistingWorkPolicy.KEEP]: Mantiene el trabajo existente si ya está en curso y descarta el nuevo.
     * - [ExistingWorkPolicy.REPLACE]: Cancela el trabajo activo anterior y encola el nuevo de inmediato.
     *
     * @param policy Política de unicidad que define el comportamiento si ya existe una tarea
     * con el mismo nombre.
     */
    fun scheduleUniqueSyncTask(policy: ExistingWorkPolicy) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val request = OneTimeWorkRequestBuilder<SyncDataWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniqueWork(
            UNIQUE_SYNC_NAME,
            policy,
            request
        )

        // Actualizamos el ID monitoreado; la UI lo reflejará de inmediato
        _selectedWorkId.value = request.id
    }

    /**
     * Configura y ejecuta una cadena de tareas secuenciales utilizando ``beginWith`` y ``then``.
     *
     * Permite encadenar múltiples [androidx.work.OneTimeWorkRequest] para que se ejecuten
     * en un orden estricto. El siguiente eslabón solo inicia si el anterior finaliza con
     * éxito (`Result.success()`). Si una tarea falla, la cadena se detiene a menos que se
     * configure una política de continuación.
     */
    fun scheduleChainedTasks() {
        val firstRequest = OneTimeWorkRequestBuilder<SyncDataWorker>().build()
        val secondRequest = OneTimeWorkRequestBuilder<SyncDataWorker>().build()

        // Ejecuta 'firstRequest' y, al completarse exitosamente, dispara 'secondRequest' en secuencia.
        workManager.beginWith(listOf(firstRequest))
            .then(secondRequest)
            .enqueue()

        // Si en lugar de 'workManager.beginWith(...)' se quisiera que esta cadena sea única (por
        // ejemplo, para evitar que se dispare múltiples veces si el usuario hace clic
        // compulsivamente), se utiliza la contraparte de unicidad para cadenas: 'beginUniqueWork'
//        workManager.beginUniqueWork(
//            "UNIQUE_CHAIN_NAME",
//            ExistingWorkPolicy.KEEP, // O REPLACE
//            listOf(firstRequest)
//        ).then(secondRequest)
//            .enqueue()

        // Se monitorea el ID del último eslabón de la cadena para reflejar su evolución en la UI
        _selectedWorkId.value = secondRequest.id
    }

    companion object {
        /** Identificador único global para las tareas de sincronización basadas en nombre. */
        private const val UNIQUE_SYNC_NAME = "sync_data_unique_work"
    }
}
