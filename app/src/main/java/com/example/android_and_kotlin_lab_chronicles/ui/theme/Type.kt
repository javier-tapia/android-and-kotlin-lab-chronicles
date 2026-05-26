package com.example.android_and_kotlin_lab_chronicles.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.android_and_kotlin_lab_chronicles.R

/**
 * Para agregar **fuentes personalizadas** (`tirra` para el ejemplo), se debe hacer lo siguiente:
 *
 * 1. Entrar al sitio de [Google Fonts](https://fonts.google.com/).
 * 2. Elegir la **familia de la fuente** (**_Font Family_**) ``tirra`` y hacer click para entrar al
 * detalle (acá también permite previsualizar los distintos estilos en caso de que tenga varios).
 * 3. Hacer click en el botón **_Get Font_** y luego descargarla (usualmente, se guarda un archivo
 * ``.zip`` que contiene un archivo ``.txt`` con la licencia y otros ``.ttf`` con los diferentes
 * estilos de la fuente).
 * 4. Crear un nuevo directorio dentro de ``/res`` llamado ``/font`` y mover el archivo (o los
 * archivos, en caso de que sean varios estilos) ``.ttf`` dentro.
 * 5. Se debe cambiar el nombre original de ser necesario, ya que el compilador de Android (``aapt2``)
 * solo admite letras minúsculas de la ``a-z``, números ``0-9`` y guiones bajos ``_``.
 * 6. Se crea una nueva propiedad de tipo ``FontFamily`` que recibe uno o más objetos de tipo
 * ``Font``, los cuales esperan el ``resId`` (la ubicación de los archivos ``.ttf`` importados en el
 * paso previo) y el ``weight`` (el cual debe corresponderse con el tipo de fuente especificada por
 * el archivo ``.ttf``).
 * 7. Finalmente, se puede aplicar esa nueva fuente en donde se quiera. Por ejemplo:
 * ```kotlin
 * fontFamily = tirra, // En lugar de FontFamily.Default
 * ```
 *
 * @see LabTheme
 * @see shapes
 * @see Typography
 */
val tirra = FontFamily(
    Font(resId = R.font.tirra_regular, weight = FontWeight.Normal),
    Font(resId = R.font.tirra_black, weight = FontWeight.Black),
    Font(resId = R.font.tirra_bold, weight = FontWeight.Bold),
    Font(resId = R.font.tirra_extra_bold, weight = FontWeight.ExtraBold),
    Font(resId = R.font.tirra_medium, weight = FontWeight.Medium),
    Font(resId = R.font.tirra_semi_bold, weight = FontWeight.SemiBold)
)

/**
 * Define la **tipografía global** de la app, es decir, los estilos de texto que usarán todos los
 * componentes de Material 3: títulos, *body*, *labels*, *displays*, etc. De esta forma, toda la
 * tipografía de la app se vuelve consistente y centralizada dentro del sistema de *theming*.
 * Material 3 utiliza una estructura llamada `Typography` que agrupa todos estos estilos y permite
 * personalizar:
 * - **Fuente** -> `fontFamily`
 * - **Peso** -> `fontWeight`
 * - **Tamaño** -> `fontSize`
 * - **Interlineado** -> `lineHeight`
 * - ***Tracking*** (ajuste uniforme del espacio entre todas las letras de un texto) -> `letterSpacing`
 *
 * Todos estos estilos se consumen automáticamente al usar componentes de Material y también se pueden
 * utilizar directamente:
 * ```kotlin
 * Text("Some text", style = MaterialTheme.typography.labelMedium)
 * ```
 *
 * @see LabTheme
 * @see shapes
 * @see tirra
 */
val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = tirra,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = tirra,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = tirra,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = tirra,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = tirra,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontFamily = tirra,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = tirra,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = tirra,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 2.sp
    ),
    bodySmall = TextStyle(
        fontFamily = tirra,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 2.sp
    ),
    labelLarge = TextStyle(
        fontFamily = tirra,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = tirra,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = tirra,
        fontWeight = FontWeight.Medium,
        fontSize = 8.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.3.sp
    )
)
