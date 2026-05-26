package com.example.android_and_kotlin_lab_chronicles.experiments.jetpack_compose.catalog.components

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.android_and_kotlin_lab_chronicles.R
import com.example.android_and_kotlin_lab_chronicles.core.SamplesShowcase
import java.util.Calendar

/**
 * Experimentos con diálogos personalizados.
 * El ``AlertDialog`` ya implementa por defecto un título, una descripción y dos botones.
 */
@Composable
fun DialogsLab() {
    SamplesShowcase(
        { CustomDialogSample() },
        { ConfirmationDialogSample() },
        { AlertDialogSample() },
        { DatePickerSample() },
        { TimePickerSample() },
    )
}

@Composable
fun CustomDialogSample() {
    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showDialog = true }
    ) {
        Text(
            text = "Mostrar Custom Dialog"
        )
    }

    if (showDialog) {
        Dialog(
            // Esta lambda es el "contrato" con el sistema operativo
            // Se ejecuta cuando el SO detecta que el usuario quiere salir
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Column(
                Modifier
                    .background(Color.White)
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                CustomTitleDialog("Set backup account")
                AccountItem("ejemplo1@gmail.com", R.drawable.avatar)
                AccountItem("ejemplo2@gmail.com", R.drawable.avatar)
                AccountItem("Añadir nueva cuenta", R.drawable.add)
            }
        }
    }
}

@Composable
fun ConfirmationDialogSample() {
    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showDialog = true }
    ) {
        Text(
            text = "Mostrar Confirmation Dialog"
        )
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                CustomTitleDialog(text = "Phone ringtone", modifier = Modifier.padding(24.dp))
                HorizontalDivider(Modifier.fillMaxWidth(), 4.dp, Color.LightGray)
                var status by remember { mutableStateOf("") }
                CustomRadioButtonList(status) { status = it }
                HorizontalDivider(Modifier.fillMaxWidth(), 4.dp, Color.LightGray)
                Row(
                    Modifier
                        .align(Alignment.End)
                        .padding(8.dp)
                ) {
                    TextButton(onClick = { }) {
                        Text(text = "CANCEL")
                    }
                    TextButton(onClick = { }) {
                        Text(text = "OK")
                    }
                }
            }
        }
    }
}

@Composable
fun AlertDialogSample() {
    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showDialog = true }
    ) {
        Text(
            text = "Mostrar Alert Dialog"
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Título") },
            text = { Text(text = "Hola, soy una descripción") },
            confirmButton = {
                TextButton(onClick = { }) {
                    Text(text = "ConfirmButton")
                }
            },
            dismissButton = {
                TextButton(onClick = { }) {
                    Text(text = "DismissButton")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerSample() {
    val context = LocalContext.current
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val calendar = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, 1)
        set(Calendar.MONTH, Calendar.JANUARY)
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = calendar.timeInMillis,
        initialDisplayedMonthMillis = calendar.timeInMillis,
        yearRange = 2024..2025,
        initialDisplayMode = DisplayMode.Input, // Por defecto, es 'DisplayMode.Picker'
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val filterCalendar = Calendar.getInstance().apply {
                    timeInMillis = utcTimeMillis
                }
                val day = filterCalendar.get(Calendar.DAY_OF_MONTH)
                return day % 2 == 0
            }
        }
    )

    Button(
        onClick = { showDialog = true }
    ) {
        Text(
            text = "Mostrar Date Picker"
        )
    }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        val result = datePickerState.selectedDateMillis
                        if (result != null) {
                            val newCalendar = Calendar.getInstance().apply { timeInMillis = result }
                            val day = newCalendar.get(Calendar.DAY_OF_MONTH)
                            val month = newCalendar.get(Calendar.MONTH) + 1
                            Toast.makeText(
                                context,
                                "Fecha seleccionada: Día $day del mes $month",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ) {
                    Text("Confirmar")
                }
            }
        ) {
            DatePicker(datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerSample() {
    var showDialog by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = 7,
        initialMinute = 33,
        is24Hour = false
    )

    Button(
        onClick = { showDialog = true }
    ) {
        Text(
            text = "Mostrar Time Picker"
        )
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false }
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = Color.Red,
                        clockDialSelectedContentColor = Color.Red,
                        clockDialUnselectedContentColor = Color.White,
                        selectorColor = Color.White,
                        periodSelectorBorderColor = Color.Red,
                        periodSelectorUnselectedContentColor = Color.Red,
                        periodSelectorUnselectedContainerColor = Color.White,
                        periodSelectorSelectedContainerColor = Color.Red,
                        periodSelectorSelectedContentColor = Color.White,
                        timeSelectorUnselectedContentColor = Color.Red,
                        timeSelectorUnselectedContainerColor = Color.White,
                        timeSelectorSelectedContainerColor = Color.Red,
                        timeSelectorSelectedContentColor = Color.White,
                    ),
                    layoutType = TimePickerLayoutType.Vertical
                )
            }
        }
    }
}

@Composable
private fun CustomTitleDialog(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        modifier = modifier.padding(bottom = 12.dp)
    )
}

@Composable
private fun CustomRadioButtonList(name: String, onItemSelected: (String) -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        Row {
            RadioButton(
                selected = name == "Javi",
                onClick = { onItemSelected("Javi") }
            )
            Text(text = "Javi")
        }
        Row {
            RadioButton(
                selected = name == "Nati",
                onClick = { onItemSelected("Nati") }
            )
            Text(text = "Nati")
        }
        Row {
            RadioButton(
                selected = name == "Branca",
                onClick = { onItemSelected("Branca") }
            )
            Text(text = "Branca")
        }
        Row {
            RadioButton(
                selected = name == "Coca",
                onClick = { onItemSelected("Coca") }
            )
            Text(text = "Coca")
        }
    }
}

@Composable
private fun AccountItem(email: String, @DrawableRes drawable: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = drawable),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(8.dp)
                .size(40.dp)
                .clip(CircleShape)
        )

        Text(text = email, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(8.dp))
    }
}
