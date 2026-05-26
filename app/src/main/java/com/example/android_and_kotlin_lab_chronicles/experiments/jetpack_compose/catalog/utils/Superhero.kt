package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.utils

import androidx.annotation.DrawableRes

data class Superhero(
    var superheroName: String,
    var realName: String,
    var publisher: String,
    @DrawableRes var photo: Int
)
