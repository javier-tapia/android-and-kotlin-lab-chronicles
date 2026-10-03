package com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.domain

import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.dto.UserDto
import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.remote.retrofit.UserApiService
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.io.IOException
import kotlin.time.measureTimedValue

/**
 * ##### Caso de Uso: Ejecución e Inspección de Peticiones con Retrofit
 *
 * Encapsula la llamada a [UserApiService], aislando las excepciones de bajo nivel
 * de la JVM ([IOException]) y de des-serialización ([SerializationException]) hacia un [NetworkResult].
 *
 * ###### Criterio de ingeniería / *Clean Architecture*
 * Si el módulo fuera Kotlin Multiplatform (KMP) con *target* iOS/Desktop, la buena práctica dictaría
 * usar el *import* de Kotlin (``kotlin.io.IOException``). Al ser un proyecto nativo puramente
 * Android (JVM), la convención al trabajar con llamadas directas a OkHttp es importar
 * ``java.io.IOException`` para mantener coherencia explícita con las firmas lanzadas
 * por el cliente HTTP subyacente.
 */
class RetrofitLabUseCase(
    private val apiService: UserApiService
) {

    /**
     * Obtiene un usuario por ID midiendo la latencia completa de la invocación.
     *
     * Captura [IOException] lanzada explícitamente por el motor OkHttp subyacente
     * ante fallos de E/S en red (``SocketTimeout``, ``UnknownHostException``, etc.).
     */
    suspend fun executeFetchUser(userId: String): NetworkResult<UserDto> {
        return try {
            val (response, duration) = measureTimedValue {
                apiService.fetchUser(userId = userId)
            }
            parseResponse(response, duration.inWholeMilliseconds)
        } catch (e: IOException) {
            // Captura fallos de E/S de la pila OkHttp/Retrofit (Timeout, DNS, drop de socket)
            NetworkResult.NetworkError(e)
        } catch (e: SerializationException) {
            // Captura fallos de parseo de JSON (mismatch de tipos o JSON malformado)
            NetworkResult.SerializationError(e)
        } catch (e: Exception) {
            NetworkResult.NetworkError(e)
        }
    }

    private fun parseResponse(
        response: Response<UserDto>?,
        durationMs: Long
    ): NetworkResult<UserDto> {
        val safeResponse = response ?: return NetworkResult.NetworkError(
            IllegalStateException("Response payload is null")
        )

        return if (safeResponse.isSuccessful) {
            safeResponse.body()?.let { dto ->
                NetworkResult.Success(data = dto, executionTimeMs = durationMs)
            } ?: NetworkResult.HttpError(code = safeResponse.code(), message = "HTTP body was null")
        } else {
            NetworkResult.HttpError(
                code = safeResponse.code(),
                message = safeResponse.errorBody()?.string()
            )
        }
    }
}
