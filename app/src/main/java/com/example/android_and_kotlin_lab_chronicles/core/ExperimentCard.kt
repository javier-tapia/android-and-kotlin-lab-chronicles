package com.example.android_and_kotlin_lab_chronicles.core

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.core.models.Experiment

@Composable
fun ExperimentCard(exp: Experiment) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { context.startActivity(Intent(context, exp.target)) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(exp.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
