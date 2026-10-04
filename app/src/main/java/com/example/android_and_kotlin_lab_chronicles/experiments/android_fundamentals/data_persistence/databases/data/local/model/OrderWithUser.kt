package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.RoomOrderEntity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.RoomUserEntity

/**
 * ### Modelo de Relación Muchos a Uno (N:1)
 *
 * Representa una Orden de compra junto a su único Usuario propietario.
 *
 * @see UserWithOrders
 */
data class OrderWithUser(
    // 1. La entidad "Muchos" (Hija) se coloca como @Embedded
    @Embedded val order: RoomOrderEntity,

    // 2. La entidad "Uno" (Padre) se recupera mediante @Relation
    @Relation(
        parentColumn = "userId", // Campo FK que está DENTRO de la tabla de la orden (RoomOrderEntity)
        entityColumn = "uid"     // Campo PK que está DENTRO de la tabla del usuario (RoomUserEntity)
    )
    val user: RoomUserEntity // Objeto único (NO es una lista, porque una orden pertenece a UN solo usuario)
)
