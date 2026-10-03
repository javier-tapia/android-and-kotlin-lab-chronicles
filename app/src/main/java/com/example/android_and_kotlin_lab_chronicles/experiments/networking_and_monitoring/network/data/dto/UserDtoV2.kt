package com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class UserDtoV2(
    @SerialName("address")
    val address: Address? = null,
    @SerialName("company")
    val company: Company? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("phone")
    val phone: String? = null,
    @SerialName("username")
    val username: String? = null,
    @SerialName("website")
    val website: String? = null
)