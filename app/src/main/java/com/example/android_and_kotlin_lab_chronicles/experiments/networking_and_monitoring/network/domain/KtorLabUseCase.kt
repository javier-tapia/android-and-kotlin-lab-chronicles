package com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.domain

import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.serialization.JsonConvertException
import java.io.IOException
import kotlin.time.measureTimedValue

/**
 * ##### Caso de Uso: Ejecución e Inspección de Peticiones con Ktor
 *
 * Encapsula la llamada a la API REST, mide la latencia de red *end-to-end* de forma explícita
 * y captura las excepciones nativas de Ktor mapeándolas al modelo unificado [NetworkResult].
 *
 * @property httpClient Instancia de [HttpClient] previamente configurada con sus plugins necesarios.
 */
class KtorLabUseCase(
    private val httpClient: HttpClient
) {
    /**
     * Obtiene los detalles de un usuario dado su [userId] mediante el cliente de Ktor.
     *
     * @param userId Identificador único del usuario a consultar.
     * @return [NetworkResult] encapsulando la respuesta deserializada ([UserDto]), la latencia de
     * la llamada o el error correspondiente.
     */
    suspend fun executeFetchUser(userId: String): NetworkResult<UserDto> {
        return try {
            val (user, duration) = measureTimedValue {
                httpClient.get("users/$userId").body<UserDto>()
            }

            NetworkResult.Success(
                data = user,
                executionTimeMs = duration.inWholeMilliseconds
            )
        } catch (e: ClientRequestException) {
            // Manejo de errores HTTP 4xx
            NetworkResult.HttpError(
                code = e.response.status.value,
                message = e.localizedMessage
            )
        } catch (e: ServerResponseException) {
            // Manejo de errores HTTP 5xx
            NetworkResult.HttpError(
                code = e.response.status.value,
                message = e.localizedMessage
            )
        } catch (e: JsonConvertException) {
            // Fallo en la deserialización JSON
            NetworkResult.SerializationError(e)
        } catch (e: IOException) {
            // Errores de conectividad o timeout
            NetworkResult.NetworkError(e)
        } catch (e: Exception) {
            // Errores genéricos no previstos
            NetworkResult.NetworkError(e)
        }
    }
}
