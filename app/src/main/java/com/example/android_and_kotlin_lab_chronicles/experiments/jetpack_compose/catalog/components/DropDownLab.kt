package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase

/**
 * Experimentos con menús desplegables (DropdownMenu y ExposedDropdownMenuBox).
 */
@Composable
fun DropDownLab() {
    SamplesShowcase(
        { DropdownMenuSample() },
        { ExposedDropdownMenuBoxSample() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuSample() {
    var selectedText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val desserts = listOf(
        "Helado",
        "Chocolate",
        "Café",
        "Fruta",
        "Yerba",
        "Shampoo"
    )

    Column(Modifier.padding(20.dp)) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            enabled = false,
            readOnly = true,
            modifier = Modifier
                .clickable { expanded = true }
                .fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            desserts.forEach { dessert ->
                DropdownMenuItem(
                    text = { Text(dessert) },
                    onClick = {
                        expanded = false
                        selectedText = dessert
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuBoxSample(modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selection by rememberSaveable { mutableStateOf("") }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryEditable,
                    enabled = true
                )
                .fillMaxWidth(),
            readOnly = true,
            value = selection,
            onValueChange = {},
            label = { Text("Idioma") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Opción 1") },
                onClick = {
                    selection = "Opción 1"
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Opción 2") },
                onClick = {
                    selection = "Opción 2"
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Opción 3") },
                onClick = {
                    selection = "Opción 3"
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Opción 4") },
                onClick = {
                    selection = "Opción 4"
                    expanded = false
                }
            )
        }
    }
}
