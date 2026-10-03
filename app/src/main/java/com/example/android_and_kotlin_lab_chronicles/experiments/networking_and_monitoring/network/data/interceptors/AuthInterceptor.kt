package com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.interceptors

import okhttp3.Interceptor
import okhttp3.Response

/**
 * ##### *Application Interceptor*: Autenticación y Cabeceras Globales
 *
 * Actúa en la capa superior del *pipeline* de OkHttp. Se ejecuta exactamente una vez
 * por *request* lógico (sobrevive a redirecciones HTTP y consultas servidas desde caché).
 *
 * @property tokenProvider Lambda para la extracción dinámica de *tokens* de autenticación. Se usa
 * una lambda para que el *interceptor* lea siempre el *token* más reciente en tiempo real (incluso si
 * el usuario cerró/inició sesión o si el *token* se renovó automáticamente en segundo plano) sin
 * tener que recrear la instancia del cliente ``OkHttpClient``.
 */
class AuthInterceptor(
    private val tokenProvider: () -> String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer ${tokenProvider()}")
            .header("User-Agent", "Android-Kotlin-Lab-Chronicles/1.0")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
