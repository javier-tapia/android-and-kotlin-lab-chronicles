package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.RoomOrderEntity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.RoomUserEntity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.RoomLabDatabase
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.dao.RoomUserDao
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.converter.RoomConverters

/**
 * ### Modelo de Relación Uno a Muchos (1:N)
 *
 * Objeto de transferencia de datos (POJO / DTO) utilizado por Room para mapear y reconstruir
 * una relación relacional **Uno a Muchos** (Un usuario posee cero, una o múltiples órdenes de compra).
 *
 * | **Criterio** | **Padre (Lado 1)** | **Hija (Lado N)** |
 * | --- | --- | --- |
 * | **Identificador** | Tiene Clave Primaria (*Primary Key* / `PK`) | Contiene la Clave Foránea (*Foreign Key* / `FK`) apuntando al Padre |
 * | **Dependencia** | Existe de forma independiente | Depende de la existencia del Padre para mantener sentido relacional |
 * | **Representación en Room (1:N)** | Objeto único o individual (`@Embedded val user: RoomUserEntity`) | Colección / Lista de objetos (`@Relation ... val orders: List<RoomOrderEntity>`) |
 *
 * Permite realizar consultas relacionales y recuperar el objeto [user] junto a su lista de [orders]
 * mediante ``@Embedded`` y ``@Relation``, independientemente de que exista o no una restricción
 * de clave foránea en SQLite a nivel de entidad (ver uso de `foreignKeys` en [RoomOrderEntity]).
 *
 * #### ¿Cómo identificar que modela una relación 1:N?
 *
 * 1. **Tipo de Colección en la Entidad Hija**: La propiedad [orders] se declara como una lista
 * (`List<RoomOrderEntity>`). Si la relación fuese Uno a Uno (1:1), se declararía un único objeto
 * individual (`RoomOrderEntity?`).
 * 2. **Emparejamiento de Columnas en `@Relation`**:
 *    - `parentColumn = "uid"`: Corresponde al campo de clave primaria en la entidad padre ([RoomUserEntity]).
 *    - `entityColumn = "userId"`: Corresponde al campo de clave foránea en la entidad hija ([RoomOrderEntity]).
 *
 * *For dummies...*:
 * ```kotlin
 * @Relation(
 *     parentColumn = "uid",     // "Room, buscá este campo en la tabla padre..."
 *     entityColumn = "userId"   // "... y matchealo con este campo en la tabla hija"
 * )
 * ```
 *
 * #### Detalle de Anotaciones
 * - **`@Embedded`**: Aplana los campos de la entidad padre ([RoomUserEntity]) e instruye a Room
 * para que los lea directamente dentro de la misma consulta relacional, instanciando la propiedad `user`.
 * - **`@Relation`**: Le indica a Room que ejecute de manera secundaria o mediante un `JOIN` automático
 *   la recolección de todos los registros de [RoomOrderEntity] cuyo valor en `userId` coincida con el
 *   `uid` del usuario padre.
 *
 * > **Nota de Transaccionalidad:**
 * > Dado que la resolución de [UserWithOrders] requiere que Room ejecute múltiples lecturas sobre
 * > SQLite (una para la tabla `room_users` y otra para `room_orders`), las consultas DAO que
 * > retornen esta clase deben marcarse de manera obligatoria con la anotación ``@Transaction``
 * > para garantizar consistencia de lectura.
 *
 * @property user Instancia embebida de la entidad padre [RoomUserEntity].
 * @property orders Lista que almacena la colección de entidades hijas [RoomOrderEntity] asociadas al usuario.
 *
 * @see RoomLabDatabase
 * @see RoomUserDao
 * @see RoomConverters
 * @see OrderWithUser
 */
data class UserWithOrders(
    @Embedded val user: RoomUserEntity,
    @Relation(
        parentColumn = "uid",
        entityColumn = "userId"
    )
    val orders: List<RoomOrderEntity>
)
