package com.example.lightcinema.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.lightcinema.getTestSeatsList
import com.example.lightcinema.models.CostClass
import com.example.lightcinema.models.Seat
import com.example.lightcinema.ui.theme.LightCinemaTheme

@Composable
fun CinemaHall(seats: MutableList<Seat>, seatHeight: Dp, seatWidth: Dp) {
//    val map: Map<Int, Seat> = seats.sortedBy { it.numberInRow }.associateBy { it.row }.toSortedMap()
//    for (row in map.keys) {
//        map.mapValues {  }
//    }
    val lists = seats.groupBy { it.row }
        .map { it.value.sortedBy { seat -> seat.numberInRow } }
        .sortedBy { it.first().row }
//    val matrix = remember {
//        mutableStateListOf<List<Seat>>()
//    }
    val matrix by remember {
        mutableStateOf(seats.groupBy { it.row }
            .map { it.value.sortedBy { seat -> seat.numberInRow } }
            .sortedBy { it.first().row })
    }

//    for (list in lists) {
//        matrix.add(list)
//    }

    Column {
        for (row in matrix) {
            Row {
                for (seatNumber in 0..row.size - 2) {
                    Seat(seat = row[seatNumber], height = seatHeight, width = seatWidth) { seat ->
                        seat.isSelected = !seat.isSelected
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Seat(seat = row[row.size - 1], height = seatHeight, width = seatWidth) { seat ->
                    seat.isSelected = !seat.isSelected
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }

}

@Composable
fun CinemaScreen() {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        offset += offsetChange
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            // apply other transformations like rotation and zoom
            // on the pizza slice emoji
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            )
            .transformable(state = state)
            .fillMaxSize()
    ) {
        CinemaHall(getTestSeatsList(), 22.dp, 22.dp)
    }
}

//@Preview(showBackground = true)
@Composable
fun Seat(
    seat: Seat,
    height: Dp,
    width: Dp,
    onSeatClick: (Seat) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp))
            .height(height)
            .width(width)
            .background(
                color = if (seat.isSelected) {
                    MaterialTheme.colorScheme.tertiaryContainer
                } else {
                    when (seat.costClass) {
                        CostClass.BASIC -> MaterialTheme.colorScheme.background
                        CostClass.VIP -> MaterialTheme.colorScheme.secondary
                        CostClass.TAKEN -> MaterialTheme.colorScheme.primary
                    }
                }, RoundedCornerShape(5.dp)
            )
            .clickable { onSeatClick(seat) }
    ) {
        Text(
            text = seat.numberInRow.toString(), color = if (seat.isSelected) {
                MaterialTheme.colorScheme.onTertiaryContainer
            } else {
                when (seat.costClass) {
                    CostClass.BASIC -> MaterialTheme.colorScheme.onBackground
                    CostClass.VIP -> MaterialTheme.colorScheme.onSecondary
                    CostClass.TAKEN -> MaterialTheme.colorScheme.onPrimary
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CinemaHallPreview(seats: MutableList<Seat> = getTestSeatsList()) {
    LightCinemaTheme {
        CinemaHall(seats, 22.dp, 22.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun SeatPreview(
    seat: Seat = Seat(1, 2, 3, CostClass.VIP, false),
    height: Dp = 22.dp,
    width: Dp = 22.dp
) {
//    val lineHeightSp: TextUnit = TextUnit.Unspecified
//    val lineHeightDp: Dp = with(LocalDensity.current) {
//        lineHeightSp.toDp()
//    }
    LightCinemaTheme {
        Seat(seat, height, width) { seat ->
            seat.isSelected = !seat.isSelected
        }
    }
}