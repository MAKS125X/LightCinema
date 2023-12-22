package com.example.lightcinema.ui.screens.visitor.movie_info

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.example.lightcinema.R
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.mappers.MovieMapper
import com.example.lightcinema.data.visitor.network.responses.CountryResponse
import com.example.lightcinema.data.visitor.network.responses.GenreResponse
import com.example.lightcinema.data.visitor.network.responses.MovieLongResponse
import com.example.lightcinema.data.visitor.network.responses.SessionDateResponse
import com.example.lightcinema.ui.common.LoadIndicator
import com.example.lightcinema.ui.common.SessionInfoButton
import com.example.lightcinema.ui.theme.LightCinemaTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun MovieInfoScreen(
    viewModel: MovieInfoViewModel = viewModel(factory = MovieInfoViewModel.Factory),
    onSessionClick: (Int) -> Unit
) {

    val movie = viewModel.movie.collectAsState()

    var refreshing by remember { mutableStateOf(false) }

//    LaunchedEffect(refreshing) {
//        if (refreshing) {
//            delay(3000)
//            refreshing = false
//        }
//    }
    val context = LocalContext.current

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing),
        onRefresh = {
            viewModel.updateMovieInfo()
            refreshing = false
        },
        modifier = Modifier.fillMaxSize()
    ) {
        //ToDo:CheckSwipes
        when (val response = movie.value) {
            is ApiResponse.Failure -> {
                LaunchedEffect(Unit) {
                    Toast.makeText(context, response.errorMessage, Toast.LENGTH_LONG).show()
                }
            }

            ApiResponse.Loading -> MovieLoading()
            is ApiResponse.Success -> MovieInfo(
                Modifier
                    .fillMaxSize()
                    .padding(10.dp), response.data, onSessionClick
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieInfo(
    modifier: Modifier = Modifier,
    movie: MovieModel,
    onSessionClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            SubcomposeAsyncImage(
                model = movie.imageLink,
                contentDescription = movie.name,
                contentScale = ContentScale.FillWidth,
                error = {
                    Image(
                        ImageBitmap.imageResource(R.drawable.no_photo_hor),
                        contentDescription = "Нельзя загрузить изображение"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
            )
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = movie.name.uppercase(),
                    fontSize = 26.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = movie.genres.joinToString(", ") { it },
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp)
                )
                Text(
                    text = movie.countries.joinToString(", ") { it },
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp)
                )
                Text(
                    text = movie.description, fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp)
                )
            }
        }

        movie.sessionMap.forEach {
            item {
                Text(
                    text = it.key,
                    fontSize = 26.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                FlowRow(
                    horizontalArrangement = Arrangement.SpaceAround,
                    maxItemsInEachRow = 3,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    it.value.forEach { session ->
                        SessionInfoButton(
                            boxText = session.time.format(
                                DateTimeFormatter.ofPattern(
                                    "HH:mm", Locale.getDefault()
                                )
                            ), additionInfo = session.minPrice.toString()
                        ) {
                            onSessionClick(session.id)
                        }
                    }
                }
            }
//            itemsIndexed(it.value) { index, item ->
//
//            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun MovieInfoPreview(
    modifier: Modifier = Modifier,
    movie: MovieModel = MovieMapper.toModel(
        MovieLongResponse(
            1,
            "Drive",
            "Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание",
            listOf(GenreResponse(1, "asd")),
            2011,
            listOf(CountryResponse(1, "asd")),
            12,
            "https://4.bp.blogspot.com/-pMdtPxE2iEk/Ty8RwlalpCI/AAAAAAAAF18/UaaLXMQIwCI/s1600/driceempire.jpg",
            "https://4.bp.blogspot.com/-pMdtPxE2iEk/Ty8RwlalpCI/AAAAAAAAF18/UaaLXMQIwCI/s1600/driceempire.jpg",
            listOf(
                SessionDateResponse(
                    1,
                    "2023-12-10 12:00",
                    300
                ),
                SessionDateResponse(
                    2,
                    "2023-12-10 14:00",
                    300
                ),
                SessionDateResponse(
                    3,
                    "2023-12-11 12:00",
                    300
                ),
                SessionDateResponse(
                    4,
                    "2023-12-11 14:00",
                    300
                ),
                SessionDateResponse(
                    5,
                    "2023-12-10 16:00",
                    400
                ),
                SessionDateResponse(
                    6,
                    "2023-12-11 16:00",
                    500
                ),
                SessionDateResponse(
                    5,
                    "2023-12-10 18:00",
                    400
                ),
                SessionDateResponse(
                    6,
                    "2023-12-11 18:00",
                    500
                ),
                SessionDateResponse(
                    5,
                    "2023-12-10 20:00",
                    400
                ),
                SessionDateResponse(
                    6,
                    "2023-12-11 20:00",
                    500
                ),
                SessionDateResponse(
                    5,
                    "2023-12-10 22:00",
                    400
                ),
                SessionDateResponse(
                    6,
                    "2023-12-11 22:00",
                    500
                )
            )
        )
    )
) {
    LightCinemaTheme {
        MovieInfo(
            Modifier
                .fillMaxSize()
                .padding(10.dp), movie
        ) { _ -> }
    }
}

//@Preview(showBackground = true)
@Composable
fun MovieLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        LoadIndicator()
    }
}

