package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.android_and_kotlin_lab_chronicles.R
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase

/**
 * Experimentos con el componente Image, Iconos y gestión de recursos gráficos.
 *
 * ``Icon`` permite añadir íconos. Una de sus sobrecargas, permite usar un `ImageVector` (por ejemplo, `Icons.Default.PlayArrow`).
 * Otra, permite usar un recurso a través de `painterResource` (`painterResource(id = R.drawable.ic_launcher_foreground)`).
 *
 * Se pueden visualizar todos los íconos disponibles [aquí](https://fonts.google.com/icons).
 * Pero para utilizar todos esos íconos en un proyecto, es necesario agregar la siguiente dependencia
 * al ``build.gradle(:app)``:
 *
 * ```kotlin
 * implementation "androidx.compose.material:material-icons-extended:{COMPOSE-VERSION}"
 * ```
 *
 * ``Image`` permite cargar imágenes de distintas formas, aunque lo más habitual es usar un `painter`,
 * que no hace más que **definir lo que se debe pintar en la imagen**. La librería Coil
 * (**Co**routine **I**mage **L**oader) es la librería standard para cargar imágenes que, al estar basada en corrutinas,
 * encaja bien en la filosofía de Jetpack Compose. Esta librería ofrece un `painter` que permite
 * cargar imágenes de forma sencilla, utilizando la función `AsyncImage`, el cual recibe un `model` con la URL de la imagen.
 * Pero si no es la URL, también se le puede pasar un `ImageRequest.Builder`, al cual se le pasa el contexto
 * con `LocalContext.current`.
 *
 * Coil también ofrece la posibilidad de configurar varias cosas, entre ellas por ejemplo, permite utlizar un
 * `ImageRequest.Builder` para definir transformaciones (`CircleCropTransformation()`, `RoundedCornersTransformation()`,
 * `crossfade`), un `Modifier` para determinar qué espacio ocupará la imagen (por ejemplo, `Modifier.fillMaxSize()`),
 * o `ContentScale` para recortar la imagen (`ContentScale.Crop`).
 *
 * Requiere agregar el permiso de Internet:
 *
 * ```xml
 * <uses-permission android:name="android.permission.INTERNET" />
 * ```
 *
 * Requiere agregar la siguiente dependencia:
 *
 * ```kotlin
 * implementation 'io.coil-kt:coil-compose:{COIL-VERSION}'
 * ```
 */
@Composable
fun ImageLab() {
    SamplesShowcase(
        { IconSample() },
        { ImageSample() },
        { AsyncImageSample() },
    )
}

@Composable
fun IconSample() {
    Icon(
        imageVector = Icons.Rounded.Star,
        contentDescription = "Icono",
        tint = Color.Red
    )
}

@Composable
fun ImageSample() {
    Image(
        modifier = Modifier
            .clip(CircleShape)
            .border(5.dp, Color.Red, CircleShape),
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = "ejemplo",
        alpha = 0.5f // Define la opacidad
    )
}

@Composable
fun AsyncImageSample() {
    Box {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://loremflickr.com/400/400/dog?lock=8")
                .crossfade(2000)
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
