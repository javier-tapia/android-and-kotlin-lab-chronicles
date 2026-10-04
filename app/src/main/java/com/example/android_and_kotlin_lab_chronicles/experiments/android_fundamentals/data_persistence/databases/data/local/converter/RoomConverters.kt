package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.converter

import androidx.room.TypeConverter
import java.util.Date

/**
 * ### Room *Type Converters*
 *
 * Clase puente encargada de convertir tipos de datos complejos o no nativos de Java/Kotlin
 * a tipos primitivos soportados de forma nativa por el motor de base de datos **SQLite**.
 *
 * #### Fundamento Técnico y Limitación de SQLite
 *
 * SQLite admite un conjunto reducido y estricto de **tipos de datos primarios (*Storage Classes*)**:
 * - `NULL`
 * - `INTEGER` (enteros de 1, 2, 3, 4, 6 u 8 bytes)
 * - `REAL` (valores de punto flotante de 8 bytes)
 * - `TEXT` (cadenas de caracteres con codificación UTF-8, UTF-16BE o UTF-16LE)
 * - `BLOB` (bloque de datos binarios almacenado exactamente como se ingresó)
 *
 * Dado que objetos como [java.util.Date], [java.util.UUID] o enumerados (`Enum`) no pertenecen a estas
 * clases de almacenamiento, Room no sabe cómo serializarlos ni deserializarlos por defecto.
 *
 * #### Funcionamiento de la Anotación `@TypeConverter`
 *
 * Cada método anotado con ``@TypeConverter`` le indica a **KSP** (*Kotlin Symbol Processing*)
 * el algoritmo de transformación bidireccional entre el objeto de dominio de alto nivel y el
 * primitivo persistible en la columna de SQLite:
 *
 * 1. **Lectura de la DB**: [fromTimestamp] toma un valor entero de la columna `INTEGER` (`Long?`)
 *    y lo reconstruye a un objeto [java.util.Date] para el código de la aplicación.
 * 2. **Escritura en la DB**: [dateToTimestamp] toma un objeto [java.util.Date] del código y extrae su
 *    representación en milisegundos (`Long?`) para persistirlo en la columna `INTEGER`.
 *
 * #### Manejo de Nulabilidad
 *
 * Ambos métodos aceptan y retornan tipos anulables (`Nullable`). Esto garantiza que si una propiedad
 * [java.util.Date] es `null` en la entidad, el convertidor no lance una excepción y Room pueda persistir un
 * valor `NULL` directamente en la celda de la base de datos SQLite.
 *
 * > **Registro Global:**
 * > Esta clase se registra a nivel de base de datos en [com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.RoomLabDatabase] mediante la anotación
 * > `@TypeConverters(RoomConverters::class)`, lo que habilita la conversión automática de campos
 * > [java.util.Date] en cualquier `@Entity` o consulta de `@Dao` asociada a esa base de datos.
 */
class RoomConverters {

    /**
     * Convierte un *timestamp* en milisegundos a una instancia de [java.util.Date].
     *
     * @param value Tiempo transcurrido desde la época Unix en milisegundos (`Long?`), leído desde
     * la columna `INTEGER` de SQLite.
     * @return Objeto [java.util.Date] reconstruido o `null` si el valor de la columna era `NULL`.
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    /**
     * Convierte un objeto [Date] a su representación numérica en milisegundos desde la época Unix.
     *
     * @param date Instancia de [Date] a persistir.
     * @return Valor numérico en milisegundos (`Long?`) para almacenar en la columna `INTEGER` de
     * SQLite o `null`.
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}