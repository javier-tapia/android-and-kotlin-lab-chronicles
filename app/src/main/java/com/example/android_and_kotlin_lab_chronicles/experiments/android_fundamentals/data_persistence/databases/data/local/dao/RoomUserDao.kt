package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.RoomOrderEntity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.RoomUserEntity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.UserTagCrossRefEntity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.model.OrderWithUser
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.model.UserWithOrders
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.model.UserWithTags
import kotlinx.coroutines.flow.Flow

/**
 * ### Room *Data Access Object* (DAO) *Specifications*
 *
 * Contrato de acceso a datos que define la interfaz de consultas y operaciones sobre la base
 * de datos SQLite. **La implementación concreta es generada automáticamente por KSP en
 * tiempo de compilación**.
 *
 * #### Detalle de Anotaciones y Parámetros
 *
 * - **`@Dao`**: Marca la interfaz como un *Data Access Object* para que KSP genere el código
 *   *boilerplate* SQL y el mapeo de objetos en tiempo de compilación.
 * - **`@Insert`**: Declara una operación de inserción.
 *     - `onConflict`: Define la estrategia a seguir si el registro a insertar viola una restricción
 *     de clave primaria (`PRIMARY KEY`) o un índice único. En este caso, [androidx.room.OnConflictStrategy.Companion.REPLACE]
 *     sobrescribe la fila existente en SQLite por el nuevo registro (equivalente a un `INSERT OR REPLACE`).
 * - **`@Query`**: Permite ejecutar sentencias SQL nativas escritas directamente en cadena de texto.
 *   KSP valida la sintaxis SQL y la existencia de las tablas/columnas contra el esquema de las
 *   entidades durante el *build*; si la consulta es inválida, el proyecto no compila.
 * - **`@Transaction`**: Garantiza que la consulta o grupo de operaciones se ejecute dentro de una
 *   única transacción atómica de SQLite (`BEGIN TRANSACTION` / `COMMIT`). Es **estrictamente obligatorio**
 *   en consultas de relaciones (como `UserWithOrders`) donde Room debe ejecutar internamente dos o
 *   más consultas SQL separadas (una para la entidad padre y otra para las entidades hijas); la
 *   transacción previene condiciones de carrera (*race conditions*) e inconsistencias si la base de datos
 *   se modifica en medio de ambas lecturas.
 * - **`@Delete`**: Elimina la fila correspondiente en SQLite emparejando la clave primaria del objeto pasado.
 *
 * #### Modelo de Ejecución: `suspend` vs. `Flow`
 *
 * - **Operaciones *One-shot* (`suspend`)**:
 *   Las funciones como [insertUser], [insertOrder] y [deleteUser] realizan una única transacción puntual.
 *   Deben marcarse con `suspend` para que Room delegue automáticamente su ejecución al pool de hilos
 *   I/O de SQLite, garantizando que no se bloquee el hilo principal (*Main Thread*).
 *
 * - **Consultas Observables (`Flow<T>`)**:
 *   Las funciones que retornan [kotlinx.coroutines.flow.Flow] **NO deben declararse como `suspend`**.
 *   Al retornar un flujo reactivo, Room registra internamente un [androidx.room.InvalidationTracker]
 *   sobre la tabla SQLite correspondiente (`room_users`). La llamada al método retorna inmediatamente
 *   el objeto `Flow` sin bloquear. Cada vez que ocurre un `INSERT`, `UPDATE` o `DELETE` en la tabla
 *   monitoreada, el tracker detecta la invalidación, re-ejecuta la consulta asincrónicamente en un
 *   hilo secundario y emite la lista actualizada a los colectores activos.
 *
 * @see com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.RoomLabDatabase
 * @see RoomUserEntity
 * @see RoomOrderEntity
 * @see com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.converter.RoomConverters
 */
@Dao
interface RoomUserDao {

    /**
     * Inserta o actualiza un usuario en la tabla `room_users`.
     *
     * @param user Entidad [RoomUserEntity] a persistir.
     * @return El `rowid` (`Long`) asignado o reemplazado en la tabla SQLite.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: RoomUserEntity): Long

    /**
     * Inserta o actualiza una orden de compra en la tabla `room_orders`.
     *
     * @param order Entidad [RoomOrderEntity] a persistir.
     * @return El `rowid` (`Long`) asignado o reemplazado en la tabla SQLite.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: RoomOrderEntity): Long

    /**
     * Observa de forma continua la tabla de usuarios en orden descendente por ID.
     *
     * @return [kotlinx.coroutines.flow.Flow] que emite la lista completa de [RoomUserEntity] ante cualquier modificación en SQLite.
     */
    @Query("SELECT * FROM room_users ORDER BY uid DESC")
    fun observeAllUsers(): Flow<List<RoomUserEntity>>

    /**
     * Elimina un registro de la tabla `room_users` asociando su clave primaria.
     *
     * @param user Entidad [RoomUserEntity] a remover.
     */
    @Delete
    suspend fun deleteUser(user: RoomUserEntity)

    /**
     * Actualiza los campos de un usuario existente en la tabla `room_users`.
     *
     * La anotación ``@Update`` localiza el registro en SQLite comparando la clave primaria (`uid`)
     * del objeto pasado. Todos los campos de la entidad sobrescribirán los valores actuales en la tabla.
     *
     * @param user Entidad [RoomUserEntity] con la clave primaria válida y los datos modificados.
     * @return El número entero (`Int`) de filas afectadas en la base de datos (debería ser `1` si
     * el usuario existe).
     */
    @Update
    suspend fun updateUser(user: RoomUserEntity): Int

    /**
     * Actualiza los campos de una orden de compra existente en la tabla `room_orders`.
     *
     * Vincula la fila en SQLite mediante el campo de clave primaria (`orderId`).
     *
     * @param order Entidad [RoomOrderEntity] con los datos actualizados a persistir.
     * @return El número entero (`Int`) de filas modificadas en SQLite.
     */
    @Update
    suspend fun updateOrder(order: RoomOrderEntity): Int

    /**
     * Consulta atómica que obtiene todos los usuarios junto con su lista de órdenes asociadas.
     *
     * Al incluir la anotación ``@Transaction``, Room asegura la consistencia de datos entre la lectura
     * de la tabla de usuarios y la tabla de órdenes.
     *
     * @return [Flow] que emite la lista de objetos de relación [UserWithOrders].
     */
    @Transaction
    @Query("SELECT * FROM room_users")
    fun getUsersWithOrders(): Flow<List<UserWithOrders>>

    /**
     * Consulta atómica que obtiene una orden específica por su ID junto a la información de su
     * usuario propietario.
     *
     * Al estar anotada con ``@Transaction``, Room ejecuta en una única transacción atómica las lecturas
     * de la tabla de órdenes (`room_orders`) y la de usuarios (`room_users`), garantizando consistencia
     * de datos durante el armado del DTO [OrderWithUser].
     *
     * @param orderId Identificador único de la orden a consultar.
     * @return La orden especificada mapeada con su usuario ([OrderWithUser]), o `null` si no existe.
     */
    @Transaction
    @Query("SELECT * FROM room_orders WHERE orderId = :orderId")
    suspend fun getOrderWithUserById(orderId: Long): OrderWithUser?

    /**
     * Consulta reactiva que obtiene el listado completo de usuarios junto con sus etiquetas asociadas
     * resolviendo una relación **Muchos a Muchos (N:M)**.
     *
     * Emite automáticamente una nueva lista cada vez que ocurren cambios en la tabla principal de
     * usuarios (`room_users`), en la tabla de etiquetas (`room_tags`) o en la tabla pivote
     * intermedia (`user_tag_cross_ref`).
     *
     * > **Nota de Transaccionalidad:**
     * > La anotación ``@Transaction`` es obligatoria para relaciones N:M. Garantiza que la lectura
     * > inicial de usuarios, la consulta a la tabla pivote intermedia y el mapeo final de las etiquetas
     * > asociadas se ejecuten como una única operación atómica dentro de SQLite.
     *
     * @return Un flujo reactivo ([Flow]) que emite la lista de DTOs [UserWithTags]
     * actualizada en tiempo real.
     *
     * @see UserWithTags
     * @see UserTagCrossRefEntity
     */
    @Transaction
    @Query("SELECT * FROM room_users")
    fun getUsersWithTags(): Flow<List<UserWithTags>>
}
