package com.example.android_and_kotlin_lab_chronicles.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Define las **formas globales** que seguirá la app: radios, esquinas redondeadas y estilos de
 * contenedores.
 *
 * Material 3 usa estas formas en sus componentes:
 * - ``extraSmall`` / ``small``: Pistas de ``TextField``, ``Tooltips`` y menús desplegables.
 * - ``medium``: Tarjetas (``Cards``), diálogos de alerta pequeños.
 * - ``large`` / ``extraLarge``: Diálogos flotantes (``AlertDialog``) y hojas de fondo (``BottomSheet``).
 *
 * @see LabTheme
 * @see Typography
 * @see tirra
 */
val shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(48.dp)
)
