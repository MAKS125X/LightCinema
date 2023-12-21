package com.example.lightcinema.ui.screens.visitor.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.ui.common.LoadIndicator
import com.example.lightcinema.ui.theme.LightCinemaTheme

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory),
    upPress: () -> Unit
) {
    val context = LocalContext.current
    val profileInfo = viewModel.profileInfo.collectAsState()


    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (val value = profileInfo.value) {
            is ApiResponse.Failure -> {
                LaunchedEffect(Unit) {
                    Toast.makeText(context, value.errorMessage, Toast.LENGTH_LONG).show()
                }
            }

            ApiResponse.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    LoadIndicator()
                }
            }

            is ApiResponse.Success -> {
                Log.d("Aboba", value.data.toString())
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ProfileSuccess(
                        value.data.login,
                        value.data.reserves,
                        { upPress() },
                        { sessionId, seatId -> viewModel.unreserve(sessionId, seatId) })
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileSuccess(
    nickname: String,
    reservations: List<ReserveModel>,
    upPress: () -> Unit,
    onUnreserve: (Int, Int) -> Unit,
) {
    var openAlertDialog by remember { mutableStateOf(false) }
    var unreservedSeatId by remember { mutableIntStateOf(-1) }
    var unreservedSessionId by remember { mutableIntStateOf(-1) }

    when {
        openAlertDialog -> UnreserveAlertDialog(
            onDismissRequest = {
                openAlertDialog = false
            },
            onConfirm = {
                onUnreserve(unreservedSessionId, unreservedSeatId)
                openAlertDialog = false
            },
            onDismiss = {
                openAlertDialog = false
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                TODO()
            }, modifier = Modifier.size(48.dp)) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "О программе",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = "Иконка профиля",
                modifier = Modifier.size(240.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )

            IconButton(onClick = { upPress() }, modifier = Modifier.size(48.dp)) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Закрыть",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Text(text = nickname, color = MaterialTheme.colorScheme.onBackground, fontSize = 40.sp)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Мои брони",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 36.sp
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .height(2.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )
        if (reservations.isNotEmpty()) {
            LazyColumn {
                items(reservations) { item ->
                    ReservationItem(
                        reserveModel = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        onClick = {
                            unreservedSessionId = item.sessionId
                            unreservedSeatId = item.seatId
                            openAlertDialog = true
                        }
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                }
            }
        }
    }
}


@Composable
fun ReservationItem(
    reserveModel: ReserveModel,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val onBackground = MaterialTheme.colorScheme.onBackground
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.background,
                RoundedCornerShape(8.dp)
            )
            .border(
                1.dp,
                color = onBackground,
                RoundedCornerShape(8.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 5.dp)
        ) {
            Row(modifier = Modifier.padding(horizontal = 10.dp)) {
                Text(
                    text = reserveModel.movieName,
                    color = onBackground,
                    fontSize = 18.sp
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                color = onBackground
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    ReserveItemAdditionalInfoText(
                        text = "Зал: ${reserveModel.hall}",
                    )
                    ReserveItemAdditionalInfoText(
                        text = "Ряд: ${reserveModel.row}",
                    )
                    ReserveItemAdditionalInfoText(
                        text = "Место: ${reserveModel.number}",
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    ReserveItemAdditionalInfoText(
                        text = "Дата сеанса: ${reserveModel.date}",
                    )
                    ReserveItemAdditionalInfoText(
                        text = "Время начала сеанса: ${reserveModel.time}",
                    )
                    if (reserveModel.canUnreserve) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .wrapContentSize()
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    onClick()
                                }
                        ) {
                            Text(
                                text = "Отменить бронь",
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun UnreserveAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(text = "Отменить бронь?")
        },
        modifier = Modifier.border(
            1.dp,
            MaterialTheme.colorScheme.onBackground,
            AlertDialogDefaults.shape
        ),
        text = {
            Text(text = "Вы действительно хотите отменить бронь для данного места?")
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text("Да")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Нет")
            }
        }
    )
}

@Composable
fun ReserveItemAdditionalInfoText(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Preview(showBackground = true)
@Composable
fun ReservationItemPreview(
    reserveModel: ReserveModel = ReserveModel(1, 2, "DRIVE", 3, 4, 5, false, "14.12.2023", "12:00"),
) {
    LightCinemaTheme {
        ReservationItem(
            reserveModel,
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp), { }
        )

    }
}


