package com.example.lightcinema.ui.screens.admin.add_hall

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chihsuanwu.freescroll.freeScrollWithTransformGesture
import com.chihsuanwu.freescroll.rememberFreeScrollState
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.ui.screens.admin.addSession.alertDialogsTextFieldColors

@Preview(showBackground = true)
@Composable
fun CreateHallScreen(
    viewModel: CreateHallViewModel = viewModel(factory = CreateHallViewModel.Factory),
    upPress: () -> Unit = {},
) {

    val creatingResponse by viewModel.creatingResponse.collectAsState(initial = null)
    val context = LocalContext.current
    var rowCount by remember {
        mutableStateOf("0")
    }
    var seatsInRowCount by remember {
        mutableStateOf("0")
    }
    var hallNumber by remember {
        mutableStateOf("0")
    }
    var seatsCollection by remember {
        mutableStateOf<SeatsModelCollection?>(null)
    }

    when (val response = creatingResponse) {
        is ApiResponse.Failure -> {
            Log.d(
                "Aboba",
                (creatingResponse as ApiResponse.Failure).errorMessage.toString()
            )
            LaunchedEffect(Unit) {
                Toast.makeText(context, response.errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        ApiResponse.Loading -> {}

        is ApiResponse.Success -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Зал успешно добавлен!", Toast.LENGTH_LONG).show()
            }
            upPress()
        }

        null -> {}
    }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 5.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            if (seatsCollection == null) {
                                Toast.makeText(
                                    context,
                                    "Сгенерируйте модель зала",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            } else {
                                val hall = hallNumber.toIntOrNull()
                                if (hall == null) {
                                    Toast.makeText(
                                        context,
                                        "Введите корректный номер зала",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else if (seatsCollection == null) {
                                    Toast.makeText(
                                        context,
                                        "Создайте зал",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    viewModel.createHall(hall, seatsCollection!!)
                                }

                            }


                        },
                        shape = RoundedCornerShape(
                            16.dp
                        ),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.onSecondary
                        ),
                        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                    ) {
                        if (creatingResponse is ApiResponse.Loading) {
                            CircularProgressIndicator()
                        } else {
                            Text(
                                text = "Создать зал",
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }) { it ->
        Box(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 4.dp, horizontal = 10.dp)
            ) {

                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(text = "Конструктор зала", fontSize = 28.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Номер зала: ")
                        OutlinedTextField(
                            value = hallNumber,
                            onValueChange = { hallNumber = it },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = alertDialogsTextFieldColors(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .width(60.dp)
                                .padding(0.dp),
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Количество рядов: ")
                        OutlinedTextField(
                            value = rowCount,
                            onValueChange = { rowCount = it },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = alertDialogsTextFieldColors(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .width(60.dp)
                                .padding(0.dp),
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Количество мест в ряде: ")
                        OutlinedTextField(
                            value = seatsInRowCount,
                            onValueChange = { seatsInRowCount = it },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = alertDialogsTextFieldColors(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .width(60.dp)
                                .padding(0.dp),
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            val seatsInRowCountParse = seatsInRowCount.toIntOrNull()
                            val rowCountParse = rowCount.toIntOrNull()

                            when {
                                rowCountParse == null || seatsInRowCountParse == null -> {
                                    Toast.makeText(
                                        context,
                                        "Введите корректные параметры зала!",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                                rowCountParse !in 7..15 -> {
                                    Toast.makeText(
                                        context,
                                        "Количество рядов должно быть в отрезке [7;15]",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                                seatsInRowCountParse !in 5..25 -> {
                                    Toast.makeText(
                                        context,
                                        "Количество мест в ряде должно быть в отрезке [5;25]",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                                else -> {
                                    seatsCollection = SeatsModelCollection(
                                        Array(rowCountParse.toInt()) { row ->
                                            Array(seatsInRowCountParse.toInt()) { seat ->
                                                SeatModel(row + 1, seat + 1, mutableStateOf(false))
                                            }
                                        }
                                    )
                                }
                            }
                        },
                        shape = RoundedCornerShape(
                            16.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Text(text = "Сгенерировать макет зала")
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .heightIn(0.dp, 600.dp)
                    ) {

                    }
                    seatsCollection?.seats?.let {
                        ScalableSeatMatrix(it) {
                            it.isIncreasedPrice.value = !it.isIncreasedPrice.value
                        }
                    }

                }
            }
        }
    }

}

@Composable
fun ScalableSeatMatrix(seatsMatrix: Array<Array<SeatModel>>, onSeatClick: (SeatModel) -> Unit) {
    val freeScrollState = rememberFreeScrollState()
    var scale by remember { mutableFloatStateOf(1f) }
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .freeScrollWithTransformGesture(
                state = freeScrollState,
                onGesture = { centroid: Offset,
                              pan: Offset,
                              zoom: Float,
                              rotation: Float ->
                    scale *= zoom
                }
            )
            .graphicsLayer(scaleX = scale, scaleY = scale)
    ) {
        seatsMatrix.forEach { row ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                row.forEach { seat ->
                    Seat(seat, 30.dp, 30.dp, onSeatClick)
                }
                Box(contentAlignment = Alignment.Center, modifier = Modifier.width(40.dp)) {
                    Text(
                        text = row.firstOrNull()?.row?.toString() ?: "",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .background(MaterialTheme.colorScheme.background)
//                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
//                .padding(vertical = 5.dp)
//                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier) {
                BoxSeat(
                    "",
                    MaterialTheme.colorScheme.background,
                    MaterialTheme.colorScheme.onTertiaryContainer,
                    Color.Transparent,
                    20.dp,
                    20.dp,
                    {}
                )
                Text(
                    text = "Место с обычным коэффициентом",
                    color = MaterialTheme.colorScheme.tertiaryContainer
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier) {
                BoxSeat(
                    "",
                    MaterialTheme.colorScheme.secondary,
                    Color.Transparent,
                    Color.Transparent,
                    20.dp,
                    20.dp,
                    {}
                )
                Text(
                    text = "Место с увеличенным коэффициентом",
                    color = MaterialTheme.colorScheme.tertiaryContainer
                )
            }
        }
    }
}

@Composable
fun Seat(
    seat: SeatModel,
    height: Dp,
    width: Dp,
    onSeatClick: (SeatModel) -> Unit
) {
    val backgroundColor = when {

        seat.isIncreasedPrice.value -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.background
    }
    val textColor = when {
        seat.isIncreasedPrice.value -> MaterialTheme.colorScheme.onSecondary
        else -> MaterialTheme.colorScheme.onBackground
    }
    BoxSeat(
        seat.number.toString(),
        backgroundColor,
        MaterialTheme.colorScheme.onTertiaryContainer,
        textColor,
        height,
        width
    ) { onSeatClick(seat) }
}

@Composable
fun BoxSeat(
    value: String,
    backgroundColor: Color,
    borderColor: Color,
    textColor: Color,
    height: Dp,
    width: Dp,
    onSeatClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(8.dp)
            .border(
                2.dp, borderColor, RoundedCornerShape(7.dp)
            )
            .height(height)
            .width(width)
            .background(
                color = backgroundColor, RoundedCornerShape(7.dp)
            )
            .clickable {
                onSeatClick()
            }

    ) {
        Text(
            text = value, textAlign = TextAlign.Center, color = textColor, modifier = Modifier
                .fillMaxWidth()
        )
    }
}
