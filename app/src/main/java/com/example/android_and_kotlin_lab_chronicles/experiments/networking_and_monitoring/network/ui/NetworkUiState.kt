package com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.ui

import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.dto.UserDto
import com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.NetworkLab

/**
 * Estado inmutable de la pantalla [NetworkLab] para desacoplar la UI de la lógica de red.
 */
sealed interface NetworkUiState {
    data object Idle : NetworkUiState
    data object Loading : NetworkUiState
    data class Success(
        val user: UserDto,
        val latencyMs: Long,
        val clientName: String
    ) : NetworkUiState
    data class Error(
        val message: String,
        val latencyMs: Long? = null
    ) : NetworkUiState
}
