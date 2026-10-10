package com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.interceptors

import com.example.android_and_kotlin_lab_chronicles.core.utils.CustomLogger.log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * ##### *Network Interceptor*: Métricas de Transporte y *Socket*
 *
 * Actúa en la capa de transporte (*wire level*). Se ejecuta por cada salto de red (*redirects*)
 * y no se invoca si la respuesta es resuelta por la caché de OkHttp.
 * Permite auditar *headers* finales (post-compresión/headers implícitos) y
 * latencia RTT (*Round Trip Time*, Tiempo de ida y vuelta).
 *
 * En producción, en lugar de un ``Log.d``, este interceptor enviaría la métrica directamente a la
 * herramienta de telemetría/APM.
 *
 * ###### Nota sobre RTT y Latencia
 * RTT es el tiempo medido en milisegundos que transcurre desde que un paquete de datos (en este
 * caso la solicitud HTTP) sale del cliente (el dispositivo Android), viaja por la red hasta el
 * servidor destino, y la primera respuesta/acknowledgement regresa al dispositivo.
 *
 * **Diferencia técnica con la latencia simple**: La latencia mide solo el tiempo que tarda el
 * paquete en ir de A a B (un solo sentido). El RTT mide el ciclo completo (A -> B -> A), que es la
 * métrica real que impacta al usuario final al esperar una respuesta HTTP.
 */
class NetworkMetricsInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startNs = System.nanoTime()

        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            log("NetworkMetricsInterceptor", "❌ Fallo en la llamada a ${request.url}: ${e.message}")
            throw e
        }

        val tookMs = (System.nanoTime() - startNs) / 1e6
        log(
            "NetworkMetricsInterceptor",
            "🌐 ${request.method} ${request.url} -> Status: ${response.code} (${tookMs}ms)"
        )

        return response
    }
}
