package com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class Geo(
    @SerialName("lat")
    val lat: String? = null,
    @SerialName("lng")
    val lng: String? = null
)