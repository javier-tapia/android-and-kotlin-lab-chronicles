package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase

/**
 * Experimentos con la NavigationBar (así se llama la Bottom Navigation en Material 3).
 */
@Composable
fun NavigationBarLab() {
    SamplesShowcase(
        { NavigationBarSample() }
    )
}

@Composable
fun NavigationBarSample() {
    var index by remember { mutableIntStateOf(0) }

    NavigationBar(
        containerColor = Color.Red,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            selected = index == 0,
            onClick = { index = 0 },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "home"
                )
            },
            label = { Text(text = "Home") }
        )

        NavigationBarItem(
            selected = index == 1,
            onClick = { index = 1 },
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "fav"
                )
            },
            label = { Text(text = "FAV") }
        )

        NavigationBarItem(
            selected = index == 2,
            onClick = { index = 2 },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "person"
                )
            },
            label = { Text(text = "Person") }
        )
    }
}
