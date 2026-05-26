package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.navigation.activity_result_contracts

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.core.net.toUri
import com.example.android_and_kotlin_lab_chronicles.core.BaseLayoutScreen

class ActivityResultFirstActivity : ComponentActivity() {
    /**
     * Crea una propiedad de tipo `ActivityResultLauncher<Intent!>` antes de crear la *activity* y registrar el *callback*
     *
     * @see ActivityResultSecondActivity
     */
    private val resultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            // Handle the returned result
            if (result.resultCode == 0) {
                val data: Intent? = result.data
                setResult(0, data)
                Toast.makeText(this, "De vuelta en FirstActivity...", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaseLayoutScreen(title = "Activity Result Contracts") {
                Button(
                    onClick = { navigateToSecondActivity() }
                ) {
                    Text(
                        text = "Navegar a SecondActivity"
                    )
                }
            }
        }
    }

    /**
     * Lanza otra *activity* (solo si el ciclo de vida de la *activity* actual alcanzó el estado `CREATED`)
     */
    private fun navigateToSecondActivity() {
        Toast.makeText(this, "Navegando a SecondActivity...", Toast.LENGTH_SHORT).show()

        val intent = Intent(Intent.ACTION_VIEW)
            .apply {
                `package` = packageName
                data = "example://screen/second".toUri()
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
        resultLauncher.launch(intent)
    }
}
