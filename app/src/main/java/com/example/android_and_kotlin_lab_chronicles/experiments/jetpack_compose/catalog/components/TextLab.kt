package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase

/**
 * El componente ``TextField`` sería el equivalente al ``EditText`` en el sistema de vistas clásico.
 * Dicho componente debe gestionar estado y recibe dos parámetros: el valor que se le va a asignar
 * y una lambda que va a retornar el valor luego de ser modificado por el usuario.
 *
 * El ``OutlinedTextField`` es una variante del ``TextField`` que muestra un borde alrededor del campo.
 * Es posible modificar el color del borde dependiendo si está en foco o no.
 *
 * `AnnotatedString` sería como el ``SpannableString`` del sistema de vistas clásico.
 * Usa un *Builder* para "armar" el texto por piezas. Es una clase que permite
 * aplicar diferentes estilos (color, tamaño, negrita, links) a partes específicas de una misma
 * cadena de texto. Y además, permite guardar metadatos que pueden recuperarse después.
 *
 * Algunos ejemplos de uso de `AnnotatedString`:
 *
 * **1. Menciones y Hashtags (Estilo Twitter/Instagram)** -> Cuando se ve un texto como "Hola @Javi,
 * mira este #AndroidLab", el sistema no solo pinta de azul esas palabras.
 * - Metadato: Se guarda una anotación con el USER_ID en "@Javi" y un TAG_ID en "#AndroidLab".
 * - Uso: Al tocar la mención, la app no necesita buscar el nombre en una base de datos;
 * simplemente lee el metadato del clic y navega directamente al perfil del usuario 12345.
 *
 * **2. Términos Legales Seleccionables** -> Imagina un bloque de texto legal donde hay tres
 * documentos diferentes: "Política de Privacidad", "Cookies" y "Condiciones de Uso".
 * - Metadato: Cada frase tiene una anotación con una clave diferente (ej.: "PRIVACY_DOC", "COOKIE_DOC").
 * - Uso: Un solo Text maneja toda la lógica. Al hacer clic, extraes el metadato y decides qué PDF
 * abrir o qué pantalla mostrar.
 *
 * **3. Traducción o Definiciones "On-the-fly"** -> En apps de lectura o aprendizaje de idiomas.
 * - Metadato: Cada palabra difícil tiene una anotación con su definición o traducción técnica.
 * - Uso: Cuando el usuario mantiene presionado o toca una palabra, la app extrae el metadato y
 * muestra un *Tooltip* o un *Bottom Sheet* con la explicación, sin haber tenido que separar el
 * texto en cientos de componentes pequeños.
 *
 * **4. Deep Linking Interno** -> A veces el texto viene de un servidor (un mensaje de soporte,
 * por ejemplo) que dice: "Tu pedido #98765 está en camino".
 * - Metadato: Se etiqueta "#98765" con el tipo "ORDER_ID".
 * - Uso: La app detecta que es un pedido y, al tocarlo, redirige al usuario a la pantalla de
 * tracking de ese pedido específico.
 */
@Composable
fun TextLab() {
    SamplesShowcase(
        { TextSample() },
        { TextFieldSample() },
        { OutlinedTextFieldSample() },
        { AnnotatedStringSample() },
        { LinkableTextSample() }
    )
}

@Composable
fun TextSample() {
    Column(Modifier.fillMaxSize()) {
        Text(text = "Esto es un ejemplo")
        Text(text = "Esto es un ejemplo", color = Color.Blue)
        Text(text = "Esto es un ejemplo", fontWeight = FontWeight.ExtraBold)
        Text(text = "Esto es un ejemplo", fontWeight = FontWeight.Light)
        Text(text = "Esto es un ejemplo", fontFamily = FontFamily.Cursive)
        Text(
            text = "Esto es un ejemplo",
            textDecoration = TextDecoration.LineThrough
        )
        Text(
            text = "Esto es un ejemplo",
            textDecoration = TextDecoration.Underline
        )
        Text(
            text = "Esto es un ejemplo",
            textDecoration = TextDecoration.combine(
                listOf(TextDecoration.Underline, TextDecoration.LineThrough)
            )
        )
        Text(text = "Esto es un ejemplo", fontSize = 30.sp)
    }
}

@Composable
fun TextFieldSample() {
    var myText by remember { mutableStateOf("") }

    TextField(
        value = myText,
        onValueChange = {
            myText = if (it.contains("a")) {
                it.replace("a", "")
            } else {
                it
            }
        },
        label = { Text(text = "Introduce tu nombre") })
}

@Composable
fun OutlinedTextFieldSample() {
    var myText by remember { mutableStateOf("") }

    OutlinedTextField(
        value = myText,
        onValueChange = { myText = it },
        modifier = Modifier.padding(24.dp),
        label = { Text(text = "Example") },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Magenta,
            unfocusedContainerColor = Color.Blue
        )
    )
}

@Composable
fun AnnotatedStringSample() {
    val context = LocalContext.current

    val annotatedString = buildAnnotatedString {
        append("Usuario: ")

        // Se agrega un `annotation` para guardar un ID de usuario oculto.
        // Lo que se agregue entre esta función y la función `pop()`, es lo que llevará la anotación
        pushStringAnnotation(tag = "USER_ID", annotation = "12345")

        // Se le da un estilo específico
        withStyle(style = SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold)) {
            append("Javi")
        }

        // Se cierra la anotación
        pop()

        append(". Estado: ")

        withStyle(style = SpanStyle(background = Color.Green)) {
            append("En línea")
        }
    }

    // Acá el texto es estático, pero contiene metadatos que se pueden leer
    Text(text = annotatedString)

    Spacer(modifier = Modifier.height(20.dp))

    Button(onClick = {
        // Ejemplo de cómo buscar TODAS las anotaciones de tipo "USER_ID" en el texto
        val annotations = annotatedString.getStringAnnotations(
            tag = "USER_ID",
            start = 0,
            end = annotatedString.length
        )

        annotations.firstOrNull()?.let { annotation ->
            Toast.makeText(context, "ID recuperado: ${annotation.item}", Toast.LENGTH_SHORT).show()
        }
    }) {
        Text("Leer ID oculto de Javi")
    }
}

@Composable
fun LinkableTextSample() {
    val context = LocalContext.current

    val annotatedString = buildAnnotatedString {
        append("Revisá nuestro ")

        // `LinkAnnotation.Url` Define el destino, el estilo y el comportamiento en un solo bloque
        withLink(
            LinkAnnotation.Url(
                url = "https://github.com/javier-tapia/android-and-kotlin-lab-chronicles",
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                    )
                ),
                // Si no se setea el listener, Compose intentará abrir la URL por defecto
                // si el sistema lo permite. Sin embargo, definirlo uno mismo da control total:
                // se podría registrar un evento en Analytics, mostrar un diálogo de confirmación
                // antes de salir de la app, o incluso decidir abrir la URL en una WebView
                // interna en lugar del navegador externo.
                linkInteractionListener = { linkAnnotation ->
                    // Se extrae la URL de la anotación
                    val url = (linkAnnotation as LinkAnnotation.Url).url

                    // Se crea un Intent para abrir el navegador
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())

                    // Se intenta lanzar la actividad
                    context.startActivity(intent)
                }
            )
        ) {
            append("repositorio de laboratorio")
        }

        append(" para más detalles.")
    }

    // Importante: No hace falta Modifier.clickable ni calcular índices manualmente;
    // Compose se encarga de interceptar el toque solo en el área del enlace,
    // ya que el Text detecta el LinkAnnotation
    Text(text = annotatedString)
}
