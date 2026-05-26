package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.utils

data class CheckInfo(
    val title: String,
    var selected: Boolean = false,
    var onCheckedChange: (Boolean) -> Unit
)