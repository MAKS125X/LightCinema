package com.example.lightcinema.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.lightcinema.models.CostClass
import com.example.lightcinema.models.Seat
import com.example.lightcinema.ui.theme.LightCinemaTheme


@Composable
fun CinemaHall(seats: MutableList<Seat>) {

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

        Seat(seat, height, width)

    }

}

//@Preview(showBackground = true)
@Composable
fun Seat(
    seat: Seat,
    height: Dp,
    width: Dp
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