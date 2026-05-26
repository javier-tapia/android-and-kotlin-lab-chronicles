package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.core.LocalSnackbarHostState
import kotlinx.coroutines.launch

/**
 * Experimentos con menús laterales (Drawer) integrados con la estructura de la app.
 * Para poder visualizar si los insets se validan correctamente, se usan también
 * un NavigationBar (bottom navigation) y un FAB.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalNavigationDrawerLab() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Se consume el estado del Snackbar inyectado por BaseRawScreen vía CompositionLocal
    val snackState = LocalSnackbarHostState.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet {
                CustomDrawerOptions { scope.launch { drawerState.close() } }
            }
        },
        content = {
            // El Lab define su propio Scaffold porque es un experimento de estructura
            Scaffold(
                snackbarHost = { SnackbarHost(snackState) },
                topBar = {
                    TopAppBarSample(
                        onClickIcon = { tag ->
                            scope.launch {
                                snackState.showSnackbar("Has pulsado $tag")
                            }
                        },
                        onClickDrawer = { scope.launch { drawerState.open() } }
                    )
                },
                content = { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .consumeWindowInsets(paddingValues)
                            .padding(16.dp)
                    ) {
                        Text(text = "Laboratorio de Drawer")
                        Text(text = "Aquí el Scaffold es necesario para probar el docking.")
                    }
                },
                bottomBar = { NavigationBarSample() },
                floatingActionButton = { FloatingActionButtonSample() },
                floatingActionButtonPosition = FabPosition.End
            )
        }
    )
}

@Composable
private fun CustomDrawerOptions(onCloseDrawer: () -> Unit) {
    Column(Modifier.padding(8.dp)) {
        Text(
            text = "Primera opcion", modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { onCloseDrawer() }
        )
        Text(
            text = "Segunda opcion", modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { onCloseDrawer() }
        )
        Text(
            text = "Tercera opcion", modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { onCloseDrawer() }
        )
        Text(
            text = "Cuarta opcion", modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { onCloseDrawer() }
        )
    }
}
