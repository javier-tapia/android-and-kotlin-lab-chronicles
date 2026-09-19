package com.example.android_and_kotlin_lab_chronicles.experiments.dependency_injection.manual

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_and_kotlin_lab_chronicles.LabApplication

/**
 * Pantalla que demuestra el uso de Inyección de Dependencias Manual en Jetpack Compose.
 *
 * En DI manual no existen funciones mágicas de extensión (como `hiltViewModel()` o `koinViewModel()`).
 * La resolución del ViewModel con su respectiva [ManualLabViewModel.Factory] debe resolverse a nivel
 * de composición (como en este ejemplo) o pasarse desde el punto de entrada de la Activity.
 */
@Composable
fun ManualLabScreen(
    modifier: Modifier = Modifier,
    // Accedemos a la Application desde el contexto local de Compose
    context: Context = LocalContext.current,
    // Creación manual del ViewModel conectando el contenedor de la Application con la Factory
    viewModel: ManualLabViewModel = viewModel(
        factory = ManualLabViewModel.Factory(
            (context.applicationContext as LabApplication).appContainer.manualLabConfigurator
        )
    )
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = viewModel.getLabStatus())
        }
    }
}
