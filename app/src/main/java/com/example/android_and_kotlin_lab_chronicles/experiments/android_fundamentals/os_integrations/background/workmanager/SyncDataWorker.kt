package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android_and_kotlin_lab_chronicles.core.utils.CustomLogger.log
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlin.coroutines.cancellation.CancellationException

/**
 * *Worker* en segundo plano encargado de ejecutar la sincronización de datos de forma asíncrona.
 *
 * Utiliza [CoroutineWorker] para correr de manera no bloqueante mediante corrutinas en un hilo
 * optimizado para E/S, simplificando las llamadas de red o base de datos.
 *
 * ###### Componentes clave, anotaciones y su funcionamiento técnico
 * - ``@HiltWorker``: Anotación que habilita a Hilt para reconocer este Worker e inyectarle
 *   dependencias personalizadas (como [SyncRepository]) mediante un [androidx.hilt.work.HiltWorkerFactory].
 * - ``@AssistedInject`` y ``@Assisted``: Patrón de inyección asistida. Permiten combinar dependencias
 *   del grafo de Hilt con parámetros que solo se conocen en tiempo de ejecución por parte del sistema,
 *   específicamente el [Context] y los [WorkerParameters].
 * - [WorkerParameters]: Estructura que provee los metadatos de la tarea, tales como su identificador
 *   único, etiquetas asociadas, datos de entrada y el contador de reintentos actuales ([runAttemptCount]).
 * - [Result]: Define el veredicto del trabajo al finalizar [doWork]:
 *   - [Result.success]: Indica que la tarea concluyó con éxito, cerrando su ciclo de vida.
 *   - [Result.retry]: Ordena un nuevo intento de ejecución posterior.
 *   - [Result.failure]: Marca la tarea como fallida de manera definitiva.
 * - [CancellationException]: Excepción que **nunca debe atraparse como un error estándar** (clave cuando
 *   se usa una política como [androidx.work.ExistingWorkPolicy.REPLACE]). Debe propagarse
 *   (`throw e`) para permitir que *WorkManager* cancele de forma segura la ejecución anterior.
 */
@HiltWorker
class SyncDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val syncRepository: SyncRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            val success = syncRepository.performRemoteSync()
            if (success) Result.success() else Result.retry()
        } catch (e: CancellationException) {
            // CRUCIAL: Las cancelaciones (como las que genera REPLACE) deben
            // propagarse para que 'WorkManager' maneje el estado correctamente.
            throw e
        } catch (e: Exception) {
            log(
                "SyncDataWorker",
                "Error executing sync task. Attempt: $runAttemptCount",
                e
            )

            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }
}
