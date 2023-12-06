package com.example.lightcinema.ui.screens.filminfo

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lightcinema.R
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.visitor.network.responses.MovieLongResponse
import com.example.lightcinema.data.visitor.network.responses.SessionDateResponse
import com.example.lightcinema.ui.common.SessionInfoButton
import com.example.lightcinema.ui.mappers.MovieMapper
import com.example.lightcinema.ui.theme.LightCinemaTheme
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun MovieInfoScreen(viewModel: MovieInfoViewModel = viewModel(factory = MovieInfoViewModel.Factory)) {

    val movie = viewModel.movie.collectAsState()

    when (val response = movie.value) {
        is ApiResponse.Failure -> TODO()
        ApiResponse.Loading -> MovieLoading()
        is ApiResponse.Success -> MovieInfo(Modifier.fillMaxSize().padding(10.dp), response.data)
    }

}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieInfo(
    modifier: Modifier = Modifier,
    movie: MovieModel
) {
    LazyColumn(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            Image(
                ImageBitmap.imageResource(id = R.drawable.drive_horizontal),
                contentDescription = movie.name,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.onBackground, RectangleShape)
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
                    text = movie.genre.joinToString(", ") { it },
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
                    text = "${
                        it.key.format(
                            DateTimeFormatter.ofPattern(
                                "EEEE", Locale.getDefault()
                            )
                        )
                            .replaceFirstChar { letter ->
                                if (letter.isLowerCase()) letter.titlecase(
                                    Locale.getDefault()
                                ) else letter.toString()
                            }
                    } ${it.key.dayOfMonth}.${it.key.monthValue}",
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

                        }
                    }
                }
            }
            itemsIndexed(it.value) { index, item ->

            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun MovieInfoPreview(
    modifier: Modifier = Modifier,
    movie: MovieModel = MovieMapper().toModel(
        MovieLongResponse(
            1,
            "Drive",
            "Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание Описание",
            listOf("комедия", "взрослое кино", "пантомима"),
            2011,
            "Россия",
            true,
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
        )
    }
}

//@Preview(showBackground = true)
@Composable
fun MovieLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Indicator()
    }
}

@Composable
fun Indicator(
    size: Dp = 32.dp, // indicator size
    sweepAngle: Float = 90f, // angle (lenght) of indicator arc
    color: Color = MaterialTheme.colorScheme.primary, // color of indicator arc line
    strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth //width of cicle and ar lines
) {
    ////// animation //////

    // docs recomend use transition animation for infinite loops
    // https://developer.android.com/jetpack/compose/animation
    val transition = rememberInfiniteTransition(label = "Анимация загрузки")

    // define the changing value from 0 to 360.
    // This is the angle of the beginning of indicator arc
    // this value will change over time from 0 to 360 and repeat indefinitely.
    // it changes starting position of the indicator arc and the animation is obtained
    val currentArcStartAngle by transition.animateValue(
        0,
        360,
        Int.VectorConverter,
        infiniteRepeatable(
            animation = tween(
                durationMillis = 1100,
                easing = LinearEasing
            )
        ), label = "Анимация загрузки"
    )

    ////// draw /////

    // define stroke with given width and arc ends type considering device DPI
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Square)
    }

    // draw on canvas
    Canvas(
        Modifier
            .progressSemantics() // (optional) for Accessibility services
            .size(size) // canvas size
            .padding(strokeWidth / 2) //padding. otherwise, not the whole circle will fit in the canvas
    ) {
        // draw "background" (gray) circle with defined stroke.
        // without explicit center and radius it fit canvas bounds
        drawCircle(Color.LightGray, style = stroke)

        // draw arc with the same stroke
        drawArc(
            color,
            // arc start angle
            // -90 shifts the start position towards the y-axis
            startAngle = currentArcStartAngle.toFloat() - 90,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = stroke
        )
    }
}