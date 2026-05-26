package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.side_effects

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

private const val TAG = "SideEffectsLab"

/**
 * Experimentos: *Side Effects* y *Effect Handlers*.
 *
 * **Las fases de Compose**: Compose tiene cuatro fases secuenciales:
 * 1. ***Composition***: Se ejecutan las funciones ``@Composable`` y se genera/actualiza el árbol de
 * la UI (la estructura de nodos).
 * 2. ***Layout***: Se miden los nodos y se posicionan en coordenadas ``(x, y)`` en la pantalla.
 * 3. ***Commit* (Fase de ciclo de vida interna)**: Una vez que Compose sabe qué hay que dibujar y
 * dónde, el *Snapshot System* consolida y "cierra" los cambios de estado en memoria.
 * Justo en este microsegundo, antes de pintar los píxeles, Compose abre su agenda y ejecuta los
 * *Effect Handlers* síncronos (como ``SideEffect``) y encola los asíncronos (``LaunchedEffect``).
 * 4. ***Draw***: El motor de renderizado recorre los nodos modificados y dibuja los píxeles reales
 * en el lienzo (Canvas) del dispositivo.
 *
 * **Regla de oro de Compose**: Cambiar el valor de un estado (escribir) no dispara una recomposición
 * en el lugar donde se realiza la escritura. Lo que dispara la recomposición es la **lectura activa de ese
 * estado en un bloque ejecutable durante la fase de Composición**.
 * Esto deja fuera a las lecturas que ocurren dentro de los *effect handlers* (``SideEffect``,
 * ``LaunchedEffect``, etc.), ya que sus bloques se ejecutan tardíamente en la fase de ***Draw/Commit***.
 * Estas lecturas están protegidas por el *framework*, el cual no las registra en la agenda de *Smart Recomposition*,
 * evitando así invalidar el alcance de recomposición (*recomposition scope*) donde reside el efecto.
 *
 * Temas:
 * - ``LaunchedEffect``: Ejecución de código suspendido vinculado a una llave.
 * - ``SideEffect``: Comunicación de estado de Compose con objetos no gestionados por Compose.
 * - ``DisposableEffect``: Efectos que requieren limpieza (cleanup) al salir de la composición.
 * - ``derivedStateOf``: Optimización de recomposición mediante estados derivados de otros estados.
 * - ``rememberCoroutineScope``: Proveedor de un alcance de corrutinas (``CoroutineScope``) vinculado
 * al ciclo de vida de la composición, diseñado para disparar tareas suspendidas desde *callbacks*
 * de UI sincrónicos.
 * - ``rememberUpdatedState``: Mecanismo de captura que almacena una referencia mutable y actualizada
 * de un valor, permitiendo que efectos de larga duración (como un ``LaunchedEffect``) accedan al
 * último dato sin forzar su propio reinicio.
 * - ``produceState``: Operador puente que transforma fuentes de datos externas y asíncronas (como
 * *flows*, *callbacks* o APIs imperativas) en un estado observable nativo de Compose mediante un
 * *scope* suspendido.
 * - ``snapshotFlow``: Un conversor inverso que analiza el estado reactivo de Compose y lo transforma
 * en un flujo continuo (``Flow``) de corrutinas cada vez que dicho estado muta, ideal para aplicar
 * operadores de filtrado o procesamiento.
 */
@Composable
fun SideEffectsLab(
    viewModel: SideEffectsViewModel = hiltViewModel()
) {
    SamplesShowcase(
        { LaunchedEffectSample() },
        { SideEffectSample() },
        { DisposableEffectSample() },
        { DerivedStateOfSample() },
        { RememberCoroutineScopeSample() },
        { RememberUpdatedStateSample(viewModel = viewModel) },
        { ProduceStateSample(viewModel = viewModel) },
        { SnapshotFlowSample(viewModel = viewModel) },
    )
}

/**
 * - Ejecuta **funciones suspendidas** (corrutinas) de forma segura dentro del ciclo de vida de la UI.
 * - Su bloque de código no se ejecuta durante la fase de *Composition*, sino que se agenda para ser
 * lanzado de forma asíncrona en la fase de *Commit*, una vez que el árbol de nodos se consolidó en memoria.
 * - Se **dispara la primera vez que el componente entra al árbol de la UI y se cancela y reinicia por
 * completo si cambian sus *keys***. Si la *key* es ``Unit``, se ejecuta una sola vez al nacer.
 * - **Comportamiento ante Scroll/Reciclaje**: Si el componente reside en un contenedor indexado (como
 * ``LazyColumn``) y sale del área visible, el efecto se cancela inmediatamente al salir del árbol de UI.
 * Al reincorporarse por scroll, volverá a ejecutar su ciclo desde cero; por ello, cualquier estado
 * sobreviviente debe ampararse en ``rememberSaveable``.
 *
 * **Uso principal en la UI**:
 * - Operaciones asíncronas ligadas estrictamente a eventos periféricos o UX de la pantalla (solicitar
 * el foco del teclado de forma automatizada, disparar animaciones estéticas iniciales, o aplicar un
 * retraso (*Debounce*) visual local para mitigar ráfagas de escritura en un buscador).
 *
 * **Antipatrones a evitar**:
 * - No se debe utilizar para gestionar lógicas de negocio persistentes, peticiones de red globales
 * o temporizadores (*timers*). Esas tareas deben delegarse al ``viewModelScope`` dentro de un ``ViewModel``,
 * ya que la UI es efímera y estructuralmente inestable para sostener la verdad de los datos.
 */
@Composable
fun LaunchedEffectSample(modifier: Modifier = Modifier) {
    val focusRequester = remember { FocusRequester() }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var searchResult by rememberSaveable { mutableStateOf("Esperando entrada de texto...") }

    // --- EFECTO 1: KEY CONSTANTE (Unit) ---
    // Se ejecuta una única vez cuando el componente se añade al árbol de la UI (fase de Commit).
    // Diseñado para inicializaciones asíncronas de la interfaz de usuario.
    LaunchedEffect(Unit) {
        Log.i(
            TAG,
            "[LaunchedEffect-Unit] Inicializando componente. Solicitando foco del teclado de forma automática."
        )
        // Retraso técnico mínimo para garantizar la existencia del nodo en el árbol de renderizado
        delay(100)
        focusRequester.requestFocus()
    }

    // --- EFECTO 2: KEY VARIABLE (searchQuery) ---
    // Se cancela la ejecución previa y se reinicia el bloque de forma automática cada vez que 'searchQuery' muta.
    // Diseñado para lógicas asíncronas reactivas (Debounce) controladas por el estado de la UI.
    LaunchedEffect(searchQuery) {
        if (searchQuery.isBlank()) {
            searchResult = "Esperando entrada de texto..."
            return@LaunchedEffect
        }

        Log.i(
            TAG,
            "[LaunchedEffect-Key] Clave modificada a '$searchQuery'. Cancelando corrutina previa y reiniciando temporizador de debounce."
        )
        searchResult = "Procesando entrada..."

        // Retraso de estabilización (Debounce) para mitigar ráfagas de escritura
        delay(500)

        Log.i(
            TAG,
            "[LaunchedEffect-Key] Debounce superado con éxito. Ejecutando operación de filtrado para: '$searchQuery'"
        )
        searchResult = "Resultados locales filtrados para: '$searchQuery'"
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTitle("LaunchedEffect (Unit vs Keys)")

        Text(
            text = "Combinación de UX única y lógica reactiva asíncrona:",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("El foco se activa solo...") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = searchResult, style = MaterialTheme.typography.bodyLarge)
    }
}

/**
 * - Ejecuta un bloque de código síncrono y no suspendido **únicamente cuando el frame actual de la UI
 * se ha consolidado con éxito**.
 * - Su ejecución ocurre de forma estricta durante la fase de *Commit*. Esto garantiza que el bloque
 * jamás se ejecute ante recomposiciones que fueron canceladas, abortadas o descartadas por el framework
 * durante la fase de *Composition*.
 * - No acepta llaves (*keys*); por diseño, se evalúa en cada ciclo de renderizado. Para restringir su
 * ejecución a escenarios específicos, se debe envolver el handler dentro de estructuras de control
 * condicionales nativas (como un bloque ``if`` condicionado por un estado reactivo).
 *
 * **Uso principal en la UI**:
 * - Sincronizar y transferir estados consolidados de Compose hacia objetos externos imperativos o de bajo
 * nivel que **no son gestionados por el framework** (emitir pulsos físicos a controladores de hardware
 * como el ``Vibrator`` manager, enviar eventos atómicos a SDKs de analíticas nativas, o actualizar
 * parámetros en sockets de comunicación en C/C++).
 *
 * **Antipatrones a evitar**:
 * - Jamás debe contener código pesado que bloquee el hilo principal de la UI, ya que retrasaría la
 * fase de *Draw* provocando caídas de frames (*lag*).
 * - No se debe utilizar para mutar variables ordinarias o estados de Compose con el fin de actualizar
 * la misma interfaz. Al ejecutarse de forma tardía en el *pipeline*, provocaría ciclos de recomposición
 * infinita (*Feedback Loops*) o desfases visuales donde la UI marcha un frame atrasada respecto al dato real.
 */
@Composable
fun SideEffectSample(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var hasError by rememberSaveable { mutableStateOf(false) }

    // --- CONTROL DE EFECTO SÍNCRONO ---
    // La estructura condicional garantiza que el bloque SideEffect solo se evalúe
    // y se agende cuando la pantalla consolide un estado de error.
    if (hasError) {
        SideEffect {
            Log.i(
                TAG,
                "[SideEffect] Estado de error consolidado en pantalla (Fase de Commit). Transmitiendo pulso al hardware."
            )

            // Obtención del servicio de hardware mediante la API imperativa del sistema operativo
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            // Configuración de un pulso único de 150 milisegundos a amplitud por defecto
            val effect =
                VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE)

            // Ejecución directa sobre el componente físico; salta las restricciones de 'Touch vibration'
            vibrator.vibrate(effect)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTitle("SideEffect (Motor de Vibración)")

        Text(
            text = "Estado del formulario: ${if (hasError) "⚠️ ERROR CRÍTICO" else "✅ Todo OK"}",
            color = if (hasError) Color.Red else Color.Unspecified,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { hasError = !hasError }) {
            Text(if (hasError) "Solucionar Error" else "Provocar Error")
        }
    }
}

/**
 * - Garantiza la ejecución de **efectos secundarios que requieren una liberación o limpieza (*cleanup*)**
 * obligatoria cuando el componente se desvincula del árbol de la UI o cuando sus *keys* mutan.
 * - Su evaluación ocurre en la fase de *Composition*, pero la ejecución del bloque se pospone para
 * la fase de *Commit*. Asegura de forma estricta que la cláusula final ``onDispose { ... }`` se ejecute
 * antes de cada reinicio provocado por un cambio de llave, o de forma definitiva al salir de la composición.
 * - **Comportamiento ante Scroll/Reciclaje**: Si el componente es destruido o reciclado por un contenedor
 * dinámico (como ``LazyColumn``), el bloque ``onDispose`` se dispara inmediatamente en el hilo principal,
 * liberando los recursos de hardware o memoria asociados en ese preciso frame.
 *
 * **Uso principal en la UI**:
 * - Enlazar y encapsular el ciclo de vida de APIs imperativas del sistema operativo o SDKs de terceros
 * que requieran una gestión manual de recursos (desregistrar suscriptores de eventos, remover
 * ``SensorEventListener`` de hardware, pausar y liberar decodificadores de video locales, o desvincular
 * *listeners* de transmisiones de red). Su propósito fundamental es prevenir fugas de memoria (*Memory Leaks*).
 *
 * **Contrato Obligatorio del Framework**:
 * - La arquitectura de Compose exige de forma estricta que la última instrucción ejecutable del bloque
 * sea la llamada a ``onDispose``. La omisión de este bloque genera un error sintáctico en tiempo de compilación.
 *
 * **Validación en Entorno de Desarrollo**:
 * - La verificación del comportamiento físico de los listeners de hardware puede emularse mediante el
 * panel lateral de controles extendidos (*Extended Controls -> Virtual Sensors*) provisto por el emulador.
 */
@Composable
fun DisposableEffectSample(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var proximityValue by remember { mutableFloatStateOf(0f) }

    // --- MANEJO DE SUSCRIPCIÓN ASOCIADA AL CICLO DE VIDA ---
    // La clave 'Unit' garantiza que el listener se registre una sola vez al entrar en la composición.
    // El bloque exige de forma obligatoria la cláusula 'onDispose' como última línea de ejecución.
    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let { proximityValue = it.values[0] }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        // Registro del callback imperativo en el servicio del sistema operativo
        sensorManager.registerListener(listener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL)

        // Cláusula de desvinculación obligatoria. Se ejecuta inmediatamente si el componente
        // abandona el árbol de UI (por scroll en LazyColumn, navegación o destrucción de la vista).
        onDispose {
            Log.i(
                TAG,
                "[DisposableEffect] El componente sale de la composición. " +
                        "Liberando recursos del sensor de hardware de forma segura."
            )
            sensorManager.unregisterListener(listener)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTitle("DisposableEffect (Sensores)")

        Text(
            text = "Sensor de Proximidad de Hardware:",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (proximityValue < 1f) "¡OBJETO CERCA!" else "Objeto lejos",
            style = MaterialTheme.typography.headlineMedium,
            color = if (proximityValue < 1f) Color.Red else Color.Green
        )
    }
}

/**
 * - Actúa como un **amortiguador (*buffer*) o filtro de notificaciones** dentro del *Snapshot
 * System* de Compose.
 * - Su función principal es observar un flujo de estados mutables que cambian a **alta frecuencia**
 * (como eventos físicos o decimales por milisegundo) y transformarlo en un nuevo estado de **baja
 * frecuencia** (como booleanos o strings formateadas).
 * - **Mecanismo de Optimización**: No impide la evaluación estructural del scope o contenedor donde se
 * modifican las variables nativas, sino que **frena en seco la propagación de la invalidez hacia abajo**
 * en el árbol de nodos. Evita que los componentes hijos pasen por las costosas fases de *Layout*
 * (cálculo de tamaños) y *Draw* (pintado de píxeles) de forma innecesaria si el resultado final no varió.
 * - Su comportamiento reactivo es conceptualmente equivalente al operador ``distinctUntilChanged()``
 * de Kotlin Coroutines Flows, pero nativo y adaptado al sistema de capturas (*Snapshots*) de Compose.
 *
 * **Uso principal en UI**:
 * - Escenarios donde las condiciones de visibilidad o formato dependan de eventos periféricos continuos
 * (conmutar la visibilidad de un botón flotante según los píxeles de scroll de un ``LazyListState``, o
 * activar alertas visuales basadas en umbrales específicos de un ``Slider``).
 *
 * **Antipatrones a evitar (Optimización Prematura)**:
 * - Jamás debe utilizarse para cálculos elementales o de bajo costo derivados de estados que ya son de
 * baja frecuencia (como concatenar strings de un formulario o validar si dos campos de texto están vacíos).
 * El costo de inicializar la estructura, registrar observadores en el Snapshot y gestionar la caché de
 * ``derivedStateOf`` es sustancialmente más caro para la CPU que realizar la lógica limpia "a ras del suelo".
 */
@Composable
fun DerivedStateOfSample(modifier: Modifier = Modifier) {
    // Estado de alta frecuencia persistente: 'rememberSaveable' garantiza que el valor
    // sobreviva a la destrucción del nodo por scroll en contenedores indexados o rotación de pantalla.
    var sliderValue by rememberSaveable { mutableFloatStateOf(0f) }

    // --- OPTIMIZACIÓN DE ALTA FRECUENCIA 1 (Amortiguación Booleana) ---
    // Actúa como un buffer de notificaciones: evalúa el flujo de datos continuo de 'sliderValue',
    // pero solo notifica a los observadores externos cuando el resultado booleano final conmuta.
    val isThresholdExceeded by remember {
        derivedStateOf {
            sliderValue > 0.7f
        }
    }

    // --- OPTIMIZACIÓN DE ALTA FRECUENCIA 2 (Amortiguación de Texto) ---
    // Restringe el formateo de strings. Aunque la columna padre se evalúe por completo,
    // este bloque garantiza que el nodo 'Text' subyacente solo invalide sus fases de Layout
    // y Draw cuando la parte entera del porcentaje sufra un cambio real.
    val sliderPercentageText by remember {
        derivedStateOf {
            "Valor actual: ${(sliderValue * 100).toInt()}%"
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTitle("derivedStateOf (Optimización)")

        Text(
            text = "Arrastrá el slider y hacé scroll para ocultar el componente. Al regresar, " +
                    "el estado se conserva y los buffers (umbral y formateo del texto) recalculan la " +
                    "UI sin ráfagas gráficas.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // La asignación imperativa en 'onValueChange' invalida el scope de recomposición
        // de la función contenedora, forzando la evaluación estructural de la columna.
        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = sliderPercentageText,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Bloque condicional aislado: el árbol interno de la animación no se evalúa
        // ni se procesa mientras 'isThresholdExceeded' se mantenga estable en 'false'.
        AnimatedVisibility(visible = isThresholdExceeded) {
            Text(
                text = "⚠️ ADVERTENCIA: LÍMITE CRÍTICO SUPERADO (>70%)",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Red
            )
        }
    }
}

/**
 * - Proporciona un **``CoroutineScope`` asociado estrictamente al ciclo de vida del nodo de la
 * composición** donde es invocado.
 * - Su propósito fundamental es permitir el disparo **imperativo y manual** de tareas asíncronas o de
 * suspensión desde bloques de código tradicionales (como eventos de click o gestos táctiles), donde
 * las restricciones del compilador impiden el uso directo de efectos declarativos como ``LaunchedEffect``.
 * - **Mecanismo de Cancelación Segura**: Garantiza la contención de memoria al **cancelar de forma
 * automática e inmediata** todas las corrutinas que se estén ejecutando en su interior en el instante
 * exacto en que el componente sale del árbol de la UI (por ejemplo, debido a un reciclaje por scroll).
 *
 * **Casos de uso en UI**:
 * - Controlar el estado de componentes nativos de Jetpack Compose que exigen APIs de suspensión para
 * realizar transiciones animadas (abrir/cerrar un ``ModalBottomSheetState``, realizar scrolls automáticos
 * con ``LazyListState.animateScrollToItem``, o desplazar layouts frame a frame mediante ``Animatable``).
 *
 * **Antipatrones a evitar (Fugas lógicas de Arquitectura)**:
 * - Jamás debe utilizarse para orquestar llamadas a capas de lógica de negocio duraderas, persistencia de
 * datos (bases de datos locales) o peticiones de red (API REST). Dado que este scope es efímero y se destruye
 * con el ciclo de vida del nodo visual, cualquier proceso crítico de datos sería fulminado a mitad de camino
 * ante un simple evento gráfico (como ocultar el componente por scroll o rotar la pantalla), dejando la app
 * en un estado inconsistente. Esas operaciones corresponden estrictamente al ``viewModelScope``.
 */
@Composable
fun RememberCoroutineScopeSample(modifier: Modifier = Modifier) {
    // Instanciación del Scope de UI asociado al ciclo de vida de este nodo de la pantalla.
    // Proporciona el contexto necesario para lanzar corrutinas de forma manual desde callbacks imperativos.
    val scope = rememberCoroutineScope()

    // Estado de animación avanzado: encapsula un valor flotante reactivo que calcula
    // la transición de píxeles frame a frame de forma matemática acoplada a la tasa de refresco del dispositivo.
    val offsetX = remember { Animatable(0f) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTitle("rememberCoroutineScope (Gestos)")

        Text(
            text = "Interactúa con el bloque inferior:\n" +
                    "• Tap: Desplazamiento suave aleatorio.\n" +
                    "• Presión larga: Animación de retorno con rebote.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    // Transforma el valor flotante de la animación a píxeles enteros para desplazar la vista
                    .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                    .size(60.dp)
                    .background(Color(0xFF6200EE), RoundedCornerShape(12.dp))
                    // El modificador 'pointerInput' crea una corrutina latente encargada de escuchar eventos táctiles.
                    .pointerInput(Unit) {
                        // 'detectTapGestures' procesa los toques crudos y expone lambdas tradicionales de alto nivel.
                        // Al ser métodos de conveniencia estándar, rompen la suspensión interna del bloque 'pointerInput'.
                        detectTapGestures(
                            onTap = {
                                // Al no ser un bloque suspendido, se requiere 'scope.launch' para abrir una
                                // nueva corrutina dedicada a ejecutar la animación de forma asíncrona en el tiempo.
                                scope.launch {
                                    val targetRandom = Random.nextFloat() * 400f

                                    // 'animateTo' suspende estrictamente ESTA corrutina en cada pulso de sincronización
                                    // vertical (VSYNC) de la pantalla. No bloquea el hilo de renderizado de la UI.
                                    // CONCURRENCIA: Si hay una animación previa en curso, Animatable la cancela automáticamente.
                                    offsetX.animateTo(
                                        targetValue = targetRandom,
                                        animationSpec = spring(stiffness = Spring.StiffnessLow)
                                    )
                                }
                            },
                            onLongPress = {
                                // Retorno controlado usando físicas de rebote (Spring Spec) en una corrutina independiente.
                                scope.launch {
                                    offsetX.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        )
                                    )
                                }
                            }
                        )
                    }
            )
        }
    }
}

/**
 * - Facilita la **captura dinámica del valor o referencia más reciente** dentro de un ciclo de vida
 * asíncrono o de efecto, **impidiendo de forma absoluta que dicho efecto se reinicie o redespliegue**
 * ante mutaciones de ese valor.
 * - Mecánica Interna: Genera y gestiona un contenedor de estado estable (``State<T>``) acoplado al
 * *Snapshot System* de Compose, cuyo campo interno `.value` se actualiza de forma transparente en cada
 * frame de recomposición sin alterar la referencia del puntero original.
 *
 * **El Dilema de las Claves (Clave Fija vs Clave Variable)**:
 * - Este handler actúa como el antídoto directo contra el ***Stale State*** (**Estado Estancado**). Su uso
 * es **mandatorio** cuando se configura un efecto con una clave inmutable (como ``DisposableEffect(Unit)``
 * o ``LaunchedEffect(Unit)``) para proteger la continuidad de un proceso de hardware o de fondo, pero
 * dicho proceso necesita capturar de forma garantizada las actualizaciones de flags o configuraciones
 * que cambian dinámicamente en la UI.
 * - Si en su lugar se utilizara la variable mutada como clave del efecto (ej: ``DisposableEffect(currentValue)``),
 * el efecto se reiniciaría por completo en cada cambio de estado, forzando la ejecución de su bloque de
 * limpieza (``onDispose``). En componentes de infraestructura (motores de audio, buffers de streaming o
 * listeners de sockets), este reinicio constante destruiría la continuidad del proceso físico, provocando
 * cortes bruscos, micro-lags o reinicios innecesarios del flujo de datos.
 *
 * **Casos de uso**:
 * - Registro de callbacks unidireccionales en SDKs multimedia nativos (``Media3/ExoPlayer``), controladores
 * de sensores del sistema operativo (``SensorManager``) o pasarelas de red que deben permanecer activas en
 * segundo plano, pero necesitan reaccionar en tiempo real a flags modificadas por el usuario en la UI
 * (como un Switch de auto-avance o un modo de alta fidelidad).
 */
@Composable
fun RememberUpdatedStateSample(
    modifier: Modifier = Modifier,
    viewModel: SideEffectsViewModel
) {
    // 1. ELEVACIÓN DE ESTADO (State Hoisting)
    // Se recolecta el estado desde la cima del componente para mantener la pureza declarativa de la UI.
    val uiState by viewModel.uiState.collectAsState()

    // 2. EVITAR FUGAS GRÁFICAS (Estructura de Lambdas)
    // Se define la acción que debe ocurrir al finalizar la pista en la parte superior.
    // Esta lambda se recrea en cada ciclo de recomposición, capturando el estado más nuevo del ViewModel.
    val onTrackEndedAction: () -> Unit = {
        if (uiState.audioState.isAutoAdvanceEnabled) {
            viewModel.updateAudioLog("🎵 [Motor] Pista finalizada -> Saltando a la siguiente canción automáticamente.")
        } else {
            viewModel.updateAudioLog("⏹️ [Motor] Pista finalizada -> Deteniendo reproducción (Auto-avance apagado).")
        }
    }

    // Animación de físicas de UI para la barra de progreso reactiva
    val animatedProgress by animateFloatAsState(
        targetValue = uiState.audioState.currentProgress,
        label = "AudioProgress"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTitle("rememberUpdatedState (Multimedia)")

        Text(
            text = "Simulador de Motor de Audio Nativo",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Interfaz de control: Cambia la flag de configuración "en caliente"
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Auto-avanzar a la siguiente pista:")
            Switch(
                checked = uiState.audioState.isAutoAdvanceEnabled,
                onCheckedChange = { viewModel.toggleAutoAdvance(it) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = uiState.audioState.playbackLog,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        // 3. PUENTE DE INFRAESTRUCTURA
        // Se acopla el renderizado al ciclo de vida del hardware subyacente.
        AudioEngineBridge(
            onTrackFinished = onTrackEndedAction,
            onProgressChanged = { progress -> viewModel.updateAudioProgress(progress) },
            onResetRequired = { viewModel.resetAudioPlaybackState() }
        )
    }
}

/**
 * - Simplificación sintáctica (*sugar syntax*) que encapsula un ``remember { mutableStateOf(initial) }``
 * y un ``LaunchedEffect`` en un único bloque semántico especializado en la producción de estados.
 * - Actúa como un **conversor o puente asíncrono** diseñado para transformar flujos de datos externos
 * que no pertenecen al ecosistema nativo de Compose (como Flows de Kotlin, LiveData o callbacks crudos de
 * infraestructura del sistema operativo) en un ``State<T>`` reactivo que el *Snapshot System* de
 * Compose pueda leer.
 *
 * **Mecánica Interna y Relación con el Framework**:
 * - Constituye la base fundacional de la reactividad asíncrona en Jetpack Compose: funciones estándar del
 * SDK como ``collectAsState()`` y ``collectAsStateWithLifecycle()`` son, en su código fuente nativo, envolturas
 * (*wrappers*) directas que ejecutan ``produceState`` bajo el capot.
 * - Inicializa el estado con un valor por defecto (``initialValue``) y lanza una corrutina de larga duración
 * atada estrictamente al ciclo de vida del nodo en el árbol de la composición. Si el componente sale de la
 * pantalla (por ejemplo, mediante scroll en un contenedor indexado), la corrutina se cancela automáticamente,
 * lo que desencadena la ejecución de los bloques de liberación de recursos subyacentes (como ``awaitClose``
 * en un ``callbackFlow``).
 *
 * **Casos de uso**:
 * - Transformación y adaptación de emisores de infraestructura asíncronos (observadores de conectividad de red,
 * lecturas de sensores de hardware o flujos de eventos de sockets) que requieren inicialización por defecto y
 * recolección segura sin exponer la lógica de la tubería de datos directamente "a ras del suelo" en la interfaz
 * de usuario.
 */
@Composable
fun ProduceStateSample(
    modifier: Modifier = Modifier,
    viewModel: SideEffectsViewModel
) {
    // 1. DECLARACIÓN DEL PRODUCTOR DE ESTADO
    // 'produceState' inicializa un State<ConnectionState> con el valor 'Loading'.
    // Abre un scope de corrutina de larga duración atado al ciclo de vida del nodo (ProduceStateSample).
    // Clave 'Unit': La recolección del Flow se inicializa una sola vez al nacer el nodo.
    val networkState by produceState<ConnectionState>(
        initialValue = ConnectionState.Loading,
        key1 = Unit
    ) {
        // 2. TRANSFERENCIA DE DATOS AL SNAPSHOT SYSTEM DE COMPOSE
        // Se recolecta el flujo asíncrono y se utiliza la palabra clave 'value'
        // (provista por el scope de 'produceState') para empujar los datos hacia la UI.
        viewModel.networkStatusFlow.collectLatest { newState ->
            value = newState
        }
    }

    // 3. PROPIEDADES ESTÉTICAS DE LA CAPA DE PRESENTACIÓN
    val (statusText, backgroundColor) = when (networkState) {
        ConnectionState.Loading -> "Analizando adaptadores de red..." to Color(0xFFFFA000)
        ConnectionState.Available -> "Conexión en línea (Servicios activos)" to Color(0xFF2E7D32)
        ConnectionState.Unavailable -> "Sin acceso a la red (Modo desconectado)" to Color(0xFFC62828)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTitle("produceState (Puente de Infraestructura)")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(8.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}

/**
 * - Transforma **mutaciones del estado interno de Compose** (propiedades controladas por el Snapshot System,
 * ``derivedStateOf``, etc.) en un **Flow frío de Kotlin**, realizando el camino inverso al procesamiento ordinario
 * de entrada de datos.
 * - Monitorea de forma continua las lecturas de estado ejecutadas dentro de su lambda fundamental. Cada vez que
 * ocurre una mutación, el flujo evalúa el resultado y emite el nuevo valor por la tubería, aplicando de forma
 * nativa comparaciones por igualdad estructural para evitar redundancias.
 *
 * **Trampa del Puntero Estancado (*Stale Pointer* / Estancamiento de la Referencia en memoria)**:
 * - ATENCIÓN: La lambda de inspección debe evaluar rutas de estado dinámicas y actualizadas por la recomposición
 * (ej. acceder al path completo ``uiState.telemetryState.currentProgress`` a través de un delegado vivo).
 * - Si se extrae una propiedad de un ``data class`` en una variable local fuera de un efecto de clave fija
 * (como ``LaunchedEffect(Unit)``), la lambda capturará una copia de memoria fija e inmóvil del frame inicial. Esto
 * provocará un cortocircuito que impedirá al flujo detectar cambios reales en el Snapshot System.
 *
 * **Casos de uso y Operadores**:
 * - Habilita la exportación de interacciones visuales hacia subprocesos asíncronos de infraestructura, permitiendo
 * el acoplamiento de operadores avanzados de Corrutinas (como ``debounce`` para estabilización de pulsos,
 * ``distinctUntilChanged`` o ``map``).
 * - Es el estándar para el procesamiento eficiente de telemetría, auditoría de posiciones de scroll en contenedores
 * indexados o persistencia analítica de buffers en caliente sin saturar el hilo principal de la interfaz gráfica.
 */
@OptIn(FlowPreview::class)
@Composable
fun SnapshotFlowSample(
    modifier: Modifier = Modifier,
    viewModel: SideEffectsViewModel
) {
    // 1. ELEVACIÓN DE ESTADO (State Hoisting)
    // Recolección del estado de presentación desde el ViewModel unificado.
    val uiState by viewModel.uiState.collectAsState()

    // 2. EXTRACCIÓN Y FILTRADO ASÍNCRONO (Mecánica de snapshotFlow)
    // Ejecución de un bloque de larga duración con clave 'Unit' para impedir reinicios.
    LaunchedEffect(Unit) {
        // Limpiar el estado residual del ViewModel cada vez que el nodo revive en el scroll
        viewModel.resetTelemetryState()

        // 'snapshotFlow' inspecciona el Snapshot System de Compose en cada frame.
        // Ante cualquier mutación en 'state.currentProgress', lee el valor y lo emite al flujo.
        snapshotFlow { uiState.telemetryState.currentProgress }
            // Conversión a porcentaje entero
            .map { progress -> (progress * 100).toInt() }
            // Filtrado de emisiones consecutivas idénticas
            .distinctUntilChanged()
            // Suspensión por 3 segundos de inactividad antes de emitir (estabilización)
            .debounce(3000)
            .collect { stabilizedProgress ->
                // Ejecución del efecto colateral de infraestructura fuera del hilo principal de UI
                viewModel.sendAnalyticsReport(stabilizedProgress)
            }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTitle("snapshotFlow (Telemetría de Estado)")

        Text(
            text = "Debounce de 3 segundos.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Progreso actual: ${(uiState.telemetryState.currentProgress * 100).toInt()}%",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(
            progress = { uiState.telemetryState.currentProgress },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Disparador manual para emular incrementos de progreso en la UI
        Button(
            onClick = {
                if (uiState.telemetryState.currentProgress < 1.0f) {
                    viewModel.updateTelemetryProgress(uiState.telemetryState.currentProgress + 0.1f)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simular Incremento de Carga (+10%)")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = uiState.telemetryState.telemetryLog,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}
