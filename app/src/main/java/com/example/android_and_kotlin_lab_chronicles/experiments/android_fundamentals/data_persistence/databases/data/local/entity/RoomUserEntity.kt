package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ### Entidad Usuario
 *
 * Mapea la clase de datos Kotlin a una tabla relacional SQLite.
 *
 * #### Detalle de Anotaciones y Parámetros
 * - **`@Entity`**: Declara la clase como una **tabla física** gestionada por Room.
 *     - `tableName`: Especifica el nombre explícito de la tabla en SQLite (`room_users`). Si se omite,
 *     Room utiliza por defecto el nombre de la clase Kotlin.
 * - **`@PrimaryKey`**: Identifica el campo como la **clave primaria** única de la tabla.
 *     - `autoGenerate`: Cuando se establece en `true`, SQLite asigna automáticamente un identificador
 *     entero único e incremental (`ROWID`) al insertar un registro.
 * La asignación por defecto (`uid: Long = 0`) **no significa que la clave primaria en la base de
 * datos vaya a ser cero**. El valor `0` actúa como un **marcador temporal en memoria**
 * (*placeholder*). Cuando Room recibe una entidad con `uid = 0` (o `null` en tipos anulables) en
 * una operación de inserción, ignora dicho valor y permite que el motor de SQLite autogenere la
 * clave correspondiente (`1`, `2`, `3`...).
 *
 * - **`@ColumnInfo`**: Mapea la propiedad de la clase a una columna específica en la tabla.
 *     - `name`: Define el nombre explícito de la columna en la base de datos (`first_name`, `last_name`).
 *     Permite respetar las convenciones de nombrado estándar de SQLite (`snake_case`) manteniendo al mismo
 *     tiempo la convención de Kotlin (`camelCase`) en el código fuente.
 *
 * @property uid Identificador único incremental del usuario (Clave Primaria).
 * @property firstName Nombre del usuario.
 * @property lastName Apellido del usuario.
 * @property isActive Indica si el usuario está activo o no.
 */
@Entity(tableName = "room_users")
data class RoomUserEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true
)
