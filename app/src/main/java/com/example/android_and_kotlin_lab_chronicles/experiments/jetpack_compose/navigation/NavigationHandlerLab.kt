package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay

/**
 * **Navigation Compose "clásico" (con `NavHost`):**
 * - El ``NavController`` es el "motor" que empuja los cambios y el ``NavHost`` es el "mapa"
 *
 * **Navigation3:**
 * - Navigation3 rompe con el ``NavHost`` tradicional.
 * - Ya no hay un objeto que "gestiona" la pila de forma imperativa; ahora la navegación es una
 * lista de estados.
 * - Si la lista cambia, la pantalla cambia. Es puramente declarativo.
 *
 * **Diferencias Clave:**
 * 1. **Controlador vs Estado**: En Navigation clásico se usa ``navController.navigate()``.
 * En Navigation 3, simplemente se modifica una lista de Kotlin (``backstack = backstack + nuevaRuta``).
 *
 * 2. **Visibilidad**: En la versión anterior, el grafo está "escondido" dentro del ``NavHost``.
 * En Navigation 3, se usa un ``when`` normal de Kotlin, lo que lo hace mucho más fácil de debuguear
 * y de aplicar animaciones personalizadas.
 *
 * 3. ***Type-Safety***: Ambos ya soportan Kotlin Serialization,
 * eliminando los molestos Strings con ``/`` y ``?arg=``.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationHandlerLab() {
    // Estado para alternar entre Nav3 y Navigation clásico sin usar SamplesShowcase
    var currentLab by rememberSaveable { mutableStateOf("Nav3") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Laboratorio: $currentLab") })
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentLab == "Nav3",
                    onClick = { currentLab = "Nav3" },
                    icon = { Icon(Icons.Default.Star, null) },
                    label = { Text("Nav 3") }
                )
                NavigationBarItem(
                    selected = currentLab == "Clásico",
                    onClick = { currentLab = "Clásico" },
                    icon = { Icon(Icons.Default.Build, null) },
                    label = { Text("Clásico") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (currentLab) {
                "Nav3" -> Navigation3Sample()
                "Clásico" -> NavigationSample()
            }
        }
    }
}

/**
 * - ``NavBackStack``: Es una interfaz que representa la pila de navegación como una lista reactiva
 * y observable de entradas; a diferencia del controlador clásico, no recibe órdenes de "ir a",
 * sino que permite manipular la lista directamente (añadir o quitar elementos) para que la UI
 * reaccione al estado.
 * - ``rememberNavBackStack()``: Crea y mantiene el estado de navegación (la lista de rutas,
 * pila o *backstack*) que sobrevive a cambios de configuración y recreación del proceso.
 * - ``NavDisplay``: Es el componente visual (``Composable``) que observa el *backstack* y decide qué
 * ``Scene`` mostrar y cómo animar la transición entre ellas.
 * - ``Scene``: Representa una **configuración visual en pantalla** que agrupa una o más entradas de
 * navegación (por ejemplo, para manejar vistas de panel dividido o *adaptive layouts*).
 * - ``NavKey``: Es un **identificador único y serializable** que representa el **estado de una escena**;
 * se genera automáticamente para determinar cuándo el ``NavDisplay`` debe disparar una animación.
 * - ``NavEntry``: Es el objeto que envuelve la ruta original junto con su propio ciclo de vida
 * (*Lifecycle*), estado guardado (*SavedState*) y metadatos. Es el objeto que realmente "vive"
 * en la memoria.
 * - ``entryProvider {...}``: Es una función de orden superior donde se centraliza la **definición de
 * todas las rutas** y cómo estas se transforman en contenido visual. Se encarga de procesar el
 * *backstack* cuando se navega a una ruta, envolviendo cada objeto de ruta en un ``NavEntry`` en
 * tiempo de ejecución.
 * - ``entry {...}``: Es la **declaración de qué datos necesita la pantalla**. Dentro del
 * ``entryProvider``, vincula un tipo de ruta específico con un ``Composable`` y permite adjuntar
 * metadatos de comportamiento (como animaciones).
 * - ``key`` de ``entry<T> { key -> ... }``: Es la **instancia real del objeto de ruta** (la
 * ``data class`` o ``data object``) que desencadenó la navegación, conteniendo todos sus parámetros
 * de datos ya deserializados. Es "Type-Safe" porque el compilador conoce el tipo exacto ``<T>``
 * definido en la función ``entry``, permitiendo acceder a las propiedades de la ruta sin necesidad
 * de casteos manuales o búsqueda de parámetros en un ``Bundle``.
 * - ``targetState``: Representa la ``Scene`` **hacia la cual se está navegando**; se usa en el
 * ``transitionSpec`` (y sus variantes ``popTransitionSpec`` y ``predictivePopTransitionSpec``)
 * para definir la animación de entrada.
 * - ``initialState``: Representa la ``Scene`` **desde la cual se está saliendo**; se usa en el
 * ``transitionSpec`` (y sus variantes ``popTransitionSpec`` y ``predictivePopTransitionSpec``)
 * para definir la animación de salida en conjunto con el estado de destino.
 */
@Composable
fun Navigation3Sample() {
    val backstack = rememberNavBackStack(Routes.ScreenA)

    NavDisplay(
        backStack = backstack,
        onBack = { if (backstack.size > 1) backstack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Routes.ScreenA> {
                ScreenAContent(
                    onNavigateToScreenB = {
                        backstack.add(
                            Routes.ScreenB(
                                name = "Javi",
                                age = 40
                            )
                        )
                    },
                    onNavigateToErrorScreen = { backstack.add(Routes.Error) }
                )
            }

            entry<Routes.ScreenB>(
                // Animación específica para cuando se navegue a esta pantalla
                metadata = {
                    NavDisplay.transitionSpec {
                        fadeIn(tween(800)) togetherWith
                                fadeOut(tween(800))
                    }
                }
            ) { key ->
                ScreenBContent(name = key.name, age = key.age) {
                    backstack.removeLastOrNull()
                }
            }

            entry<Routes.Error>(
                // Animación específica para cuando se navegue a esta pantalla
                metadata = NavDisplay.transitionSpec {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(700)
                    ) togetherWith fadeOut(tween(700))
                }
            ) {
                ErrorContent {
                    backstack.removeLastOrNull()
                }
            }
        },
        // Las tres variantes funcionales que se suelen definir en un 'NavDisplay' sirven para cubrir
        // los tres momentos clave del ciclo de vida de la navegación: adelante, atrás y predictive back.
        // Las tres operan con keys serializadas en lugar de las instancias del tipo original porque
        // Navigation 3 modela el backstack como una lista de referencias (keys) que deben
        // sobrevivir a la finalización del proceso y a los cambios de configuración.
        // El 'this' en sus respectivos bloques da acceso a 'initialState' (la pantalla que se va)
        // y a 'targetState' (la pantalla que entra).

        // "Hacia adelante": se dispara al hacer 'backstack.add(...)'.
        // Animación por defecto que ocurre cuando se agrega un elemento a la pila
        transitionSpec = {
            slideInHorizontally { it } togetherWith
                    slideOutHorizontally { -it }
        },
        // "Hacia atrás": se dispara al hacer 'backstack.removeLastOrNull()'.
        // Es la animación que ocurre cuando se quita el último elemento de la pila de forma programática
        popTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(500)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(500)
            )
        },
        // "Gesto de Android": se dispara con el gesto físico de "Atrás" del sistema (específica
        // para Android 14+). Controla cómo se ve la pantalla mientras el usuario arrastra el dedo
        // desde el borde para volver, pero aún no ha soltado.
        // A diferencia de las otras, esta animación es interactiva; se mueve al ritmo del dedo del
        // usuario. Por eso suele ser igual a la popTransitionSpec, pero optimizada para que el
        // sistema la maneje frame a frame.
        predictivePopTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(500)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(500)
            )
        }
    )
}

/**
 * - ``NavHostController``: Es la implementación principal de ``NavController`` que añade el soporte
 * para manejar el ciclo de vida de los ``Composables`` y vincularse a un ``NavHost``; actúa como el
 * motor centralizado que ejecuta órdenes imperativas para cambiar el estado de la navegación.
 * - ``rememberNavController()``: Crea y mantiene una instancia del controlador (``NavHostController``)
 * que gestiona la pila de navegación y el estado de cada pantalla durante el ciclo de vida del ``Composable``.
 * - ``NavHost``: Actúa como el **contenedor principal y el "mapa" de navegación**, vinculando el
 * controlador con el grafo de destinos disponibles.
 * - ``NavBackStackEntry``: Representa una entrada individual en la pila; contiene información
 * contextual sobre la ruta actual, como parámetros y el estado del ciclo de vida.
 * - ``composable<T> {...}``: Define un destino específico en el grafo de navegación asociado a un
 * tipo de dato único, encargándose de inflar el contenido visual cuando se navega a esa ruta.
 * - ``navigate()``: Es el comando imperativo que le ordena al controlador cambiar la pantalla actual,
 * añadiendo un nuevo destino a la pila de navegación.
 * - ``toRoute<T>()``: Una función de extensión de *Type-Safe Navigation* que extrae y deserializa
 * automáticamente los argumentos de la entrada actual para convertirlos de nuevo en
 * el objeto de ruta original.
 * - ``popBackStack()``: Elimina el destino actual de la parte superior de la pila y regresa a la
 * pantalla anterior, gestionando automáticamente el orden de retroceso.
 * - ``popUpTo(route) {...}``: Permite limpiar la pila de navegación hasta un destino específico
 * antes de realizar una nueva transición, evitando la acumulación de pantallas repetidas y
 * gestionando si el destino de referencia también debe ser eliminado (``inclusive = true``).
 * - ``enterTransition``: Define cómo aparece la nueva pantalla que entra en el frente de la interfaz.
 * - ``exitTransition``: Define cómo desaparece la pantalla actual para dejar paso a la nueva.
 * - ``popEnterTransition``: Define la animación de la pantalla que vuelve a aparecer cuando el
 * usuario regresa hacia atrás (la que estaba "debajo" en la pila).
 * - ``popExitTransition``: Define cómo desaparece la pantalla superior cuando es eliminada de la
 * pila mediante un gesto o comando de retroceso.
 */
@Composable
fun NavigationSample() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.ScreenA,
        // Animaciones globales por defecto (equivalente al transitionSpec de Nav3)
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { -it } },
        popEnterTransition = { slideInHorizontally { -it } },
        popExitTransition = { slideOutHorizontally { it } }
    ) {
        composable<Routes.ScreenA> {
            ScreenAContent(
                onNavigateToScreenB = {
                    navController.navigate(Routes.ScreenB(name = "Javi", age = 40))
                },
                onNavigateToErrorScreen = {
                    navController.navigate(Routes.Error)
                }
            )
        }
        composable<Routes.ScreenB>(
            // Animación específica para cuando se navegue a esta pantalla
            enterTransition = { fadeIn(tween(800)) },
            exitTransition = { fadeOut(tween(800)) }
        ) { backStackEntry ->
            // Los argumentos se recuperan de forma automática
            val profile = backStackEntry.toRoute<Routes.ScreenB>()
            ScreenBContent(profile.name, profile.age) {
                navController.popBackStack()
            }
        }
        composable<Routes.Error>(
            // Animación específica para cuando se navegue a esta pantalla
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(700)
                )
            },
            exitTransition = { fadeOut(tween(700)) }
        ) {
            ErrorContent {
                navController.navigate(Routes.ScreenA) {
                    popUpTo<Routes.ScreenA> {
                        inclusive = true
                    }
                }
            }
        }
    }
}
