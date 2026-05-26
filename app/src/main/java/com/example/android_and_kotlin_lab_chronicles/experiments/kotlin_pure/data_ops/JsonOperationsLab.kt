package com.example.android_and_kotlin_lab_chronicles.experiments.kotlin_pure.data_ops

/**
 * Experimentos con operaciones JSON.
 *
 * Aprendizaje: Uso de Gson para serialización/deserialización y manejo de Tipos Genéricos
 * mediante TypeToken para evitar la pérdida de tipos (Type Erasure).
 */
object JsonOperationsLab {

    // Ejemplo de un modelo sencillo para pruebas
    data class LabUser(val id: Int, val name: String, val email: String?)

    /*
    TODO: Implementar:
    1. String?.jsonToObject<T>() usando reified
    2. Any.objectToJson()
    3. Lectura de JSON desde Resources (InputStreamReader)
    */
}
