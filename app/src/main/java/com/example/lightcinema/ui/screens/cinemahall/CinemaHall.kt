package com.example.lightcinema.ui.screens.cinemahall

//import androidx.compose.ui.graphics.Paint

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.lightcinema.ui.theme.LightCinemaTheme

//@Composable
//fun CinemaHallGrid(
//    matrix: MutableList<SeatModel>, onSeatClick: (SeatModel) -> Unit, seatHeight: Dp, seatWidth: Dp
//) {
//    LazyVerticalGrid(columns = GridCells.Fixed(matrix.maxOf { it.numberInRow })) {
//        itemsIndexed(items = matrix) { _, seat ->
//            Seat(seat = seat, height = seatHeight, width = seatWidth) { lastSeat ->
//                onSeatClick(lastSeat)
//            }
//        }
//    }
//}

@Composable
@Preview(showBackground = true)
fun SeatsModelCollectionPreview() {
    val testSeatsCollectionResponse = createTestSeatsCollectionResponse()
    val seatsModelCollection = toModel(testSeatsCollectionResponse)

    LightCinemaTheme {
        ScalableSeatMatrix(
            createTestSeatMatrix(8, 10)
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
    Column(modifier = Modifier
        .fillMaxSize()
        .freeScrollWithTransformGesture(
            state = freeScrollState,
            onGesture = { centroid: Offset,
                          pan: Offset,
                          zoom: Float,
                          rotation: Float ->
                scale *= zoom
                Log.d("Aboba", "Zoom: $zoom")
            }
        )
        .graphicsLayer(scaleX = scale, scaleY = scale)
    ) {
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

@Composable
fun SeatMatrix(seatsMatrix: Array<Array<SeatModel>>, onSeatClick: (SeatModel) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        seatsMatrix.forEach { row ->
            Row {

                row.forEach { seat ->
                    Seat(seat, 40.dp, 40.dp, onSeatClick)
                }
//                Spacer(modifier = Modifier.width(5.dp))
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


@Composable
fun CinemaScreen(
    cinemaHallViewModel: CinemaHallViewModel = viewModel(factory = CinemaHallViewModel.Factory)
) {
    val movie by cinemaHallViewModel.movie.collectAsState()
    val seatsModelCollection by cinemaHallViewModel.seatsModelCollection.collectAsState()

    when (val value = movie) {
        is ApiResponse.Failure -> TODO()
        ApiResponse.Loading -> {}
        is ApiResponse.Success -> {
            Column {
                Text(text = value.data.name, color = MaterialTheme.colorScheme.onBackground)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    when (val seats = seatsModelCollection) {
                        is ApiResponse.Failure -> TODO()
                        ApiResponse.Loading -> {
                            CircularProgressIndicator()
                        }

                        is ApiResponse.Success -> {
                            ScalableSeatMatrix(seats.data.seats) {
                                cinemaHallViewModel.changeSeatSelectedState(it)
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
        seat.selected.value -> MaterialTheme.colorScheme.tertiaryContainer
        seat.isVip -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.background
    }
    val textColor = when {
        seat.selected.value -> MaterialTheme.colorScheme.onTertiaryContainer
        seat.isVip -> MaterialTheme.colorScheme.onSecondary
        else -> MaterialTheme.colorScheme.onBackground
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(8.dp)
            .border(
                1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp)
            )
            .height(height)
            .width(width)
            .background(
                color = backgroundColor, RoundedCornerShape(5.dp)
            )
//            .clickable {
//                onSeatClick(seat)
//            }

    ) {
        Text(
            text = seat.number.toString(), color = textColor, modifier = Modifier.clickable {
                onSeatClick(seat)
            }
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
