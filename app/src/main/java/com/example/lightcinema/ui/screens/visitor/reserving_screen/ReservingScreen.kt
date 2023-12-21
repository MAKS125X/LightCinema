package com.example.lightcinema.ui.screens.visitor.reserving_screen

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chihsuanwu.freescroll.freeScrollWithTransformGesture
import com.chihsuanwu.freescroll.rememberFreeScrollState
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.mappers.SeatMapper.toModel
import com.example.lightcinema.data.visitor.network.responses.SeatResponse
import com.example.lightcinema.data.visitor.network.responses.SeatsCollectionResponse
import com.example.lightcinema.ui.common.SessionInfoButton
import com.example.lightcinema.ui.theme.LightCinemaTheme

@Composable
@Preview(showBackground = true)
fun SeatsModelCollectionPreview() {
    val testSeatsCollectionResponse = createTestSeatsCollectionResponse()
    val seatsModelCollection = toModel(testSeatsCollectionResponse)

    LightCinemaTheme {
        ScalableSeatMatrix(
            createTestSeatMatrix(15, 25)
        ) {
        }
    }
}

fun createTestSeatsCollectionResponse(): SeatsCollectionResponse {
    val seats = createTestSeatMatrix().flatten().map {
        SeatResponse(
            id = it.id,
            row = it.row,
            number = it.number,
            price = if (it.isVip) 20 else 15, // Произвольные цены для теста
            reserved = it.reserved
        )
    }
    return SeatsCollectionResponse(seats)
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
    }
}

@Composable
fun SeatMatrix(seatsMatrix: Array<Array<SeatModel>>, onSeatClick: (SeatModel) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        seatsMatrix.forEach { row ->
            Row {

                row.forEach { seat ->
                    Seat(seat, 40.dp, 40.dp, onSeatClick)
                }
                Text(
                    text = row.firstOrNull()?.row?.toString() ?: "",
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

fun createTestSeatMatrix(rows: Int, columns: Int): Array<Array<SeatModel>> {
    return Array(rows) { row ->
        Array(columns) { col ->
            val seatNumber = row * columns + col
            val isVip = (row in 5..10 && col in 5..15)
            val reserved = (row % 2 == 0 && col % 2 == 0)
            SeatModel(
                id = seatNumber,
                row = row + 1,
                number = seatNumber % columns + 1,
                isVip = isVip,
                reserved = reserved,
                selected = mutableStateOf(false)
            )
        }
    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReservingScreen(
    reservingViewModel: ReservingViewModel = viewModel(factory = ReservingViewModel.Factory),
    finishReserving: (String) -> Unit,
    upPress: () -> Unit
) {
    val movieResponse by reservingViewModel.movie.collectAsState()
    val seatsModelCollection by reservingViewModel.seatsModelCollection.collectAsState()
    val sessionId by reservingViewModel.sessionId.collectAsState()
    val successString by reservingViewModel.successString.collectAsState()
    val reservingResponse by reservingViewModel.reservingResponse.collectAsState(initial = null)

    val context = LocalContext.current
    when (val response = reservingResponse) {
        is ApiResponse.Failure -> {
            Log.d("Aboba", (reservingResponse as ApiResponse.Failure).errorMessage.toString())
            LaunchedEffect(Unit){
                Toast.makeText(context, response.errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        ApiResponse.Loading -> {}
        is ApiResponse.Success -> {
            finishReserving(successString)
        }

        null -> {}
    }
    when (val movie = movieResponse) {
        is ApiResponse.Failure -> TODO()
        ApiResponse.Loading -> {}
        is ApiResponse.Success -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 4.dp, horizontal = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = movie.data.movieName,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = movie.data.sessionsDate,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    IconButton(
                        onClick = upPress
                    ) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Закрыть")
                    }
                }
                FlowRow(
                    horizontalArrangement = Arrangement.SpaceAround,
//                    maxItemsInEachRow = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                ) {
                    movie.data.sessions.forEach { session ->
                        SessionInfoButton(
                            boxText = session.time,
                            additionInfo = session.minPrice.toString(),
                            isSelected = session.id == sessionId
                        ) {
                            reservingViewModel.setSession(session.id)
                        }
                    }
                }
                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier.fillMaxSize()
                ) {
                    when (val seatsCollection = seatsModelCollection) {
                        is ApiResponse.Failure -> TODO()
                        ApiResponse.Loading -> {
                            CircularProgressIndicator()
                        }

                        is ApiResponse.Success -> {
                            Column(
                                Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Экран", color = MaterialTheme.colorScheme.onBackground)
                                Spacer(modifier = Modifier.height(10.dp))
                                Divider(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                ScalableSeatMatrix(seatsCollection.data.seats) {
                                    if (!it.reserved) {
                                        reservingViewModel.changeSeatSelectedState(it)
                                    }
                                }
                                Divider(
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .fillMaxWidth()
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(horizontal = 20.dp, vertical = 5.dp)
                                        .fillMaxWidth()
                                ) {
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
                                        text = "${seatsCollection.data.baseCost}₽",
                                        color = MaterialTheme.colorScheme.tertiaryContainer
                                    )
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
                                        text = "${seatsCollection.data.vipCost}₽",
                                        color = MaterialTheme.colorScheme.tertiaryContainer
                                    )
                                    BoxSeat(
                                        "",
                                        MaterialTheme.colorScheme.onTertiaryContainer,
                                        Color.Transparent,
                                        Color.Transparent,
                                        20.dp,
                                        20.dp,
                                        {}
                                    )
                                    Text(
                                        text = "Занято",
                                        color = MaterialTheme.colorScheme.tertiaryContainer
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Button(
                                    onClick = {
                                        val count = reservingViewModel.getCountOfSelectedSeats(
                                            seatsCollection.data
                                        )
                                        if (count > 5) {
                                            Toast.makeText(
                                                context,
                                                "Число бронируемых мест не может превышать 5",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else if (count < 1) {
                                            Toast.makeText(
                                                context,
                                                "Выберите хотя бы одно место",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Log.d("Aboba", movie.data.sessions.joinToString(" "))
                                            reservingViewModel.reserveSeats(
//                                                seatsCollection.data,
//                                                movie.data.sessions
                                            )
                                        }
                                    },
                                    shape = RoundedCornerShape(
                                        16.dp
                                    ),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                    border = BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.onSecondary
                                    )
                                ) {
                                    if (reservingResponse is ApiResponse.Loading) {
                                        CircularProgressIndicator()
                                    } else {
                                        Text(
                                            text = "Забронировать",
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}


@Composable
fun Seat(
    seat: SeatModel, height: Dp, width: Dp, onSeatClick: (SeatModel) -> Unit
) {
    val backgroundColor = when {
        seat.reserved -> MaterialTheme.colorScheme.primary
        seat.selected.value -> MaterialTheme.colorScheme.tertiaryContainer
        seat.isVip -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.background
    }
    val textColor = when {
        seat.reserved -> MaterialTheme.colorScheme.onPrimary
        seat.selected.value -> MaterialTheme.colorScheme.onTertiaryContainer
        seat.isVip -> MaterialTheme.colorScheme.onSecondary
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
//                .clickable {
//                    onSeatClick()
//                }
                .fillMaxWidth()
        )
    }
}

fun createTestSeatMatrix(): Array<Array<SeatModel>> {
    val rows = 8
    val columns = 8
    val seatMatrix = Array(rows) { row ->
        Array(columns) { col ->
            val seatNumber = row * columns + col
            val isVip = (row in 2..5 && col in 2..5)
            val reserved = (row % 2 == 0 && col % 2 == 0)
            SeatModel(
                id = seatNumber,
                row = row + 1,
                number = seatNumber % columns + 1,
                isVip = isVip,
                reserved = reserved,
                selected = mutableStateOf(false)
            )
        }
    }
    return seatMatrix
}
