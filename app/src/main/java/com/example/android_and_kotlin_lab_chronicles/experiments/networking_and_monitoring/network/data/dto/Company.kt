package com.example.android_and_kotlin_lab_chronicles.experiments.networking_and_monitoring.network.data.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class Company(
    @SerialName("bs")
    val bs: String? = null,
    @SerialName("catchPhrase")
    val catchPhrase: String? = null,
    @SerialName("name")
    val name: String? = null
)