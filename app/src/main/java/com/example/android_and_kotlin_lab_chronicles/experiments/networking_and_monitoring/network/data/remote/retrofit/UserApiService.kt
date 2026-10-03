package com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.remote.retrofit

import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * ##### Servicio API de Retrofit: Contrato de Red
 *
 * - Declara los endpoints HTTP para la gestión de usuarios.
 * - Utiliza *suspend functions* para la integración nativa con Corrutinas de Kotlin.
 * - Retorna [Response] para delegar la inspección de metadatos (*status code, headers, errorBody*)
 * al caso de uso/capa superior sin lanzar [retrofit2.HttpException].
 */
interface UserApiService {

    /**
     * Obtiene los detalles de un usuario específico por su identificador.
     *
     * @param journeyId Header dinámico para trazabilidad de la transacción/sesión.
     * @param sessionId Parámetro opcional de consulta para validación de estado de sesión.
     * @param userId Identificador del usuario que sustituye la variable de ruta `{id}`.
     * @return [Response] envolviendo [UserDto] en caso de éxito HTTP ($2xx$).
     */
    @GET("users/{id}")
    suspend fun fetchUser(
        @Header("journey_id") journeyId: String? = null,
        @Path("id") userId: String,
        @Query("session_id") sessionId: String? = null,
    ): Response<UserDto>

    /**
     * Actualiza o crea un registro de usuario en el servidor.
     *
     * @param journeyId Header dinámico para trazabilidad.
     * @param userPayload Objeto [UserDto] que será serializado a JSON por el `ConverterFactory`.
     * @return [Response] sin cuerpo ($204$ No Content o $200$ OK) o con el DTO actualizado.
     */
    @POST("users/update")
    suspend fun updateUser(
        @Header("journey_id") journeyId: String? = null,
        @Body userPayload: UserDto
    ): Response<UserDto>
}
