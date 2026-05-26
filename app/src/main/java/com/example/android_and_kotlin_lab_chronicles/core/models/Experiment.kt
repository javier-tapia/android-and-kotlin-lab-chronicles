package com.example.android_and_kotlin_lab_chronicles.core.models

import androidx.activity.ComponentActivity

data class Experiment(
    val title: String,
    val topic: ExperimentTopic,
    val target: Class<out ComponentActivity>,
)

enum class ExperimentTopic(val topicName: String) {
    FUNDAMENTALS("Android Fundamentals"),
    DI("Inyección de Dependencias"),
    GEOLOCATION("Geolocation & Maps"),
    INTEGRATIONS("Integrations"),
    COMPOSE("Jetpack Compose"),
    KOTLIN("Kotlin Pure"),
    MULTIMEDIA("Multimedia & Streaming"),
    NETWORKING("Networking & Monitoring"),
}
