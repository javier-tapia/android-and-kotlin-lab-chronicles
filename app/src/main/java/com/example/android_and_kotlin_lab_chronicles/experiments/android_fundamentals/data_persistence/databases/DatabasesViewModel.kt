package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.RoomOrderEntity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.dao.RoomUserDao
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.entity.RoomUserEntity
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.data_persistence.databases.data.local.model.UserWithOrders
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * El ViewModel es responsable de orquestar la lógica de negocio de la pantalla de persistencia
 * y gestionar las operaciones asíncronas sobre la base de datos local usando [RoomUserDao].
 *
 * Las dependencias son proveídas automáticamente por Hilt mediante la anotación ``@HiltViewModel``.
 *
 * @property userDao Contrato DAO para realizar transacciones de persistencia con Room.
 */
@HiltViewModel
class DatabasesViewModel @Inject constructor(
    private val userDao: RoomUserDao
) : ViewModel() {

    /**
     * Flujo reactivo de datos que expone la lista actualizada de usuarios almacenados en Room.
     *
     * Se convierte a [StateFlow] mediante [stateIn] asociándolo al [viewModelScope].
     * La estrategia [SharingStarted.WhileSubscribed] con un `stopTimeoutMillis` de 5000 ms cancela
     * la recolección si la vista entra en segundo plano (como una rotación de pantalla) optimizando
     * recursos de base de datos.
     */
    val usersState: StateFlow<List<RoomUserEntity>> = userDao.observeAllUsers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /**
     * Flujo reactivo que expone la lista de usuarios junto con sus órdenes de compra
     * asociadas ([UserWithOrders]).
     *
     * Consulta la relación 1:N utilizando la anotación `@Transaction` en el DAO para garantizar la
     * consistencia atómica de lectura entre las tablas `room_users` y `room_orders`.
     *
     * Se expone como un [StateFlow] mediante [stateIn] vinculado al [viewModelScope].
     */
    val usersWithOrdersState: StateFlow<List<UserWithOrders>> = userDao.getUsersWithOrders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /**
     * Inserta un nuevo usuario en la base de datos de manera asíncrona dentro del hilo secundario
     * de la corrutina.
     *
     * @param user Instancia de [RoomUserEntity] a persistir.
     */
    fun insertUser(user: RoomUserEntity) {
        viewModelScope.launch {
            userDao.insertUser(user)
        }
    }

    /**
     * Elimina un usuario existente de la base de datos.
     *
     * @param user Instancia de [RoomUserEntity] a remover.
     */
    fun deleteUser(user: RoomUserEntity) {
        viewModelScope.launch {
            userDao.deleteUser(user)
        }
    }

    /**
     * Actualiza los datos de un usuario existente en la tabla `room_users`.
     *
     * Localiza el registro en SQLite emparejando la clave primaria (`uid`) del objeto recibido.
     *
     * @param user Instancia de [RoomUserEntity] con los datos modificados a sobrescribir.
     */
    fun updateUser(user: RoomUserEntity) {
        viewModelScope.launch {
            userDao.updateUser(user)
        }
    }

    /**
     * Inserta o actualiza una nueva orden de compra asociada a un usuario en la base de datos.
     *
     * Ejecuta la transacción de forma asíncrona dentro del [viewModelScope] delegando el trabajo
     * al despachador de I/O gestionado por Room.
     *
     * @param order Instancia de [RoomOrderEntity] a persistir.
     */
    fun insertOrder(order: RoomOrderEntity) {
        viewModelScope.launch {
            userDao.insertOrder(order)
        }
    }

    /**
     * Actualiza los datos de una orden de compra existente en la tabla `room_orders`.
     *
     * Localiza el registro en SQLite emparejando la clave primaria (`orderId`) del objeto recibido.
     *
     * @param order Instancia de [RoomOrderEntity] con los datos modificados a sobrescribir.
     */
    fun updateOrder(order: RoomOrderEntity) {
        viewModelScope.launch {
            userDao.updateOrder(order)
        }
    }
}
