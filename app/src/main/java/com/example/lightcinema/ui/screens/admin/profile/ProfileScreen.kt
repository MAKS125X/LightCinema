package com.example.lightcinema.ui.screens.admin.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Close
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lightcinema.data.admin.network.responses.HallItemResponse
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.ui.common.LoadIndicator
import com.example.lightcinema.ui.theme.LightCinemaTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import okhttp3.ResponseBody

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory),
    upPress: () -> Unit,
    addHallClick: () -> Unit,
    onAboutProgramClick: () -> Unit,
) {
    val context = LocalContext.current
    val hallsResponse = viewModel.hallList.collectAsState()
    val deletingResponse = viewModel.deletingResponse.collectAsState(initial = null)
    var refreshing by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (val value = hallsResponse.value) {
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
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = refreshing), onRefresh = {
                        viewModel.updateHallsList()
                        refreshing = false
                    }, modifier = Modifier.fillMaxSize()
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        ProfileSuccess(
                            value.data.halls,
                            { upPress() },
                            addHallClick,
                            {
                                viewModel.deleteHall(it)
                            },
                            deletingResponse,
                            { onAboutProgramClick() },
                            { viewModel.updateHallsList() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileSuccess(
    halls: List<HallItemResponse>,
    upPress: () -> Unit,
    addHallClick: () -> Unit,
    onDeleteClick: (Int) -> Unit,
    deletingResponse: State<ApiResponse<ResponseBody>?>,
    onAboutProgramClick: () -> Unit,
    updateHalls: () -> Unit,
) {
    var openAlertDialog by remember { mutableStateOf(false) }
    var selectedHall by remember { mutableIntStateOf(-1) }

    val context = LocalContext.current

    when (val response = deletingResponse.value) {
        is ApiResponse.Failure -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, response.errorMessage.toString(), Toast.LENGTH_LONG).show()
            }
            openAlertDialog = false
        }

        ApiResponse.Loading -> {}
        is ApiResponse.Success -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Зал удалён", Toast.LENGTH_LONG).show()
            }
            openAlertDialog = false
            updateHalls()
        }

        null -> {

        }
    }

    when {
        openAlertDialog -> StyleAlertDialog(
            onDismissRequest = {
                openAlertDialog = false
            },
            onConfirm = {
                onDeleteClick(selectedHall)
            },
            onDismiss = {
                openAlertDialog = false
            }
        )
    }

    var refreshing by remember {
        mutableStateOf(false)
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing),
        onRefresh = {
            refreshing = false
        },
        modifier = Modifier.fillMaxSize()
    ) {


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
                    onAboutProgramClick()
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
            Text(text = "ADMIN", color = MaterialTheme.colorScheme.onBackground, fontSize = 40.sp)
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
                    text = "Список залов",
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
            if (halls.isEmpty()) {
                Text(
                    text = "Ни одного зала не добавлено",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (halls.isNotEmpty()) {
                LazyColumn(modifier = Modifier.padding(horizontal = 20.dp)) {
                    items(halls) { item ->
                        HallItem(
                            hall = item,
                            modifier = Modifier
                                .fillMaxWidth(),
                            onDeleteClick = {
                                selectedHall = item.number
                                openAlertDialog = true
                            }
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                    }
                    item {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                    RoundedCornerShape(8.dp)
                                )
                                .border(
                                    1.dp,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    RoundedCornerShape(8.dp)
                                )
                                .fillMaxWidth()
                                .padding(vertical = 5.dp, horizontal = 10.dp)
                        ) {
                            Text(text = "Добавить зал")
                            IconButton(
                                onClick = { addHallClick() },
                                modifier = Modifier
                                    .padding(0.dp)
                                    .wrapContentSize()
                                    .width(24.dp)
                                    .height(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Sharp.Add,
                                    contentDescription = "Добавить зал",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .padding(0.dp)
                                        .padding(top = 2.dp, end = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun HallItem(
    hall: HallItemResponse,
    modifier: Modifier,
    onDeleteClick: () -> Unit
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
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Зал ${hall.number}",
                    color = onBackground,
                    fontSize = 18.sp
                )
                IconButton(
                    onClick = { onDeleteClick() },
                    modifier = Modifier
                        .padding(0.dp)
                        .wrapContentSize()
                        .width(24.dp)
                        .height(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Sharp.Close,
                        contentDescription = "Удалить",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(0.dp)
                            .padding(top = 2.dp, end = 2.dp)
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                val seatsCount = hall.rows * hall.seatsInRow
                Column(horizontalAlignment = Alignment.Start) {
                    ReserveItemAdditionalInfoText(
                        text = "Рядов: ${hall.rows}",
                    )
                    ReserveItemAdditionalInfoText(
                        text = "Мест в ряде: ${hall.seatsInRow}",
                    )
                    ReserveItemAdditionalInfoText(
                        text = "Всего мест: ${seatsCount}",
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    ReserveItemAdditionalInfoText(
                        text = "Обычных мест: ${seatsCount - hall.vipSeats}",
                    )
                    ReserveItemAdditionalInfoText(
                        text = "VIP мест: ${hall.vipSeats}",
                    )
                }
            }
        }
    }
}


@Composable
fun StyleAlertDialog(
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
fun HallItemPreview(
    hall: HallItemResponse = HallItemResponse(1, 25, 15, 100),
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = { }
) {
    LightCinemaTheme {
        HallItem(
            hall,
            modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            { }
        )
    }
}


