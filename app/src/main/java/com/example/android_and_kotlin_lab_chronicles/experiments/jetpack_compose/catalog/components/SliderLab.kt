package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase
import kotlin.math.cos
import kotlin.math.sin

/**
 * Experimentos con Sliders continuos y discretos para selección de rangos.
 * También está disponible un tipo especial llamado ``RangeSlider``, que amplía la funcionalidad
 * del ``Slider`` utilizando los mismos conceptos, pero permite al usuario seleccionar dos valores.
 *
 * **1. Sliders Continuos**
 *
 * Son aquellos que permiten seleccionar **cualquier valor** dentro de un rango
 * definido (ej. de 0.0 a 100.0).
 * El movimiento del indicador es fluido y no se detiene en puntos fijos.
 * - **Uso ideal**: Ajuste de volumen, brillo de pantalla o cualquier valor donde la precisión
 * absoluta no sea crítica para el usuario.
 * - **Implementación en Compose**: Se logra usando el parámetro ``valueRange`` sin definir
 * el parámetro ``steps``.
 *
 * **2. Sliders Discretos**
 *
 * Son aquellos que solo permiten seleccionar **valores específicos** predefinidos.
 * El indicador "salta" de un punto a otro.
 * - **Uso ideal**: Ajustes que tienen valores enteros o pasos lógicos (ej. elegir el número
 * de estrellas de una calificación de 1 a 5, o tallas de ropa S, M, L, XL).
 * - **Visualización**: Suelen mostrar pequeños puntos (*tick marks*) a lo largo de la línea
 * para indicar dónde puede detenerse el usuario.
 * - **Implementación** en Compose: Se logra definiendo el parámetro ``steps``. Por ejemplo,
 * si el rango es de 0 a 10 y se pone ``steps = 9``, se crean 10 posiciones fijas.
 */
@Composable
fun SliderLab() {
    SamplesShowcase(
        { ContinuousSliderSample() },
        { DiscreteRangeSliderSample() },
        { DiscreteSliderWithStateSample() },
    )
}

@Composable
fun ContinuousSliderSample() {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var completeValue by remember { mutableStateOf("") }
    Slider(
        value = sliderPosition,
        onValueChange = { sliderPosition = it },
        onValueChangeFinished = { completeValue = sliderPosition.toString() },
        valueRange = 0f..10f,
        enabled = true
    )
    Text(text = completeValue)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscreteRangeSliderSample() {
    var currentRange by remember { mutableStateOf(0f..10f) }

    RangeSlider(
        value = currentRange,
        onValueChange = { currentRange = it },
        valueRange = 0f..10f,
        steps = 9
    )

    Text(text = "Valor inferior ${currentRange.start}")
    Text(text = "Valor Superior ${currentRange.endInclusive}")
}

/**
 * 1. Crear el ``MutableInteractionSource``
 *
 * 2. Crear el ``SliderState``
 *
 * 3. Al ``Slider``, se le debe pasar el ``SliderState`` y el ``MutableInteractionSource`` creados antes
 *
 * 4. Al ``Thumb`` se le debe pasar el ``MutableInteractionSource``
 *
 * 5. Al ``Track`` se le debe pasar el ``SliderState``
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscreteSliderWithStateSample() {
    var example by rememberSaveable { mutableStateOf(":(") }

    // InteractionSource gestiona estados como 'pressed' o 'dragged'
    val interactionSource = remember { MutableInteractionSource() }

    // Contiene los puntos de parada (steps) y el progreso actual
    val state = remember {
        SliderState(
            value = 5f,
            valueRange = 0f..10f,
            steps = 9,
            onValueChangeFinished = { example = ":)" }
        )
    }

    Slider(
        state = state,
        interactionSource = interactionSource,
        thumb = {
//            SliderDefaultThumb(interactionSource)

            // Extraemos los estados de la interacción
            val isPressed by interactionSource.collectIsPressedAsState()
            val isDragged by interactionSource.collectIsDraggedAsState()

            // Usamos derivedStateOf para que Compose agrupe estos estados de forma eficiente
            val isInteracting by remember {
                derivedStateOf { isPressed || isDragged }
            }

            // Creamos animaciones suaves para los cambios
            val animatedSize by animateDpAsState(
                targetValue = if (isInteracting) 40.dp else 30.dp,
                label = "SizeAnimation"
            )
            val color by animateColorAsState(
                targetValue = if (isInteracting) Color.Yellow else Color.Transparent,
                label = "ColorAnimation"
            )
            val rotation by animateFloatAsState(
                targetValue = if (isInteracting) 360f else 0f,
                label = "RotationAnimation"
            )

//            CustomBoxThumb(animatedSize, color, isInteracting)

            CustomCanvasThumb(animatedSize, color, isInteracting, rotation)
        },
        track = {
            // Cálculo de la fracción (proporción 0.0 a 1.0)
            val fraction by remember {
                derivedStateOf {
                    (state.value - state.valueRange.start) /
                            (state.valueRange.endInclusive - state.valueRange.start)
                }
            }

//            SliderDefaultTrack(state)

//            CustomBoxTrack(fraction)

            CustomCanvasTrack(fraction, state)
        }
    )
    Text(example)
}

/**
 * Implementación por defecto usando SliderDefaults.Thumb
 */
@Composable
private fun SliderDefaultThumb(interactionSource: MutableInteractionSource) {
    SliderDefaults.Thumb(
        interactionSource = interactionSource,
        colors = SliderDefaults.colors(
            thumbColor = Color.Magenta,
            activeTrackColor = Color.Cyan
        )
    )
}

/**
 * Implementación customizada del thumb usando Box
 */
@Composable
private fun CustomBoxThumb(
    size: Dp,
    color: Color,
    isInteracting: Boolean
) {
    Box(
        modifier = Modifier
            // Usamos el tamaño animado
            .size(size)
            // Usamos el color animado
            .background(color, RoundedCornerShape(4.dp))
            // Sombra opcional
            .shadow(
                if (isInteracting) 8.dp else 0.dp,
                RoundedCornerShape(4.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(size / 2) // El icono también escala
        )
    }
}

/**
 * Implementación customizada del thumb usando Canvas
 */
@Composable
private fun CustomCanvasThumb(
    animatedSize: Dp,
    color: Color,
    isInteracting: Boolean,
    rotation: Float
) {
    Canvas(modifier = Modifier.size(animatedSize)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2

        // 1. Dibujamos el fondo cuadrado redondeado (reemplaza al background del Box)
        drawRoundRect(
            color = color,
            size = size,
            cornerRadius = CornerRadius(4.dp.toPx()),
            alpha = if (isInteracting) 1f else 0.8f
        )

        // 2. Dibujamos una estrella manual usando un Path (reemplaza al Icon)
        withTransform({
            rotate(rotation, center)
        }) {
            val starPath = Path().apply {
                val innerRadius = radius / 2.5f
                val outerRadius = radius / 1.5f
                val numPoints = 5
                val angleIncrement = (2 * Math.PI / numPoints).toFloat()

                for (i in 0 until numPoints * 2) {
                    val r = if (i % 2 == 0) outerRadius else innerRadius
                    val angle = i * angleIncrement / 2 - Math.PI.toFloat() / 2
                    val x = center.x + r * cos(angle.toDouble()).toFloat()
                    val y = center.y + r * sin(angle.toDouble()).toFloat()
                    if (i == 0) moveTo(x, y) else lineTo(x, y)
                }
                close()
            }
            drawPath(path = starPath, color = Color.Black)
        }
    }
}

/**
 * Implementación por defecto usando SliderDefaults.Track
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SliderDefaultTrack(state: SliderState) {
    SliderDefaults.Track(
        sliderState = state,
        colors = SliderDefaults.colors()
    )
}

/**
 * Implementación customizada del track usando Box
 */
@Composable
private fun CustomBoxTrack(fraction: Float) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(12.dp)
            .background(Color.LightGray, CircleShape)
    ) {
        Box(
            Modifier
                .fillMaxWidth(fraction)
                .fillMaxHeight()
                .background(
                    brush = Brush.horizontalGradient(listOf(Color.Red, Color.Yellow)),
                    shape = CircleShape
                )
        )
    }
}

/**
 * Implementación customizada del track usando Canvas
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CustomCanvasTrack(fraction: Float, state: SliderState) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
    ) {
        val width = size.width
        val height = size.height
        // Simplificado: inicio es 0, así que es width * fraction
        val thumbPosition = width * fraction

        // 1. Dibujamos la parte inactiva (fondo)
        drawLine(
            color = Color.Gray.copy(alpha = 0.3f),
            start = Offset(0f, height / 2),
            end = Offset(width, height / 2),
            strokeWidth = height / 2,
            cap = StrokeCap.Round
        )

        // 2. Dibujamos la parte activa (progreso)
        drawLine(
            brush = Brush.horizontalGradient(listOf(Color.Cyan, Color.Blue)),
            start = Offset(0f, height / 2),
            end = Offset(thumbPosition, height / 2),
            strokeWidth = height / 2,
            cap = StrokeCap.Round
        )

        // 3. Ticks (puntos) como una "regla" de edición de video
        val tickCount = state.steps + 2
        repeat(tickCount) { index ->
            val tickFraction = index.toFloat() / (tickCount - 1)
            val tickX = width * tickFraction

            val isPassed = tickX <= thumbPosition

            // En lugar de círculos, dibujamos pequeñas muescas verticales
            drawLine(
                color = if (isPassed) Color.White else Color.Gray,
                start = Offset(tickX, (height / 2) - 5.dp.toPx()),
                end = Offset(tickX, (height / 2) + 5.dp.toPx()),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
}
