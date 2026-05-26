package com.example.android_and_kotlin_lab_chronicles.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val LightColorScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

/**
 * Composable responsable de:
 * 1. Elegir entre *light* y *dark theme* (según configuración del sistema)
 * 2. Proveer: ``colorScheme``, ``typography`` y ``shapes``
 * 3. Aplicar el tema a toda la UI (`content`)
 *
 * #### **¿Qué hace `dynamicColor`?**
 *
 * Es la flag que activa *Material You* (introducido en Android 12, API 31+).
 * Cuando está en ``true``, el framework **ignora por completo las paletas manuales**
 * (``DarkColorScheme`` y ``LightColorScheme``). En su lugar, el sistema operativo extrae los
 * colores dominantes del fondo de pantalla del dispositivo del usuario (mediante el motor *Monet*),
 * genera una paleta armoniosa y simétrica de 30 colores accesibles sobre la marcha, y se la inyecta
 * al contexto a través de ``dynamicDarkColorScheme(context)``.
 * Acá lo dejo en ``false`` para que el fondo de pantalla del teléfono no destruya la identidad
 * visual del laboratorio.
 *
 * #### **Sobre los colores y sus variantes (Estructura de Roles de Material 3)**
 *
 * La convención de nombres de Material 3 sigue una lógica lingüística estricta: el prefijo ``on``
 * significa literalmente "**sobre**" o "**arriba de**".
 *
 * Por lo tanto, el sistema de diseño divide los tokens en parejas funcionales para asegurar la accesibilidad:
 * - **Tokens Base / Contenedores**: Actúan como el lienzo, fondo o estructura del componente
 * (ej. ``primary``, ``surface``, ``background``, ``surfaceVariant``, ``primaryContainer``).
 * - **Tokens "On" (Contenido)**: Son los elementos gráficos que se apoyan estrictamente **arriba**
 * de sus respectivos tokens base (textos, iconos, vectores o líneas de borde). Su nomenclatura
 * asociada (ej. ``onPrimary``, ``onSurface``) garantiza que el framework aplique el contraste correcto.
 *
 * #### **Clasificación Funcional de los Slots más Utilizados**:
 * ##### 1. **La Familia de Superficies y Fondos (El Lienzo Principal)**:
 * - ``background`` / ``onBackground``: El lienzo de fondo más profundo de la pantalla (visible
 * detrás de listas con scroll).
 * - ``surface`` / ``onSurface``: El color de los contenedores planos flotantes (``Card``, menús
 * desplegables y diálogos nativos como ``DatePicker``). Su contraparte ``onSurface`` controla los
 * textos y títulos principales de estas estructuras.
 * - ``surfaceVariant`` / ``onSurfaceVariant``: Un matiz alternativo de la superficie para
 * componentes sutiles (el fondo grisáceo de un ``TextField`` o líneas divisorias). Su color ``on``
 * controla textos secundarios, etiquetas de ayuda o placeholders.
 *
 * ##### 2. **La Familia de Énfasis y Acentos (La Identidad de Marca)**:
 * - ``primary`` / ``onPrimary``: El color insignia de la aplicación. Controla componentes de máxima
 * prioridad como botones de acción principales (``Button``) y estados seleccionados.
 * - ``primaryContainer`` / ``onPrimaryContainer``: Bloques contenedores de énfasis medio. Es el slot
 * crítico que usan los componentes nativos complejos (como los días seleccionados o botones internos
 * del ``DatePicker``) para destacar elementos sin saturar la interfaz.
 * - ``secondary`` y ``tertiary`` (con sus respectivos pares ``on``): Capas cromáticas secundarias
 * reservadas para acentos discretos, estados de selección alternativos (chips) o selectores de hora.
 *
 * ##### 3. **La Familia de Estados Críticos e Inversos**:
 * - ``error`` / ``onError``: Destinado exclusivamente a estados destructivos, validaciones de
 * formularios fallidas o alertas críticas.
 * - ``inverseSurface`` / ``inverseOnSurface``: Inversión total de la paleta predominante. Utilizado
 * por componentes efímeros que necesitan romper drásticamente el esquema visual de la pantalla para
 * capturar la atención del usuario (ej. el fondo y texto de un ``Snackbar``).
 *
 * > **Nota de Contraste Operativo (Garantía WCAG)**:
 * Los pares de diseño (Base + On) están calculados matemáticamente para cumplir con las directrices WCAG
 * (*Web Content Accessibility Guidelines*), asegurando un ratio de contraste mínimo de **4.5:1** para texto normal,
 * impidiendo cortocircuitos visuales (como textos invisibles por usar el mismo color de fuente que de fondo).
 *
 * #### TIP para obtener la paleta de colores
 * 1. Entrar al sitio de [Material Theme Builder](https://material-foundation.github.io/material-theme-builder/)
 * 2. Hacer click en el color **Primary** e ingresar el código Hexadecimal del color (o elegir con el slider)
 * 3. Tildar el checkbox de **Color match** para que el builder sea fiel al color ingresado
 * 4. Opcionalmente, se pueden elegir fuentes tipográficas
 * 5. Exportar (elegir la opción de Jetpack Compose para descargar un archivo ``.zip`` que contiene tres
 * archivos: ``Color.kt``, ``Theme.kt`` y ``Type.kt``)
 *
 * @see Typography
 * @see tirra
 * @see shapes
 */
@Composable
fun LabTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = shapes,
        content = content
    )
}
