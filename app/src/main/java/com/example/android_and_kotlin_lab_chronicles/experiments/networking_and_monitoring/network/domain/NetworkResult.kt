package com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.domain

/**
 * Modelo de resultado unificado para abstracción de respuestas HTTP en el laboratorio.
 * Permite homologar las salidas de Retrofit y Ktor hacia la capa de UI.
 */
sealed interface NetworkResult<out T> {

    /**
     * Respuesta exitosa con datos parseados y el tiempo de ejecución en ms.
     */
    data class Success<T>(
        val data: T,
        val executionTimeMs: Long
    ) : NetworkResult<T>

    /**
     * Respuesta de error HTTP (códigos 4xx o 5xx).
     */
    data class HttpError(
        val code: Int,
        val message: String?,
        val executionTimeMs: Long? = null
    ) : NetworkResult<Nothing>

    /**
     * Error de conectividad, I/O o timeout.
     */
    data class NetworkError(
        val throwable: Throwable
    ) : NetworkResult<Nothing>

    /**
     * Error en la deserialización del cuerpo de la respuesta.
     */
    data class SerializationError(
        val throwable: Throwable
    ) : NetworkResult<Nothing>
}
