package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity

import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.model.UserWithOrders
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * ### Entidad Orden de Compra
 *
 * Mapea la tabla `room_orders` dentro del esquema relacional de SQLite.
 *
 * **Nota de Integridad y Rendimiento**:
 * - **Integridad Referencial (`foreignKeys`)**: Declara la relación con la tabla padre ([RoomUserEntity])
 *   para aplicar eliminación en cascada (`CASCADE`) a nivel de SQLite cuando se borra un usuario.
 * - **Optimización de Consultas (`indices`)**: La inclusión de un índice explícito en la clave foránea
 *   [userId] evita el escaneo completo de la tabla (*full table scan*) cada vez que SQLite valida o
 *   actualiza registros del usuario padre.
 *
 * @property orderId Clave primaria autogenerada para la orden.
 * @property userId Clave foránea (*Foreign Key*) que vincula la orden con la columna `uid` de [RoomUserEntity].
 * @property amount Monto numérico de la transacción.
 *
 * @see RoomUserEntity
 * @see UserWithOrders
 */
@Entity(
    tableName = "room_orders",
    foreignKeys = [
        ForeignKey(
            entity = RoomUserEntity::class,
            parentColumns = ["uid"],
            childColumns = ["userId"],
            // Al borrar el usuario, SQLite elimina también todas sus órdenes
            onDelete = ForeignKey.CASCADE
        )
    ],
    // Evita full table scans al consultar u operar por el usuario padre
    indices = [Index(value = ["userId"])]
)
data class RoomOrderEntity(
    @PrimaryKey(autoGenerate = true)
    val orderId: Long = 0,
    val userId: Long,
    val amount: Double
)
