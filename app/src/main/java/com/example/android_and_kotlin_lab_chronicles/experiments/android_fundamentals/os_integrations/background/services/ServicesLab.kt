package com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services.components.BoundServiceCard
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services.components.ForegroundServiceCard
import com.example.android_and_kotlin_lab_chronicles.experiments.android_fundamentals.os_integrations.background.services.components.HybridServiceCard

@Composable
fun ServicesLab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "ServicesLab",
            style = MaterialTheme.typography.titleLarge
        )

        // --- SECCIÓN 1: FOREGROUND SERVICE (MUSIC PLAYER) ---
        ForegroundServiceCard()

        // --- SECCIÓN 2: BOUND SERVICE (FILE DOWNLOADER) ---
        BoundServiceCard()

        // --- SECCIÓN 3: HYBRID (STARTED & BOUND) SERVICE (MUSIC PLAYER) ---
        HybridServiceCard()
    }
}
