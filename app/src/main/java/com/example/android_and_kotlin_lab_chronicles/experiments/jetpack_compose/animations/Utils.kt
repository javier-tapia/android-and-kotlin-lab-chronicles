package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

enum class ToggleLayoutState {
    COMPACT,
    DETAILED
}

enum class AnimationType {
    EXPAND_SHRINK,
    SLIDE_VERTICAL
}

// ==========================================
// CONFIGURACIONES DE ANIMACIÓN REUTILIZABLES
// ==========================================

/**
 * Función genérica ``<T>`` para que la misma curva sirva tanto para Color, Dp, Float, etc.
 * Devuelve un ``tween`` con:
 *  - Duración de 1 segundo (1000 ms);
 *  - Retardo de 300 ms antes de comenzar;
 *  - Y una curva de desaceleración (acelera rápido y se desacelera al final).
 *
 *  @return [TweenSpec] basada en tiempo estricto (Desaceleración)
 */
fun <T> getStandardTween(): TweenSpec<T> = tween(
    durationMillis = 1000,
    delayMillis = 300,
    easing = LinearOutSlowInEasing
)

/**
 * Función genérica `<T>` para que la misma física sirva para cualquier tipo de dato geométrico o visual.
 *
 * Devuelve un `spring` (resorte) que ignora el concepto de tiempo fijo y se rige por leyes físicas:
 * - ``dampingRatio`` ([Spring.DampingRatioMediumBouncy]): Define la **elasticidad o nivel de rebote**.
 * Al ser "Medium Bouncy", introduce una oscilación moderada y perceptible antes de estabilizarse.
 * - ``stiffness`` ([Spring.StiffnessLow]): Define la **rigidez** del resorte. Al ser "Low", el resorte
 * es más blando y se mueve más lentamente hacia el destino, estirando la duración de la animación.
 *
 * @return [SpringSpec] basada en física de resorte (Efecto elástico y orgánico).
 */
fun <T> getStandardSpring(): SpringSpec<T> = spring(
    dampingRatio = Spring.DampingRatioMediumBouncy,
    stiffness = Spring.StiffnessLow
)

/**
 * - ``composed { }``: Función de orden superior utilizada para crear modificadores personalizados
 * con estado (*stateful*). Permite que cada componente al que se le aplica el modificador instancie,
 * recuerde y encapsule sus propios efectos de ciclo de vida y variables de estado (como el motor de
 * animación infinito del *shimmer*) de forma totalmente independiente del resto de la UI. Dicho de
 * otra forma: transforma un modificador estático en una fábrica dinámica de código Composable que
 * se ejecuta **bajo demanda** (*Just-in-time composition*), solo cuando la UI realmente lo necesita.
 * - ``rememberInfiniteTransition``: Motor continuo que gobierna el ciclo infinito del brillo.
 * - ``animateFloat``: Anima un valor numérico puro que se usará como coordenada del
 * *Offset* (Desplazamiento).
 * - ``Brush.linearGradient``: Crea el gradiente con el destello en el centro. El desplazamiento del
 * ``start`` y ``end`` mediante el valor animado es lo que genera la ilusión de movimiento.
 */
fun Modifier.shimmer(): Modifier = composed {
    // 1. Se crea el motor infinito
    val transition = rememberInfiniteTransition(label = "ShimmerMaster")

    // 2. Se anima la coordenada X del gradiente (de 0 a 1000 píxeles de desplazamiento)
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            // Se usa LinearEasing para que el brillo se mueva a velocidad constante
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            // Reinicia desde el principio al terminar
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerTranslate"
    )

    // 3. Se define la paleta de colores del esqueleto (Gris claro -> Brillo -> Gris claro)
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f), // El destello brillante (más transparente)
        Color.LightGray.copy(alpha = 0.6f),
    )

    // 4. Se crea el gradiente lineal dinámico usando el valor animado
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation - 300f, y = translateAnimation - 300f),
        end = Offset(x = translateAnimation, y = translateAnimation)
    )

    // 5. Se retorna el modificador aplicando el fondo con el gradiente animado
    background(brush)
}
