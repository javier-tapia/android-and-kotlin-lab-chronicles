package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_sharing.ipc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

/**
 * UI para experimentar con la comunicación entre procesos.
 * Permite visualizar el envío de datos a un servicio que podría
 * estar corriendo en un proceso separado del sistema.
 */
class IpcActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            BaseLayoutScreen(title = "IPC Lab") {
                // UI para conectar, enviar mensaje y desconectar de un servicio remoto
            }
        }
    }
}