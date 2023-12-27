package com.example.lightcinema.ui.screens.admin.addSession

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lightcinema.R
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.ui.common.BaseDialog
import com.example.lightcinema.ui.theme.LightCinemaTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSessionDialog(
    viewModel: AddSessionDialogViewModel = viewModel(factory = AddSessionDialogViewModel.Factory),
    movieId: Int = -1,
    onDismissRequest: () -> Unit,
    onSuccessRequest: () -> Unit,
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val theme = MaterialTheme.colorScheme
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = null)
    var selectedDate by remember {
        mutableLongStateOf(calendar.timeInMillis)
    }
    val dateFormatter = SimpleDateFormat("EEEE, dd.MM.yyyy", Locale.getDefault())

    var showTimePicker by remember { mutableStateOf(false) }
    var selectedHour by remember {
        mutableIntStateOf(0)
    }
    var selectedMinute by remember {
        mutableIntStateOf(0)
    }
    val timePickerState = rememberTimePickerState(
        initialHour = selectedHour,
        initialMinute = selectedMinute,
        is24Hour = true
    )

    var selectedHall by remember {
        mutableIntStateOf(-1)
    }

    var expanded by remember { mutableStateOf(false) }

    var basePrice by remember {
        mutableStateOf("0")
    }

    var vipPrice by remember {
        mutableStateOf("0")
    }
    viewModel.updateHallsList()

    val halls = viewModel.hallList.collectAsState()
    val sessionResponse = viewModel.sessionResponse.collectAsState(initial = null)

    when (val response = sessionResponse.value) {
        is ApiResponse.Failure -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, response.errorMessage.toString(), Toast.LENGTH_LONG).show()
                viewModel.emitNullValue()
            }
        }

        ApiResponse.Loading -> {

        }

        is ApiResponse.Success -> {
            Log.d("Aboba2", response.toString())
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Сеанс добавлен!", Toast.LENGTH_LONG).show()
                onDismissRequest()
                onSuccessRequest()
                viewModel.emitNullValue()
            }
        }

        else -> {}
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    selectedDate = datePickerState.selectedDateMillis!!
                }) {
                    Text(text = "Подтвердить")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) {
                    Text(text = "Скрыть")
                }
            },
            colors = DatePickerDefaults.colors(containerColor = theme.surfaceTint)
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false
            )
        }
    }

    if (showTimePicker) {
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size = 12.dp)
                ),
            onDismissRequest = { showTimePicker = false }
        ) {
            Column(
                modifier = Modifier
                    .background(
                        color = Color.LightGray.copy(alpha = 0.3f)
                    )
                    .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = theme.secondary,
                        clockDialSelectedContentColor = theme.primary,
                        clockDialUnselectedContentColor = theme.primary,
                        selectorColor = theme.tertiary,
                        containerColor = theme.background,
                        timeSelectorSelectedContainerColor = theme.primary,
                        timeSelectorUnselectedContainerColor = theme.tertiary,
                        timeSelectorSelectedContentColor = theme.onPrimary,
                        timeSelectorUnselectedContentColor = theme.onTertiaryContainer
                    )
                )
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { showTimePicker = false }) {
                        Text(text = "Скрыть")
                    }
                    TextButton(
                        onClick = {
                            showTimePicker = false
                            selectedHour = timePickerState.hour
                            selectedMinute = timePickerState.minute
                        }
                    ) {
                        Text(text = "Подтвердить")
                    }
                }
            }
        }
    }

    BaseDialog(
        title = "Добавление сеанса",
        onDismissRequest = onDismissRequest,
        buttonText = "Добавить",
        onButtonClick = {
            val basePriceParse = basePrice.toIntOrNull()
            val vipPriceParse = vipPrice.toIntOrNull()

            when {
                basePriceParse == null || vipPriceParse == null -> {
                    Toast.makeText(
                        context,
                        "Введите корректные стоимости",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }

                basePriceParse !in 150..250 -> {
                    Toast.makeText(
                        context,
                        "Базовая стоимость не в отрезке [150;250]",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }

                basePriceParse >= vipPriceParse -> {
                    Toast.makeText(
                        context,
                        "Базовая стоимость должна быть меньше повышенной",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }

                else -> {
                    if (sessionResponse.value != ApiResponse.Loading) {
                        viewModel.addSession(
                            movieId,
                            selectedDate,
                            selectedHour,
                            selectedMinute,
                            selectedHall,
                            basePriceParse,
                            vipPriceParse
                        )
                    }
                }
            }


        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = dateFormatter.format(Date(selectedDate)) ?: "Выберите дату",
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = "Дата",
                        )
                    }
                },
                colors = alertDialogsTextFieldColors()
            )
            TextField(
                value = "${if (selectedHour < 10) "0$selectedHour" else "$selectedHour"}:" +
                        if (selectedMinute < 10) "0$selectedMinute" else "$selectedMinute",
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showTimePicker = true }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.clock),
                            contentDescription = "Дата",
                            modifier = Modifier.fillMaxSize(0.6f)
                        )
                    }
                },
                colors = alertDialogsTextFieldColors()
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = if (selectedHall < 0) "Выберите зал" else selectedHall.toString(),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = alertDialogsTextFieldColors(),
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    when (val hallsResponse = halls.value) {
                        is ApiResponse.Failure -> {}
                        ApiResponse.Loading -> {}
                        is ApiResponse.Success -> {
                            hallsResponse.data.halls.forEach { item ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = if (item.number < 0) "Выберите зал" else item.number.toString())
                                    },
                                    onClick = {
                                        selectedHall = item.number
                                        expanded = false
                                    }
                                )
                            }
                        }

                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Цена за место",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    OutlinedTextField(
                        value = basePrice, onValueChange = { basePrice = it },
                        singleLine = true,
                        modifier = Modifier
                            .width(115.dp)
                            .padding(0.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = alertDialogsTextFieldColors()
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Цена за VIP место",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    OutlinedTextField(
                        value = vipPrice, onValueChange = { vipPrice = it },
                        singleLine = true,
                        modifier = Modifier
                            .width(115.dp)
                            .padding(0.dp),
                        colors = alertDialogsTextFieldColors(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        shape = RoundedCornerShape(8.dp),
                    )
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun AddSessionDialogPreview(
    title: String = "Добавление сеанса",
    onDismissRequest: () -> Unit = {},
    halls: MutableState<List<Int>> = remember { mutableStateOf(listOf()) },
    addClick: () -> Unit = {},
    selectedDate: MutableLongState = mutableLongStateOf(0),
    timeText: String = "Выберите время",
    hallText: String = "Выберите зал",
    selectedHall: MutableState<Int> = mutableIntStateOf(-1),
) {
    LightCinemaTheme(darkTheme = false) {
        AddSessionDialog(
//            title = title,
            onDismissRequest = onDismissRequest,
//            halls = halls,
//            addClick = addClick,
//            selectedDate = selectedDate,
//            timeText = timeText,
//            selectedHall = selectedHall,
            onSuccessRequest = {}
        )
    }
}

@Composable
fun alertDialogsTextFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onBackground,
        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
        disabledTextColor = MaterialTheme.colorScheme.onBackground,
        errorTextColor = MaterialTheme.colorScheme.onBackground,
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
        disabledContainerColor = MaterialTheme.colorScheme.background,
        errorContainerColor = MaterialTheme.colorScheme.background,
        focusedSupportingTextColor = Color.Transparent,
        unfocusedSupportingTextColor = Color.Transparent,
        disabledSupportingTextColor = Color.Transparent,
        errorSupportingTextColor = Color.Transparent,
        focusedBorderColor = MaterialTheme.colorScheme.onBackground,
        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
        disabledBorderColor = MaterialTheme.colorScheme.onBackground,
        errorBorderColor = MaterialTheme.colorScheme.onBackground,
        focusedLabelColor = MaterialTheme.colorScheme.onBackground,
        unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
        disabledLabelColor = MaterialTheme.colorScheme.onBackground,
        errorLabelColor = MaterialTheme.colorScheme.onBackground,
        focusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
        disabledTrailingIconColor = MaterialTheme.colorScheme.onBackground,
        errorTrailingIconColor = MaterialTheme.colorScheme.onBackground,
    )
}
