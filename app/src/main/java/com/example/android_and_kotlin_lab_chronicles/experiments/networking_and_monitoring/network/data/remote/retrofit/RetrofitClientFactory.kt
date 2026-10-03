package com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.remote.retrofit

import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.interceptors.AuthInterceptor
import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.interceptors.NetworkMetricsInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

/**
 * ##### Factoría de Clientes Retrofit & OkHttp
 *
 * Configura e instancia el cliente OkHttp (*timeouts*, interceptores de aplicación y red)
 * y el motor de Retrofit con el convertidor de `kotlinx.serialization`.
 */
object RetrofitClientFactory {

    private const val DEFAULT_TIMEOUT_SECONDS = 15L

    /**
     * Construye una instancia personalizada de [OkHttpClient].
     *
     * @param authTokenProvider Proveedor de token dinámico para [AuthInterceptor].
     * @param isDebug Habilita el log detallado de HTTP ([HttpLoggingInterceptor.Level.BODY]).
     */
    fun createOkHttpClient(
        authTokenProvider: () -> String,
        isDebug: Boolean = true
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            // Application Interceptor (ejecución lógica única)
            .addInterceptor(AuthInterceptor(authTokenProvider))
            .apply {
                if (isDebug) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }
            // Network Interceptor (inspección de socket / latencia real)
            .addNetworkInterceptor(NetworkMetricsInterceptor())
            .build()
    }

    /**
     * Crea una implementación de interfaz de API parametrizada.
     *
     * @param baseUrl URL base que DEBE finalizar con barra `/`.
     * @param okHttpClient Cliente HTTP previamente configurado.
     */
    inline fun <reified T> createService(
        baseUrl: String,
        okHttpClient: OkHttpClient
    ): T {
        val jsonInstance = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        val contentType = "application/json; charset=UTF-8".toMediaType()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(jsonInstance.asConverterFactory(contentType))
            .build()
            .create(T::class.java)
    }
}
