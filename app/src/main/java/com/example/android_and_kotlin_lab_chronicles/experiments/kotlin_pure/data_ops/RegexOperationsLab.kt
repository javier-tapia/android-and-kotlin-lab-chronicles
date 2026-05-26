package com.example.android_and_kotlin_lab_chronicles.experiments.kotlin_pure.data_ops

/**
 * Experimentos con Expresiones Regulares (Regex).
 */
object RegexOperationsLab {

    /**
     * Validación simple de Email.
     */
    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()

    fun isEmailValid(email: String): Boolean {
        return email.matches(EMAIL_REGEX)
    }

    /*
    TODO: Implementar:
    1. Extracción de grupos (find/matchEntire)
    2. Opciones de Regex (IGNORE_CASE, MULTILINE)
    */
}
