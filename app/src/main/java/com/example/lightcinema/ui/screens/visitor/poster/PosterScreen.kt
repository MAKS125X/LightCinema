package com.example.lightcinema.ui.screens.visitor.poster

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.visitor.network.responses.MovieCollectionResponse
import com.example.lightcinema.data.visitor.network.responses.MovieItemResponse
import com.example.lightcinema.data.visitor.network.responses.SessionTimeResponse
import com.example.lightcinema.ui.common.SessionInfoButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PosterScreen(
    viewModel: PosterViewModel = viewModel(factory = PosterViewModel.Factory),
    onMovieClick: (Int) -> Unit,
    onSessionClick: (Int) -> Unit
) {
    val today = viewModel.posterToday.collectAsState()
    val tomorrow = viewModel.posterTomorrow.collectAsState()
    val soon = viewModel.posterSoon.collectAsState()


    val pagerState = rememberPagerState { 3 }

    Column(modifier = Modifier.fillMaxSize()) {
        Tabs(pagerState = pagerState)
        TabsContent(
            pagerState = pagerState,
            onMovieClick,
            onSessionClick,
            today.value,
            tomorrow.value,
            soon.value,
        )
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
                date = sessionDate,
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                modifier = Modifier.padding(12.dp, 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContent(
    pagerState: PagerState,
    onMovieClick: (Int) -> Unit = {},
    onSessionClick: (Int) -> Unit,
    today: ApiResponse<MovieCollectionResponse>,
    tomorrow: ApiResponse<MovieCollectionResponse>,
    soon: ApiResponse<MovieCollectionResponse>
) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> {
                Column {
                    PosterTabItem(today, true, onMovieClick, onSessionClick)
                }
            }

            1 -> {
                Column {
                    PosterTabItem(tomorrow, true, onMovieClick, onSessionClick)
                }
            }

            2 -> {
                Column {
                    PosterTabItem(soon, false, onMovieClick, onSessionClick)
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
    onMovieClick: (Int) -> Unit = {},
    onSessionClick: (Int) -> Unit,
) {
    val context = LocalContext.current
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp, 0.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(movieCollection.data.movies) { index, item ->
                        MovieItem(
                            movieItemResponse = item,
                            showSessions,
                            onMovieClick,
                            onSessionClick
                        )
                        Spacer(modifier = Modifier.padding(0.dp, 8.dp))
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
    onSessionClick: (Int) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
            .clickable { onMovieClick(movieItemResponse.id) }
    ) {
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
                        ImageBitmap.imageResource(R.drawable.drive),
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
                                onSessionClick = onSessionClick
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
    onSessionClick: (Int) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.SpaceAround,
        maxItemsInEachRow = 3,
        modifier = modifier
    ) {
        for (session in sessionShort) {
            SessionInfoButton(
                session.time,
                session.minPrice.toString(),
                onClick = { onSessionClick(session.id) })
        }
    }
}

