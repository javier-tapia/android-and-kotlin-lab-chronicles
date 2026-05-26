package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.animations

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase

/**
 * **Temas**:
 * - ``animate*AsState``: Animaciones automáticas basadas en cambios de estado.
 * - ``AnimatedVisibility``: Entrada y salida de componentes del árbol de composición.
 * - ``AnimatedContent`` y `animateContentSize()`: Transiciones suaves
 * entre diferentes estados de contenido.
 * - ``Crossfade``: Transición de desvanecimiento entre composables.
 * - ``InfiniteTransition``: Animaciones en bucle (ej. rotaciones, pulsaciones).
 * - ``AnimationSpecs``: Configuración de curvas (``tween``, ``spring``, ``easing``).
 *
 * **El porqué del signo negativo (``-it``)**:
 *
 * En los sistemas de coordenadas de pantallas (Android, iOS, Web), el punto ``(0,0)`` es la
 * **esquina superior izquierda**:
 * - El eje **Y crece hacia abajo y decrece (valores negativos) hacia arriba**, fuera de la
 * pantalla.
 * - El eje **X crece hacia la derecha y decrece (valores negativos) hacia la izquierda**, fuera de
 * la pantalla.
 *
 * Utilizar ``{ -it }`` implica **desplazar el componente hacia el lado negativo del eje**
 * (arriba para Y, izquierda para X) una distancia equivalente a su propia altura o ancho.
 * - En un ``enter``, esto define el **punto de partida** (el componente viene desde afuera).
 * - En un ``exit``, esto define el **punto de destino** (el componente se marcha hacia afuera).
 */
@Composable
fun AnimationsLab() {
    SamplesShowcase(
        { AnimateXAsStateSample() },
        { AnimatedVisibilitySample() },
        { ContentAnimationsSample() },
        { CrossfadeSample() },
        { ShimmerSample() },
        { InfiniteTransitionSample() },
    )
}

/**
 * - ``animate*AsState``: Es la familia de APIs de animación más simple de Compose; transforma un
 * valor estático en un ``State<T>`` que converge suave y automáticamente hacia el nuevo valor de
 * destino cuando este cambia.
 * - ``targetValue``: El valor final que la animación debe alcanzar; el motor de Compose detecta si
 * este parámetro cambia respecto al frame anterior y dispara la transición de inmediato.
 * - ``animationSpec``: Define la física o la curva de tiempo que gobernará la transición (como
 * rebotes, aceleraciones o duraciones fijas).
 * - ``finishedListener``: Una lambda de retorno que se ejecuta una sola vez cuando la animación ha
 * completado su recorrido con éxito, ideal para encadenar eventos o limpiar estados.
 * - ``label``: Un identificador en texto plano que no afecta al rendimiento, utilizado exclusivamente
 * para que las herramientas de inspección (como el Animation Preview de Android Studio) puedan
 * etiquetar y filtrar la animación.
 *
 * **Sobre ``Modifier.offset { ... }``**: Es una variante optimizada del modificador de desplazamiento
 * que recibe una lambda, permitiendo que los cambios frecuentes de posición se procesen directamente
 * en las fases de *Layout* y *Draw* (Dibujo) sin invalidar la composición (evitando recomponer en cada frame).
 *
 * **Comportamiento de bajo nivel y renderizado**: La variante estática (``Modifier.offset(x, y)``),
 * tiene un menor rendimiento (fuerza recomposición en cada frame), pero respeta el flujo de la
 * pantalla y empuja orgánicamente el resto de los elementos. En cambio, al usar la variante de lambda
 * (``Modifier.offset { ... }``), Compose promueve el componente a su propia capa gráfica
 * independiente (``GraphicsLayer``). Esto optimiza el rendimiento, pero tiene un impacto geométrico:
 * el desplazamiento es puramente visual y **no altera el espacio físico que el componente ocupa
 * originalmente en el Layout**.
 * - Si el componente se desplaza más allá de los límites de su contenedor padre, lo "pisará" (efecto
 * solapamiento en el orden Z) por estar en una capa superior.
 * - Si se le aplica ``.clipToBounds()`` al contenedor para evitar que pise a otros, el componente se
 * mostrará **cortado**, ya que el motor de renderizado eliminará los píxeles que excedan las fronteras
 * calculadas originalmente en la fase de Layout.
 */
@Composable
fun AnimateXAsStateSample() {
    var isSelected by rememberSaveable { mutableStateOf(false) }

    /**
     * Estado auxiliar para demostrar el uso de ``finishedListener``
     */
    var animationStatus by rememberSaveable { mutableStateOf("IDLE") }

    // 1. Las animaciones de Color y Alpha usan TWEEN (Control de tiempo y aceleración lineal)
    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) Color.Red else Color.Blue,
        animationSpec = getStandardTween(),
        // Acá se puede acceder al estado final luego de la animación
        finishedListener = { finalColor ->
            animationStatus = "Nivel de rojo: ${finalColor.red.toInt() * 100}%"
        },
        label = "Red and Blue animation"
    )

    val animatedFloat by animateFloatAsState(
        targetValue = if (isSelected) 0.3f else 1f,
        animationSpec = getStandardTween(),
        label = "Alpha animation"
    )

    // 2. Las animaciones de Tamaño y Desplazamiento (Offset) usan SPRING (Física elástica y orgánica)
    val animatedSize by animateDpAsState(
        targetValue = if (isSelected) 200.dp else 150.dp,
        animationSpec = getStandardSpring(),
        label = "Size animation"
    )

    val animatedOffset by animateOffsetAsState(
        targetValue = if (isSelected) Offset(0f, 100f) else Offset(0f, 0f),
        animationSpec = getStandardSpring(),
        label = "Offset animation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
        // Descomentar si se quiere evitar que el desplazamiento del componente se solape con el divider
//            .clipToBounds(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = { isSelected = !isSelected }
        ) {
            Text("Animar box")
        }
        Spacer(Modifier.height(8.dp))

        // Se muestra el impacto del finishedListener en la pantalla
        Text(animationStatus)

        Text("Background alpha: %.2f".format(animatedFloat))
        Spacer(Modifier.height(32.dp))

        Box(
            Modifier
                // OPTIMIZACIÓN: Usar la variante de la lambda para que el cambio de coordenadas
                // se ejecute en la fase de Layout/Draw, evitando recomponer el Box en cada frame.
//                .offset(animatedOffset.x.dp, animatedOffset.y.dp)
                .offset {
                    IntOffset(
                        x = animatedOffset.x.dp.roundToPx(),
                        y = animatedOffset.y.dp.roundToPx()
                    )
                }
                .size(animatedSize)
                .background(animatedColor.copy(alpha = animatedFloat))
        )
    }
}

/**
 * - ``AnimatedVisibility``: Composable contenedor que gestiona de forma automática la aparición y
 * desaparición de sus componentes hijos en el árbol de composición, aplicando transiciones
 * visuales customizadas.
 * - ``visible``: El estado booleano que actúa como interruptor principal; determina si el contenido
 * interno debe ser introducido (fase de entrada) o removido (fase de salida) de la UI.
 * - ``enter`` (por defecto ``fadeIn + expandIn``): Define la combinación de efectos (como
 * desvanecimiento, escalado o deslizamiento) que se ejecutarán en paralelo cuando el componente
 * sea inyectado en la pantalla. Cuando ``visible`` pasa a ``true``, el ``content`` **se inyecta**
 * en el árbol de composición y comienza a renderizarse.
 * - ``exit`` (por defecto ``fadeOut + shrinkOut``): Define la combinación de efectos de salida que
 * se aplicarán al revés justo antes de que el componente sea completamente desmontado de la memoria.
 * Cuando ``visible`` pasa a ``false``, la librería **retiene** el Composable temporalmente en el
 * árbol de composición únicamente mientras dure la animación de salida. En cuanto el ``content`` se
 * encoge por completo, es **destruido y eliminado definitivamente** de la memoria y del árbol de renderizado.
 * - ``expandIn()`` / ``shrinkOut()``: Efectos geométricos específicos que animan los límites del
 * contenedor; ``expandIn`` revela el contenido expandiendo el tamaño desde una esquina (o el centro)
 * hacia afuera, mientras que ``shrinkOut`` lo encoge hacia adentro hasta desaparecer.
 */
@Composable
fun AnimatedVisibilitySample(modifier: Modifier = Modifier) {
    // Estado para determinar qué tipo de animación mostrar según lo que elija el usuario
    var currentAnimationType by rememberSaveable {
        mutableStateOf(AnimationType.EXPAND_SHRINK)
    }

    // Estado para determinar la visibilidad del contenido de AnimatedVisibility
    val visibilityState = remember {
        MutableTransitionState(initialState = true) // Arranca visible
    }

    // Si el estado actual es distinto al objetivo, es porque la animación aún no terminó.
    // Esta lógica sirve para bloquear los botones y se calcula AFUERA, de forma reactiva y pura.
    val isAnimating = visibilityState.currentState != visibilityState.targetState

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(50.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    // Se setea el tipo de animación a mostrar
                    currentAnimationType = AnimationType.EXPAND_SHRINK
                    // Se cambia el targetState de forma segura
                    visibilityState.targetState = !visibilityState.targetState
                },
                // El botón se deshabilita automáticamente basándose en el estado puro
                enabled = !isAnimating
            ) {
                Text("Expandir / Contraer")
            }

            Button(
                onClick = {
                    // Se setea el tipo de animación a mostrar
                    currentAnimationType = AnimationType.SLIDE_VERTICAL
                    // Se cambia el targetState de forma segura
                    visibilityState.targetState = !visibilityState.targetState
                },
                // El botón se deshabilita automáticamente basándose en el estado puro
                enabled = !isAnimating
            ) {
                Text("Mostrar / Ocultar (vertical)")
            }
        }

        Spacer(Modifier.height(50.dp))

        AnimatedVisibility(
            // Usamos 'visibleState' en lugar de 'visible'
            visibleState = visibilityState,
            // El tipo de animación se evalúa dinámicamente
            enter = if (currentAnimationType == AnimationType.EXPAND_SHRINK) {
                expandIn(
                    // Fuerza a que expanda desde el centro de forma simétrica
                    expandFrom = Alignment.Center,
                    // Las físicas de resorte ayudan a suavizar interrupciones
                    animationSpec = getStandardSpring()
                ) + fadeIn(animationSpec = getStandardTween())
            } else {
                // Combina un deslizamiento vertical + desvanecimiento usando físicas de resorte
                slideInVertically(
                    animationSpec = getStandardSpring()
                ) { -it } + fadeIn(
                    animationSpec = getStandardSpring()
                )
            },
            exit = if (currentAnimationType == AnimationType.EXPAND_SHRINK) {
                shrinkOut(
                    // Fuerza a que se encoja hacia el centro
                    shrinkTowards = Alignment.Center,
                    animationSpec = getStandardSpring()
                ) + fadeOut(animationSpec = getStandardTween())
            } else {
                slideOutVertically(
                    animationSpec = getStandardTween()
                ) { -it } + fadeOut(
                    animationSpec = getStandardTween()
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color.Red)
            )
        }
    }
}

/**
 * - ``AnimatedContent``: Un contenedor que gestiona la transición visual (por defecto, un
 * *crossfade*) cuando el estado de su contenido (``targetState``) cambia, permitiendo alternar
 * entre estructuras de UI completamente distintas de forma fluida.
 * - ``animateContentSize()``: Un modificador que detecta automáticamente cualquier
 * cambio en las dimensiones de su *layout* (causado por la alteración de su contenido) y anima el
 * cambio geométrico del propio *layout* de forma suave, sin necesidad de animar los valores de tamaño
 * manualmente.
 *
 * **Diferencia de enfoque**: Mientras que ``AnimatedContent`` se encarga de **cómo entran y salen
 * los componentes internos** de la pantalla, ``.animateContentSize()`` se encarga de **cómo el
 * marco exterior se estira o encoge** para contenerlos. Al usarlos juntos, se crea la ilusión de
 * que el contenedor es un ente orgánico que se adapta físicamente a lo que lleva dentro.
 */
@Composable
fun ContentAnimationsSample() {
    var step by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        Button(onClick = {
            // Cicla entre 0, 1, 2 y 3 para probar todos los estados
            step = (step + 1) % 4
        }) {
            Text("Siguiente Estado (Actual: $step)")
        }

        Spacer(Modifier.height(32.dp))

        // Tarjeta contenedora que usa 'animateContentSize()'
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color.Red, shape = RoundedCornerShape(16.dp))
                // MODIFICADOR: Modifica la geometría de la tarjeta automáticamente
                // cuando el contenido interno cambia de tamaño.
                .animateContentSize(animationSpec = getStandardSpring())
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // CONTENEDOR: Gestiona la transición visual (Fade/Slide) entre Composables
            AnimatedContent(
                targetState = step,
                label = "UnifiedContentTransition",
                transitionSpec = {
                    // Combina los efectos de entrada (In) con las físicas de resorte
                    val enterTransition =
                        slideInHorizontally(
                            animationSpec = getStandardSpring()
                        ) { it } + fadeIn(animationSpec = getStandardSpring())

                    // Combina los efectos de salida (Out) con las físicas de resorte
                    val exitTransition =
                        slideOutHorizontally(
                            animationSpec = getStandardSpring()
                        ) { -it } + fadeOut(animationSpec = getStandardSpring())

                    // Une ambas transiciones usando 'togetherWith'
                    enterTransition togetherWith exitTransition
                },
            ) { currentStep ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Fase: $currentStep",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )

                    // Cada rama del 'when' define un árbol de nodos de UI independiente.
                    // Al cambiar el estado, 'AnimatedContent' destruye el árbol del estado anterior
                    // e infla el nuevo, calculando la diferencia de tamaño para el Layout.
                    when (currentStep) {
                        // Estado Chico
                        0 -> {
                            Text("Un texto corto y conciso.", color = Color.White)
                        }

                        // Estado Mediano (Introduce nuevos componentes)
                        1 -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Home,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Spacer(Modifier.width(8.dp))
                                Button(onClick = {}) { Text("Botón de Acción") }
                            }
                        }

                        // Estado Grande (Rompe el layout estirándolo mucho)
                        2 -> {
                            Box(
                                Modifier
                                    .size(120.dp)
                                    .background(
                                        Color.White.copy(alpha = 0.3f),
                                        RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Caja Grande", color = Color.White)
                            }
                        }

                        // Estado Extra Grande (Mucho texto)
                        else -> {
                            Text(
                                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                        "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                                        "Ut enim ad minim veniam, quis nostrud exercitation.",
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * - `Crossfade`: Un contenedor intermedio diseñado exclusivamente para animar la transición
 * de opacidad (desvanecimiento cruzado) entre dos piezas de contenido basándose en un estado.
 * A diferencia de `AnimatedContent`, no gestiona transformaciones geométricas complejas de tamaño
 * ni desplazamientos; asume que el contenido comparte el mismo espacio conceptual.
 * - `animationSpec`: Permite alterar la curva de tiempo del desvanecimiento; al ser una transición
 * basada puramente en opacidad (Alpha), se beneficia enormemente de curvas basadas en tiempo
 * (`tween`) o suavizados (`easing`) en lugar de físicas de rebote (`spring`).
 */
@Composable
fun CrossfadeSample() {
    var layoutState by rememberSaveable { mutableStateOf(ToggleLayoutState.COMPACT) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        Button(onClick = {
            layoutState = if (layoutState == ToggleLayoutState.COMPACT) {
                ToggleLayoutState.DETAILED
            } else {
                ToggleLayoutState.COMPACT
            }
        }) {
            Text("Alternar Vista (Crossfade)")
        }

        Spacer(Modifier.height(32.dp))

        // Contenedor que aplica el fundido entre las dos estructuras
        Crossfade(
            targetState = layoutState,
            // Ajustado para un desvanecimiento óptimo de tiempo
            animationSpec = getStandardTween(),
            label = "LayoutCrossfade"
        ) { state ->
            // El bloque inyecta el estado actual para decidir qué renderizar
            when (state) {
                ToggleLayoutState.COMPACT -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.85f)
                            .background(
                                color = Color.DarkGray,
                                shape = RoundedCornerShape(size = 12.dp)
                            )
                            .padding(all = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBox,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(Modifier.width(16.dp))
                        Text("Vista Compacta (Usuario)", color = Color.White)
                    }
                }

                ToggleLayoutState.DETAILED -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.85f)
                            .background(
                                color = Color.Blue,
                                shape = RoundedCornerShape(size = 12.dp)
                            )
                            .padding(all = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBox,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Javi",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Software Developer - Android & Kotlin",
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Usa la función de extensión *custom* [Modifier.shimmer] para crear el efecto propuesto sobre el
 * *background* animado
 */
@Composable
fun ShimmerSample() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Cargando perfil...", style = MaterialTheme.typography.titleMedium)

        Row(verticalAlignment = Alignment.CenterVertically) {
            // El círculo del avatar clonado en modo esqueleto
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .shimmer() // <-- ¡Magia infinita!
            )

            Spacer(Modifier.width(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Barra de esqueleto para el título
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmer()
                )
                // Barra de esqueleto para el subtítulo
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmer()
                )
            }
        }
    }
}

/**
 * - ``rememberInfiniteTransition()``: Crea y recuerda una transición de ciclo infinito en el árbol
 * de composición. Actúa como un motor centralizado que coordina y ejecuta en paralelo múltiples
 * animaciones secundarias que no dependen de un cambio de estado manual para activarse.
 * - ``animateColor``: API especializada que calcula la interpolación cromática entre dos colores,
 * encargándose de la transición suave de los canales de color (R, G, B, A) frame a frame.
 * - ``animateValue``: La API genérica y de bajo nivel para animar cualquier tipo de dato personalizado
 * que no posea una función de extensión directa (como Dp, Rect, etc.).
 * - ``infiniteRepeatable``: Especificación de animación exclusiva para bucles continuos; envuelve a una
 * curva base (como ``tween``) y define las reglas de repetición del ciclo.
 * - ``RepeatMode.Reverse``: Indica que al llegar al ``targetValue``, la animación invierte su marcha
 * hacia el ``initialValue`` de forma fluida (efecto vaivén). Su contraparte, ``RepeatMode.Restart``,
 * saltaría bruscamente al inicio en cada ciclo.
 * - ``Dp.VectorConverter``: El traductor matemático (conversor de vectores) provisto por Compose que le
 * enseña al motor de animación cómo desarmar una unidad de medida ``Dp`` en valores flotantes bidimensionales
 * para poder calcular su progresión elástica o temporal.
 */
@Composable
fun InfiniteTransitionSample() {
    // 1. Se instancia el motor del bucle continuo
    val infiniteTransition = rememberInfiniteTransition(label = "LoopMaster")

    // 2. Animación de color continua (Rojo <-> Amarillo)
    val animatedColor by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Yellow,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ColorLoop"
    )

    // 3. Animación de tamaño continua (Efecto pulsación sutil)
    val animatedSize by infiniteTransition.animateValue(
        initialValue = 100.dp,
        targetValue = 150.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "SizeLoop"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 4. Se combinan ambas animaciones continuas en un único Box orgánico
        Box(
            modifier = Modifier
                .size(animatedSize)
                .background(animatedColor, shape = RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "LOOP",
                color = Color.Black,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
