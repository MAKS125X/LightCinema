package com.example.lightcinema.ui.screens

import  android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lightcinema.getTestSeatsList
import com.example.lightcinema.models.CostClass
import com.example.lightcinema.models.Seat
import com.example.lightcinema.ui.theme.LightCinemaTheme

@Composable
fun CinemaHallGrid(
    matrix: MutableList<Seat>, onSeatClick: (Seat, Seat) -> Unit, seatHeight: Dp, seatWidth: Dp
) {
//    val matrix by remember {
//        mutableStateOf(seats.groupBy { it.row }
//            .map { it.value.sortedBy { seat -> seat.numberInRow } }
//            .sortedBy { it.first().row }
//        )
//    }


    val TAG = "aboba"
    LazyVerticalGrid(columns = GridCells.Fixed(matrix.maxOf { it.numberInRow })) {
        itemsIndexed(items = matrix) { _, seat ->
            Seat(seat = seat, height = seatHeight, width = seatWidth) { lastSeat, newSeat ->
                onSeatClick(lastSeat, newSeat)
            }
        }
    }
}


@Composable
fun CinemaScreen(
    cinemaHallViewModel: CinemaHallViewModel = viewModel()
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        offset += offsetChange
    }

    val matrix by cinemaHallViewModel.hallMatrix.collectAsStateWithLifecycle()

    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
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
        CinemaHallGrid(
            matrix,
            { a, b -> cinemaHallViewModel.changeSeatSelectedState(a, b) },
            seatHeight = 51.dp,
            seatWidth = 51.dp
        )
    }
}

//@Preview(showBackground = true)
@Composable
fun Seat(
    seat: Seat, height: Dp, width: Dp, onSeatClick: (Seat, Seat) -> Unit
) {
//    val seatIsSelected by remember {
//        mutableStateOf(seat.isSelected)
//    }
    val backgroundColor = if (seat.isSelected) {
        MaterialTheme.colorScheme.tertiaryContainer
    } else {
        when (seat.costClass) {
            CostClass.BASIC -> MaterialTheme.colorScheme.background
            CostClass.VIP -> MaterialTheme.colorScheme.secondary
            CostClass.TAKEN -> MaterialTheme.colorScheme.primary
        }
    }
    val textColor = if (seat.isSelected) {
        MaterialTheme.colorScheme.onTertiaryContainer
    } else {
        when (seat.costClass) {
            CostClass.BASIC -> MaterialTheme.colorScheme.onBackground
            CostClass.VIP -> MaterialTheme.colorScheme.onSecondary
            CostClass.TAKEN -> MaterialTheme.colorScheme.onPrimary
        }
    }
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .border(
                1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp)
            )
            .height(height)
            .width(width)
            .background(
                color = backgroundColor, RoundedCornerShape(5.dp)
            )
            .clickable {
                Log.v("aboba", "CLICKED")
                onSeatClick(seat, seat.copy(isSelected = !seat.isSelected))
            }

    ) {
        Text(
            text = seat.numberInRow.toString(), color = textColor
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CinemaHallPreview(seats: MutableList<Seat> = getTestSeatsList()) {
    LightCinemaTheme {
        CinemaHallGrid(mutableListOf(), { a, b -> }, seatHeight = 51.dp, seatWidth = 51.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun SeatPreview(
    seat: Seat = Seat(1, 2, 3, CostClass.VIP, false), height: Dp = 22.dp, width: Dp = 22.dp
) {
//    val lineHeightSp: TextUnit = TextUnit.Unspecified
//    val lineHeightDp: Dp = with(LocalDensity.current) {
//        lineHeightSp.toDp()
//    }
    LightCinemaTheme {
        Seat(seat, height, width) { _, _ ->
        }
    }
}


