package com.example.lightcinema.ui.screens.admin.poster

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.example.lightcinema.R
import com.example.lightcinema.data.admin.network.responses.MovieCollectionResponse
import com.example.lightcinema.data.admin.network.responses.MovieItemResponse
import com.example.lightcinema.data.admin.network.responses.SessionTimeResponse
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.ui.common.SessionInfoButton
import com.example.lightcinema.ui.models.SessionDate
import com.example.lightcinema.ui.screens.admin.addSession.AddSessionDialog
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PosterScreen(
    viewModel: PosterViewModel = viewModel(factory = PosterViewModel.Factory),
    onMovieClick: (Int) -> Unit,
    addMovieClick: () -> Unit
) {
    val today = viewModel.posterToday.collectAsState()
    val tomorrow = viewModel.posterTomorrow.collectAsState()
    val soon = viewModel.posterSoon.collectAsState()
    var refreshing by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState { 3 }

    val deleteSessionResponse = viewModel.deleteSessionResponse.collectAsState(initial = null)

    val context = LocalContext.current

    when (val response = deleteSessionResponse.value) {
        is ApiResponse.Failure -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, response.errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        ApiResponse.Loading -> {}

        is ApiResponse.Success -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Сеанс удалён", Toast.LENGTH_LONG).show()
                viewModel.getPosterInfo(SessionDate.Today)
                viewModel.getPosterInfo(SessionDate.Tomorrow)
                viewModel.getPosterInfo(SessionDate.Soon)
            }
        }

        null -> {}
    }

    var movieId by remember {
        mutableIntStateOf(-1)
    }

    var showAddSessionDialog by remember {
        mutableStateOf(false)
    }

    if (showAddSessionDialog) {
        AddSessionDialog(
            onDismissRequest = { showAddSessionDialog = false },
            movieId = movieId,
            onSuccessRequest = {
                showAddSessionDialog = false
                viewModel.getPosterInfo(SessionDate.Today)
                viewModel.getPosterInfo(SessionDate.Tomorrow)
                viewModel.getPosterInfo(SessionDate.Soon)
            }
        )
    }

    val columnModifier = if (showAddSessionDialog) Modifier
        .fillMaxSize()
        .blur(
            radiusX = 10.dp,
            radiusY = 10.dp,
            edgeTreatment = BlurredEdgeTreatment(RoundedCornerShape(8.dp))
        )
    else Modifier
        .fillMaxSize()
    Column(
        modifier = columnModifier
    ) {

        Tabs(pagerState = pagerState)
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = refreshing), onRefresh = {
                viewModel.getPosterInfo(SessionDate.Today)
                viewModel.getPosterInfo(SessionDate.Tomorrow)
                viewModel.getPosterInfo(SessionDate.Soon)
                refreshing = false
            }, modifier = Modifier.fillMaxSize()
        ) {
            TabsContent(
                pagerState = pagerState,
                onMovieClick,
                { viewModel.deleteSession(it) },
                today.value,
                tomorrow.value,
                soon.value,
                addSessionClick = {
                    movieId = it
                    showAddSessionDialog = true
                },
                addMovieClick = {
                    addMovieClick()
                }
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf(
        "Сегодня", "Завтра", "Скоро"
    )
    val scope = rememberCoroutineScope()
    TabRow(selectedTabIndex = pagerState.currentPage, indicator = {}, divider = {}) {
        list.forEachIndexed { index, sessionDate ->
            DateTab(
                date = sessionDate, selected = pagerState.currentPage == index, onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }, modifier = Modifier.padding(12.dp, 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContent(
    pagerState: PagerState,
    onMovieClick: (Int) -> Unit,
    onSessionClick: (Int) -> Unit,
    today: ApiResponse<MovieCollectionResponse>,
    tomorrow: ApiResponse<MovieCollectionResponse>,
    soon: ApiResponse<MovieCollectionResponse>,
    addSessionClick: (Int) -> Unit,
    addMovieClick: () -> Unit,
) {

    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> {
                Column {
                    PosterTabItem(
                        today,
                        true,
                        onMovieClick,
                        onSessionClick,
                        addSessionClick,
                        addMovieClick
                    )
                }
            }


            1 -> {
                Column {
                    PosterTabItem(
                        tomorrow,
                        true,
                        onMovieClick,
                        onSessionClick,
                        addSessionClick,
                        addMovieClick
                    )
                }

            }

            2 -> {
                Column {
                    PosterTabItem(
                        soon,
                        false,
                        onMovieClick,
                        onSessionClick,
                        addSessionClick,
                        addMovieClick
                    )
                }
            }
        }
    }
}

@Composable
fun DateTab(date: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Tab(
        text = {
            Text(
                text = date,
                color = if (selected) colorScheme.onPrimary else colorScheme.onBackground
            )
        }, selected = selected, onClick = onClick, modifier = modifier
            .background(
                color = if (selected) colorScheme.primary else colorScheme.background,
                shape = RoundedCornerShape(8.dp)
            )
            .border(1.dp, color = colorScheme.primary, shape = RoundedCornerShape(8.dp))
    )
}

@Composable
fun PosterTabItem(
    movieCollection: ApiResponse<MovieCollectionResponse>,
    showSessions: Boolean,
    onMovieClick: (Int) -> Unit,
    onSessionClick: (Int) -> Unit,
    addSessionClick: (Int) -> Unit,
    addMovieClick: () -> Unit
) {
    val context = LocalContext.current

    var openDeleteSessionDialog by remember { mutableStateOf(false) }
    var selectedSession by remember {
        mutableIntStateOf(-1)
    }

    when {
        openDeleteSessionDialog -> AlertDialog(
            title = {
                Text(text = "Удалить сеанс?")
            },
            modifier = Modifier.border(
                1.dp,
                MaterialTheme.colorScheme.onBackground,
                AlertDialogDefaults.shape
            ),
            text = {
                Text(text = "Вы действительно хотите удалить данный сеанс?")
            },
            onDismissRequest = {
                openDeleteSessionDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDeleteSessionDialog = false
                        onSessionClick(selectedSession)
                    }
                ) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDeleteSessionDialog = false
                    }
                ) {
                    Text("Нет")
                }
            }
        )
    }

    when (movieCollection) {
        is ApiResponse.Failure -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, movieCollection.errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        ApiResponse.Loading -> {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Image(Icons.Filled.Refresh, contentDescription = "Загрузки")
            }
        }

        is ApiResponse.Success -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp, 0.dp)
            ) {
                if (movieCollection.data.movies.isEmpty()) {
                    Text(text = "На данный день не добавлено сеансов")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        itemsIndexed(movieCollection.data.movies) { index, item ->
                            MovieItem(
                                movieItemResponse = item,
                                showSessions,
                                onMovieClick,
                                {
                                    selectedSession = it
                                    openDeleteSessionDialog = true
                                },
                                addSessionClick,
                            )
                            Spacer(modifier = Modifier.padding(0.dp, 8.dp))
                        }
                        item {
                            Button(
                                onClick = { addMovieClick() },
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier.width(250.dp)
                            ) {
                                Text(
                                    text = "Добавить фильм",
                                    maxLines = 1,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.padding(4.dp, 4.dp),
                                    fontSize = 12.sp
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
fun MovieItem(
    movieItemResponse: MovieItemResponse,
    showSessions: Boolean = true,
    onMovieClick: (Int) -> Unit,
    onSessionClick: (Int) -> Unit,
    addSessionClick: (Int) -> Unit
) {
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
            .clickable { onMovieClick(movieItemResponse.id) }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SubcomposeAsyncImage(
                model = movieItemResponse.posterLink,
                loading = {
                    Box {
                        CircularProgressIndicator()
                    }
                },
                contentDescription = movieItemResponse.name,
                contentScale = ContentScale.Fit,
                error = {
                    Image(
                        ImageBitmap.imageResource(R.drawable.no_photo_ver),
                        contentDescription = "Нельзя загрузить изображение"
                    )
                },
                modifier = Modifier
                    .width(108.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = movieItemResponse.name.uppercase(),
                        fontSize = 26.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = movieItemResponse.genres.joinToString(", ") { it },
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        softWrap = true,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp)
                    )
                    if (showSessions) {
                        movieItemResponse.sessions?.let {
                            SessionFlexRow(
                                sessionShort = it,
                                modifier = Modifier.fillMaxWidth(),
                                onSessionClick = onSessionClick,
                                addSessionClick = { addSessionClick(movieItemResponse.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SessionFlexRow(
    sessionShort: List<SessionTimeResponse>,
    modifier: Modifier,
    onSessionClick: (Int) -> Unit,
    addSessionClick: () -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.SpaceAround, maxItemsInEachRow = 3, modifier = modifier
    ) {
        for (session in sessionShort) {
            SessionInfoButton(session.time,
                session.minPrice.toString(),
                onClick = { onSessionClick(session.id) })
        }
        SessionInfoButton("+", "", onClick = { addSessionClick() })
    }
}

