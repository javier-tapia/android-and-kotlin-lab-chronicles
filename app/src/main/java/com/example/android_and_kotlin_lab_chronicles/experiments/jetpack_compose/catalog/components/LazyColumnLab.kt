package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_and_kotlin_lab_chronicles.R
import com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.utils.Superhero
import kotlinx.coroutines.launch

/**
 * Para crear listas dinámicas se usan ``LazyColumn`` (vertical) o ``LazyRow`` (horizontal).
 * Para añadir elementos a estas listas, se utilizan las funciones ``item`` o ``items`` (para más de
 * un elemento).
 *
 * ``LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {...}``:
 * La función ``spacedBy`` permite agregar un espacio entre las vistas hijas del componente
 * a través del eje principal (vertical para ``LazyColumn``, horizontal para ``LazyRow``).
 * Si el espacio es negativo, los hijos van a superponerse.
 *
 * La función ``rememberLazyListState`` crea y recuerda el estado de la lista a través de las
 * diferentes composiciones.
 *
 * ***Sticky headers*** **(cabeceras)**: La función ``stickyHeader`` permite agregar una cabecera
 * que permanece fija hasta que la próxima cabecera ocupe su lugar.
 *
 * `LazyVerticalGrid`: Define un **listado de elementos en grilla vertical**. Requiere de un
 * parámetro llamado `columns`, que define la cantidad de celdas por fila en caso de pasarle un
 * número fijo (`GridCells.Fixed(2)`) o adaptable al tamaño mínimo definido
 * (`GridCells.Adaptive(150.dp)`). Esto es útil para cuando el dispositivo se rota, por ejemplo.
 *
 * `LazyHorizontalGrid`: Lo mismo que la vertical, pero en horizontal. En lugar del parámetro
 * `columns`, requiere uno llamado `rows`.
 *
 * **Mención aparte para ``Modifier.weight(1f)``**:
 * Se debe usarlo cuando la ``LazyColumn`` está dentro de una ``Column`` o ``Row`` y se quiere que
 * la lista rellene el espacio sobrante sin desplazar otros elementos fuera de la pantalla.
 * - **Evita el error de medida infinita**: Si el contenedor padre no tiene un tamaño fijo,
 * el ``weight(1f)`` le otorga a la ``LazyColumn`` una altura calculada, evitando que intente medir
 * "hasta el infinito".
 *
 * - **Mantiene elementos fijos**: Si se tiene un título arriba y un botón abajo, el peso asegura
 * que la lista se quede "atrapada" en el medio, permitiendo que solo el contenido de la lista haga
 * scroll mientras lo demás permanece estático.
 */
@Composable
fun LazyColumnLab() {
    LazyColumnSample()
//    LazyColumnWithStickyHeaderSample()
//    LazyVerticalGridSample()
//    LazyColumnItemsIndexedSample()
}

@Composable
fun LazyColumnSample() {
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val showButton by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 1
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // El Box toma la totalidad del espacio restante gracias al weight(1f) y, a su vez, crea
        // el contexto de superposición para el FAB
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            LazyColumn(
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                // Ocupa la totalidad del Box
                modifier = Modifier.fillMaxSize()
            ) {
                item { Text(text = "Header") }
                items(getSuperheroes()) { superhero ->
                    ItemHero(superhero = superhero)
                    { Toast.makeText(context, it.realName, Toast.LENGTH_SHORT).show() }
                }
                item { Text(text = "Footer") }
            }

            // El FAB flota relativo al Box, NO a toda la pantalla
            if (showButton) {
                FloatingActionButton(
                    modifier = Modifier
                        // Lo posiciona abajo a la derecha del Box
                        .align(Alignment.BottomEnd)
                        // Margen interno respecto a los bordes del Box,
                        .padding(24.dp),
                    onClick = {
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(0)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Ir al inicio"
                    )
                }
            }
        }
    }
}

@Composable
fun LazyColumnWithStickyHeaderSample() {
    val context = LocalContext.current
    val superheroes: Map<Char, List<Superhero>> = getSuperheroes().groupBy { it.realName[0] }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        superheroes.forEach { (initial, heroesList) ->
            stickyHeader {
                Text(
                    text = initial.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray),
                    fontSize = 24.sp,
                    color = Color.Black
                )
            }

            items(heroesList) { superhero ->
                ItemHero(superhero = superhero)
                { Toast.makeText(context, it.realName, Toast.LENGTH_SHORT).show() }
            }
        }
    }
}

@Composable
fun LazyVerticalGridSample() {
    val context = LocalContext.current
    val data = getSuperheroes()

    LazyVerticalGrid(
        // Intenta meter tantas celdas como quepan de mínimo 150.dp
        columns = GridCells.Adaptive(150.dp),
        content = {
            items(data.size) { superhero ->
                ItemHero(superhero = data[superhero])
                { Toast.makeText(context, it.realName, Toast.LENGTH_SHORT).show() }
            }
        },
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    )
}

@Composable
fun LazyColumnItemsIndexedSample() {
    var items by rememberSaveable { mutableStateOf(List(100) { "Item número $it" }) }

    LazyColumn {
        itemsIndexed(items = items, key = { _, item -> item }) { index, item ->
            Row {
                Text("$item and index: $index")
                Spacer(Modifier.weight(1f))
                TextButton(
                    onClick = {
                        items = items.toMutableList().apply {
                            remove(item)
                        }
                    }
                ) {
                    Text("Borrar")
                }
                Spacer(Modifier.width(24.dp))
            }
        }
    }
}

private fun getSuperheroes(): List<Superhero> {
    return listOf(
        Superhero("Spiderman", "Petter Parker", "Marvel", R.drawable.spiderman),
        Superhero("Wolverine", "James Howlett", "Marvel", R.drawable.logan),
        Superhero("Batman", "Bruce Wayne", "DC", R.drawable.batman),
        Superhero("Thor", "Thor Odinson", "Marvel", R.drawable.thor),
        Superhero("Flash", "Jay Garrick", "DC", R.drawable.flash),
        Superhero("Green Lantern", "Alan Scott", "DC", R.drawable.green_lantern),
        Superhero("Wonder Woman", "Princess Diana", "DC", R.drawable.wonder_woman)
    )
}

@Composable
private fun ItemHero(superhero: Superhero, onItemSelected: (Superhero) -> Unit) {
    Card(
        border = BorderStroke(2.dp, Color.Red),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemSelected(superhero) }) {
        Column {
            Image(
                painter = painterResource(id = superhero.photo),
                contentDescription = "SuperHero Avatar",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = superhero.superheroName,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = superhero.realName,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 12.sp
            )
            Text(
                text = superhero.publisher,
                fontSize = 10.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp)
            )
        }

    }
}
