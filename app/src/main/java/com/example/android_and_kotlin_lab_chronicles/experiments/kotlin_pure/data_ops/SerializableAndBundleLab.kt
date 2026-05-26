package com.example.android_and_kotlin_lab_chronicles.experiments.kotlin_pure.data_ops

import android.content.Intent
import android.os.Build
import java.io.Serializable

/**
 * Experimentos sobre Serialización y paso de datos en Bundles.
 *
 * Aprendizaje: Cómo manejar la deprecación de getSerializableExtra en Android 13 (Tiramisu)+.
 */
object SerializableAndBundleLab {

    /**
     * Función de extensión para obtener Serializable de forma segura según la versión de Android.
     */
    inline fun <reified T : Serializable> Intent.getSerializableCompat(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getSerializableExtra(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            getSerializableExtra(key) as? T
        }
    }
}
