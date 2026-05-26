@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.core.LocalSnackbarHostState
import kotlinx.coroutines.launch

/**
 * Experimentos con las diferentes variantes de TopAppBars:
 * - ``TopAppBar`` (Small): La estándar, de altura fija.
 * - ``CenterAlignedTopAppBar``: Igual a la anterior, pero el título siempre está centrado.
 * - ``MediumTopAppBar``: Tiene un título más grande y más espacio vertical.
 * - ``LargeTopAppBar``: La más alta, ideal para pantallas de inicio con títulos muy importantes.
 *
 * Sobre ``TopAppBar`` y ``Scaffold``,
 * ver también [acá](https://github.com/javier-tapia/Apuntes-y-Navaja-Suiza/blob/master/Android/UI/Jetpack%20Compose.md#slot-api--scaffold)
 */
@Composable
fun TopAppBarLab() {
    // Obtener el scope de corrutinas vinculado al ciclo de vida de este Composable
    val scope = rememberCoroutineScope()

    // ESTADO: Controla qué variante de TopBar se muestra (0 a 3)
    var selectedVariant by rememberSaveable { mutableIntStateOf(0) }

    // Consumir el estado del Snackbar inyectado por la base (vía CompositionLocal)
    val snackbarHostState = LocalSnackbarHostState.current

    // Usar el scope para lanzar la función de suspensión showSnackbar
    val onNotify = { message: String ->
        scope.launch { snackbarHostState.showSnackbar(message) }
    }

    // Definir el Scroll Behavior.
    // En este caso, hacer que la barra se encoja al scrollear hacia arriba.
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection), // Vincula el scroll a la AppBar
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            // Decide qué barra mostrar según el estado
            when (selectedVariant) {
                0 -> TopAppBarSample(
                    scrollBehavior = scrollBehavior,
                    onClickIcon = { tag ->
                        onNotify("Has pulsado $tag")
                    }
                )

                1 -> CenterAlignedTopAppBarSample(
                    scrollBehavior = scrollBehavior,
                    onClickIcon = { tag ->
                        onNotify("Has pulsado $tag")
                    }
                )

                2 -> MediumTopAppBarSample(
                    scrollBehavior = scrollBehavior,
                    onClickIcon = { tag ->
                        onNotify("Has pulsado $tag")
                    }
                )

                3 -> LargeTopAppBarSample(
                    scrollBehavior = scrollBehavior,
                    onClickIcon = { tag ->
                        onNotify("Has pulsado $tag")
                    }
                )
            }
        }
    ) { innerPadding ->
        // Una lista larga para poder probar el gesto de scroll
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            item {
                Text(
                    text = "Instrucciones: Scrollear hacia arriba para ver cómo reacciona la TopAppBar.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            item {
                ControlCard {
                    Button(onClick = { selectedVariant = 0 }) { Text("TopAppBar") }
                    Button(onClick = { selectedVariant = 1 }) { Text("CenterAlignedTopAppBar") }
                    Button(onClick = { selectedVariant = 2 }) { Text("MediumTopAppBar") }
                    Button(onClick = { selectedVariant = 3 }) { Text("LargeTopAppBar") }
                }
            }

            items(50) { index ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Elemento de la lista #$index",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TopAppBarSample(
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onClickIcon: (String) -> Unit,
    onClickDrawer: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(text = "TopAppBar") },
        scrollBehavior = scrollBehavior, // Se asigna el comportamiento
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Red,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White,
            titleContentColor = Color.White
        ),
        navigationIcon = {
            IconButton(onClick = { onClickDrawer?.invoke() }) { // Llamada segura a una lambda nullable
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "menu"
                )
            }
        },
        actions = {
            IconButton(onClick = { onClickIcon("Buscar") }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "search"
                )
            }

            IconButton(onClick = { onClickIcon("¡Peligro!") }) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "warning"
                )
            }
        }
    )
}

@Composable
fun CenterAlignedTopAppBarSample(
    scrollBehavior: TopAppBarScrollBehavior,
    onClickIcon: (String) -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text("CenterAlignedTopAppBar") },
        scrollBehavior = scrollBehavior, // Se asigna el comportamiento
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Red,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White,
            titleContentColor = Color.White
        ),
        navigationIcon = {
            IconButton(onClick = { onClickIcon("Menú Central") }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Localized description")
            }
        },
        actions = {
            IconButton(onClick = { onClickIcon("Alerta") }) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}

@Composable
fun MediumTopAppBarSample(
    scrollBehavior: TopAppBarScrollBehavior,
    onClickIcon: (String) -> Unit
) {
    MediumTopAppBar(
        title = { Text("MediumTopAppBar") },
        scrollBehavior = scrollBehavior, // Se asigna el comportamiento
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Red,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White,
            titleContentColor = Color.White
        ),
        navigationIcon = {
            IconButton(onClick = { onClickIcon("Atrás") }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Localized description")
            }
        },
        actions = {
            IconButton(onClick = { onClickIcon("Buscar") }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}

@Composable
fun LargeTopAppBarSample(
    scrollBehavior: TopAppBarScrollBehavior,
    onClickIcon: (String) -> Unit
) {
    LargeTopAppBar(
        title = { Text("LargeTopAppBar") },
        scrollBehavior = scrollBehavior, // Se asigna el comportamiento
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Red,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White,
            titleContentColor = Color.White
        ),
        navigationIcon = {
            IconButton(onClick = { onClickIcon("Menú") }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Localized description")
            }
        },
        actions = {
            IconButton(onClick = { onClickIcon("Buscar") }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}

@Composable
private fun ControlCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Probar variantes", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "Seleccioná una opción para cambiar la TopAppBar.",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(Modifier.height(8.dp))
        content()
    }
}
