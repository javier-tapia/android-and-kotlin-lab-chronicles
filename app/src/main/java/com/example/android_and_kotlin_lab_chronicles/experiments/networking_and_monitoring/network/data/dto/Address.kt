package com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class Address(
    @SerialName("city")
    val city: String? = null,
    @SerialName("geo")
    val geo: Geo? = null,
    @SerialName("street")
    val street: String? = null,
    @SerialName("suite")
    val suite: String? = null,
    @SerialName("zipcode")
    val zipcode: String? = null
)