package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity

import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.model.UserWithTags
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ### Entidad Etiqueta (*Tag*)
 *
 * Mapea la tabla `room_tags` dentro del esquema relacional de SQLite.
 *
 * Representa una categoría o etiqueta reutilizable dentro del sistema (por ejemplo, "VIP",
 * "Frecuente", "Soporte"). No almacena referencias directas a [RoomUserEntity], ya que su
 * relación es **Muchos a Muchos (N:M)** y la vinculación se realiza a través de la tabla
 * pivote [UserTagCrossRefEntity].
 *
 * @property tagId Clave primaria autogenerada para identificar de forma única la etiqueta.
 * @property name Nombre visible o categoría de la etiqueta (por ejemplo, "VIP").
 *
 * @see UserTagCrossRefEntity
 * @see UserWithTags
 */
@Entity(tableName = "room_tags")
data class RoomTagEntity(
    @PrimaryKey(autoGenerate = true)
    val tagId: Long = 0,
    val name: String
)
