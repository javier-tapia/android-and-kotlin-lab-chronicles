package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity

import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.model.UserWithTags
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * ### Entidad Pivote / Tabla Intermedia de Cruce (*CrossRef*)
 *
 * Mapea la tabla `user_tag_cross_ref` en SQLite para resolver la relación **Muchos a Muchos (N:M)**
 * entre usuarios ([RoomUserEntity]) y etiquetas ([RoomTagEntity]).
 *
 * Actúa como la **entidad hija de ambas tablas principales**, almacenando las parejas de
 * identificadores que representan la asociación entre un usuario específico y una etiqueta asignada.
 *
 * #### Características Relacionales y de Rendimiento Clave:
 * 1. **Clave Primaria Compuesta (`primaryKeys = ["uid", "tagId"]`)**: Previene duplicados a nivel
 *    de base de datos (un mismo usuario no pueda tener asignada la misma etiqueta más de una vez)
 *    y crea automáticamente un índice implícito ordenado por `(uid, tagId)`.
 * 2. **Integridad Referencial en Cascada (`onDelete = ForeignKey.CASCADE`)**:
 *    - Si se elimina un usuario ([RoomUserEntity]), SQLite borra automáticamente todos sus
 *    registros asociados.
 *    - Si se elimina una etiqueta ([RoomTagEntity]), SQLite remueve automáticamente sus vínculos
 *    en esta tabla sin afectar la entidad principal del usuario.
 * 3. **Índice Secundario (`indices = [Index(value = ["tagId"])]`)**:
 *    Dado que la clave primaria compuesta cubre optimizadamente las búsquedas que inician por `uid`,
 *    se declara este índice adicional sobre [tagId] para acelerar la resolución inversa en el
 *    `Junction` de Room cuando se consultan etiquetas o se eliminan registros desde el lado de [RoomTagEntity].
 *
 * @property uid Clave foránea (*Foreign Key*) que referencia a la clave primaria `uid` de [RoomUserEntity].
 * @property tagId Clave foránea (*Foreign Key*) que referencia a la clave primaria `tagId` de [RoomTagEntity].
 *
 * @see RoomUserEntity
 * @see RoomTagEntity
 * @see UserWithTags
 */
@Entity(
    tableName = "user_tag_cross_ref",
    primaryKeys = ["uid", "tagId"],
    foreignKeys = [
        ForeignKey(
            entity = RoomUserEntity::class,
            parentColumns = ["uid"],
            childColumns = ["uid"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RoomTagEntity::class,
            parentColumns = ["tagId"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    // Acelera consultas inversas y resoluciones mediante 'Junction'
    indices = [Index(value = ["tagId"])]
)
data class UserTagCrossRefEntity(
    val uid: Long,
    val tagId: Long
)
