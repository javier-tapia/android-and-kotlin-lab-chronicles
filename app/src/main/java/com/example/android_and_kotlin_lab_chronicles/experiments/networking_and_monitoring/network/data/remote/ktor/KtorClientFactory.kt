package com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.remote.ktor

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * ##### Fábrica centralizada para la creación y configuración de instancias de [HttpClient] en Ktor.
 *
 * Aplica los plugins esenciales para un cliente de red moderno:
 * - **Motor (*Engine*):** [OkHttp] para mantener compatibilidad con interceptores de Android/Java
 * y compartir el *stack* de red con Retrofit. Para usar el motor **CIO** (***Coroutine-based I/O***),
 * más común en KMP, simplemente se reemplaza el parámetro del motor en la instancia del cliente sustituyendo
 * ``HttpClient(OkHttp)`` por ``HttpClient(CIO)``. Hay que tener en cuenta que en Gradle se debe
 * incluir el artefacto ``io.ktor:ktor-client-cio`` y que, al ser un motor 100% Kotlin puramente
 * asincrónico, **no utiliza la pila ni los interceptores nativos de OkHttp** (no se pueden reutilizar
 * los interceptores de OkHttp ni compartir el *pool* de conexiones con Retrofit).
 * - ``ContentNegotiation``: Integración con `kotlinx.serialization` para el parseo automático de JSONs.
 * - ``HttpTimeout``: Definición de límites de tiempo para solicitudes, conexión y lectura de *sockets*.
 * - ``Logging``: Registro detallado en el *Logcat* de cabeceras y cuerpo de las peticiones en
 * entornos de depuración.
 * - ``DefaultRequest``: Inyección dinámica de cabeceras globales como `Content-Type` y
 * `Authorization: Bearer <token>`.
 */
object KtorClientFactory {

    /**
     * Crea y configura un cliente [HttpClient] de Ktor listo para usar.
     *
     * @param baseUrl La URL base para todas las solicitudes enviadas con este cliente
     * (ej.: `https://api.example.com/`).
     * @param authTokenProvider Proveedor funcional diferido para obtener el *token Bearer* en tiempo
     * de ejecución. Permite evaluar *tokens* actualizados en sesión sin recrear la instancia del cliente.
     * @param isDebug Determina si se deben habilitar los *logs* de red detallados mediante el
     * plugin [Logging].
     * @return Una instancia inmutable y lista para usar de [HttpClient].
     */
    fun createHttpClient(
        baseUrl: String,
        authTokenProvider: (() -> String)? = null,
        isDebug: Boolean = true
    ): HttpClient {
        return HttpClient(OkHttp) {

            // Configuración de timeouts de solicitud
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 10_000
                socketTimeoutMillis = 10_000
            }

            // Conversión de JSON vía kotlinx.serialization
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }

            // Registro de logs de HTTP en Logcat
            if (isDebug) {
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.BODY
                }
            }

            // Configuración por defecto agregada a cada llamada saliente
            defaultRequest {
                url(baseUrl)
                header(HttpHeaders.ContentType, "application/json")
                authTokenProvider?.invoke()?.let { token ->
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }
    }
}
