package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.RoomTagEntity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.RoomUserEntity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.UserTagCrossRefEntity

/**
 * ### Modelo de Relación Muchos a Muchos (N:M)
 *
 * Objeto de transferencia de datos (POJO / DTO) utilizado por Room para mapear y reconstruir
 * una relación relacional **Muchos a Muchos** entre usuarios y etiquetas (un usuario puede
 * poseer múltiples etiquetas y una misma etiqueta puede estar asignada a múltiples usuarios).
 *
 * Permite recuperar el objeto [user] junto a su colección de [tags] asociadas mediante
 * la especificación de una tabla intermedia o pivote en el parámetro [Junction] de ``@Relation``.
 *
 * | **Criterio** | **Entidades Principales (A y B)** | **Tabla Pivote Intermedia (CrossRef)** |
 * | --- | --- | --- |
 * | **Identificador** | Tienen sus propias Claves Primarias individuales (`PK`) | Contiene una **Clave Primaria compuesta** formada por las Claves Foráneas (`FK`) de ambos lados |
 * | **Dependencia** | Existen de forma totalmente independiente entre sí | Es la entidad hija de ambas; depende de que existan tanto el registro A como el B para guardar la unión |
 * | **Representación en Room (N:M)** | Clases `@Entity` estándar (`RoomUserEntity`, `RoomTagEntity`) | Clase `@Entity` declarada dentro del parámetro `Junction` en la anotación `@Relation` (`associateBy = Junction(...)`) |
 *
 *
 * #### ¿Cómo funciona la resolución en Room?
 *
 * 1. **Entidad Embebida (`@Embedded`)**: Room lee el registro principal de la tabla de
 * usuarios ([RoomUserEntity]).
 * 2. **Unión vía Tabla Pivote (`Junction`)**: En lugar de vincular directamente las claves primaria y
 *    foránea entre dos tablas, Room consulta la tabla de cruce ([UserTagCrossRefEntity]) para
 *    determinar qué identificadores `tagId` están asociados al `uid` del usuario.
 * 3. **Mapeo de Colección (`@Relation`)**: Trae la lista de entidades [RoomTagEntity] cuyos `tagId`
 *    coincidan con los pares encontrados en la tabla pivote.
 *
 * *For dummies...*:
 * ```kotlin
 * @Relation(
 *     parentColumn = "uid",                                  // PK en RoomUserEntity
 *     entityColumn = "tagId",                                // PK en RoomTagEntity
 *     associateBy = Junction(UserTagCrossRefEntity::class)  // Tabla pivote que guarda las parejas (uid, tagId)
 * )
 * ```
 *
 * > **Nota de Transaccionalidad:**
 * > La resolución de este DTO implica consultar tres tablas distintas (`room_users`,
 * > `user_tag_cross_ref` y `room_tags`). Por ello, cualquier función DAO que retorne [UserWithTags]
 * > debe declararse obligatoriamente con la anotación ``@Transaction``.
 *
 * @property user Instancia embebida de la entidad principal [RoomUserEntity].
 * @property tags Colección de etiquetas ([RoomTagEntity]) vinculadas al usuario a través de la tabla pivote.
 *
 * @see RoomUserEntity
 * @see RoomTagEntity
 * @see UserTagCrossRefEntity
 */
data class UserWithTags(
    @Embedded val user: RoomUserEntity,
    @Relation(
        parentColumn = "uid",
        entityColumn = "tagId",
        associateBy = Junction(UserTagCrossRefEntity::class)
    )
    val tags: List<RoomTagEntity>
)
