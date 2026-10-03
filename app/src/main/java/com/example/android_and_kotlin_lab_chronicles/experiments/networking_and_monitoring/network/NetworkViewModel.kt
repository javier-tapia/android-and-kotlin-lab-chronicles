package com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.dto.UserDto
import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.remote.ktor.KtorClientFactory
import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.remote.retrofit.RetrofitClientFactory
import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.remote.retrofit.UserApiService
import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.domain.KtorLabUseCase
import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.domain.NetworkResult
import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.domain.RetrofitLabUseCase
import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.ui.NetworkUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ###### Nota de arquitectura
 * Se instancia por defecto el cliente dentro del ViewModel para mantener
 * el laboratorio funcional de forma autónoma (sin requerir DI como Hilt/Koin por ahora).
 *
 * ###### Sobre los servicios para probar
 * - [https://jsonplaceholder.typicode.com/](https://jsonplaceholder.typicode.com/): Es un servicio
 * online gratuito y público de REST API pensado para desarrollo, pruebas y prototipado.
 * Al consultar rutas como ``/users/1``, devuelve datos ficticios (*mocks*) en JSON de forma
 * síncrona sin requerir autenticación ni base de datos real.
 * - [https://httpbin.org/delay/10](https://httpbin.org/delay/10): Es un servicio público gratuito
 * (similar a JSONPlaceholder) diseñado específicamente para probar comportamientos
 * y casos borde en clientes HTTP. El endpoint ``/delay/10`` le indica al servidor que espere
 * intencionalmente 10 segundos antes de responder, ideal para probar *Timeouts* (comprobar que la app
 * capture correctamente una ``SocketTimeoutException`` y muestre el mensaje de error adecuado en la UI).
 *
 * ###### Sobre el *token*
 * El mismo provendría de un *backend* al cual consulta el cliente. El flujo habitual funciona así:
 * 1. La aplicación hace un *login* enviando usuario y contraseña (o se autentica vía OAuth/Google).
 * 2. El *backend* valida las credenciales y responde enviando un JWT (JSON *Web Token*) o un *Access Token*.
 * 3. La app guarda ese *token* localmente en un almacenamiento seguro en el dispositivo (por ejemplo,
 * usando ``DataStore`` con cifrado o ``EncryptedSharedPreferences``).
 * 4. Cuando el ``AuthInterceptor`` ejecuta ``authTokenProvider()``, invoca un método (generalmente
 * de un repositorio de sesión o *TokenManager*) que **lee el *token* actual desde el almacenamiento
 * local** y lo adjunta en la cabecera ``Authorization: Bearer <token>``.
 */
class NetworkViewModel(
    private val retrofitUseCase: RetrofitLabUseCase = RetrofitLabUseCase(
        apiService = RetrofitClientFactory.createService<UserApiService>(
            baseUrl = "https://jsonplaceholder.typicode.com/",
            okHttpClient = RetrofitClientFactory.createOkHttpClient(
                authTokenProvider = { "SAMPLE_BEARER_TOKEN" },
                isDebug = true
            )
        )
    ),
    private val ktorUseCase: KtorLabUseCase = KtorLabUseCase(
        httpClient = KtorClientFactory.createHttpClient(
            baseUrl = "https://jsonplaceholder.typicode.com/",
            authTokenProvider = { "SAMPLE_BEARER_TOKEN" },
            isDebug = true
        )
    )
) : ViewModel() {

    private val _uiState = MutableStateFlow<NetworkUiState>(NetworkUiState.Idle)
    val uiState: StateFlow<NetworkUiState> = _uiState.asStateFlow()

    fun fetchUserWithRetrofit(userId: String = "1") {
        executeRequest("Retrofit") { retrofitUseCase.executeFetchUser(userId) }
    }

    fun fetchUserWithKtor(userId: String = "1") {
        executeRequest("Ktor") { ktorUseCase.executeFetchUser(userId) }
    }

    private fun executeRequest(
        clientName: String,
        block: suspend () -> NetworkResult<UserDto>
    ) {
        viewModelScope.launch {
            _uiState.value = NetworkUiState.Loading

            when (val result = block()) {
                is NetworkResult.Success -> {
                    _uiState.value = NetworkUiState.Success(
                        user = result.data,
                        latencyMs = result.executionTimeMs,
                        clientName = clientName
                    )
                }

                is NetworkResult.HttpError -> {
                    _uiState.value = NetworkUiState.Error(
                        message = "[$clientName] HTTP ${result.code}: ${result.message ?: "Error desconocido"}"
                    )
                }

                is NetworkResult.NetworkError -> {
                    _uiState.value = NetworkUiState.Error(
                        message = "[$clientName] Error de red: ${result.throwable.localizedMessage}"
                    )
                }

                is NetworkResult.SerializationError -> {
                    _uiState.value = NetworkUiState.Error(
                        message = "[$clientName] Error de parseo JSON: ${result.throwable.localizedMessage}"
                    )
                }
            }
        }
    }
}
