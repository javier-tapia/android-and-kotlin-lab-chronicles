package com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.google.gson.annotations.SerializedName

/**
 * Modela la respuesta del API con las anotaciones de ``@SerialName`` y ``@SerializedName`` para
 * que sea compatible tanto con **Kotlinx Serialization** como con **Gson**.
 */
@Serializable
data class UserDto(
    @SerialName("id")
    @SerializedName("id")
    val id: Int,

    @SerialName("name")
    @SerializedName("name")
    val name: String,

    @SerialName("username")
    @SerializedName("username")
    val username: String,

    @SerialName("email")
    @SerializedName("email")
    val email: String,

    @SerialName("phone")
    @SerializedName("phone")
    val phone: String? = null,

    @SerialName("website")
    @SerializedName("website")
    val website: String? = null
)
