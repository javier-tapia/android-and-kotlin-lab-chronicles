package com.example.android_and_kotlin_lab_chronicles.experiments.kotlin_pure.data_ops

/**
 * Experimentos con manipulación avanzada de Strings.
 *
 * Temas: SpannableStrings, Formateo con placeholders y Codificación URL.
 */
object StringOperationsLab {

    /**
     * Ejemplo de uso de format() y placeholders.
     * Uso: "Valor: %.2f".format(25.5) -> "Valor: 25.50"
     */
    fun formatExample(value: Double): String {
        return "Result: %.2f".format(value)
    }

    /*
    TODO: Implementar:
    1. Uso de SpannableStringBuilder para textos con colores/estilos.
    2. URLEncoder y URLDecoder para parámetros de red.
    */
}
