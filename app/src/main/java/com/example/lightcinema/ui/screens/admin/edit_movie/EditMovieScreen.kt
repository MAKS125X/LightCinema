package com.example.lightcinema.ui.screens.admin.edit_movie

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.example.lightcinema.R
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.data.share.CountryResponse
import com.example.lightcinema.data.share.GenreResponse
import com.example.lightcinema.data.share.MovieLongResponse
import com.example.lightcinema.data.share.MovieMapper
import com.example.lightcinema.data.share.SessionDateResponse
import com.example.lightcinema.ui.common.SessionInfoButton
import com.example.lightcinema.ui.common.StyledTextField
import com.example.lightcinema.ui.models.Country
import com.example.lightcinema.ui.models.Genre
import com.example.lightcinema.ui.models.MovieModel
import com.example.lightcinema.ui.models.SessionModel
import com.example.lightcinema.ui.screens.admin.addSession.AddSessionDialog
import com.example.lightcinema.ui.theme.LightCinemaTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("MutableCollectionMutableState")
@Composable
fun EditMovieScreen(
    viewModel: EditMovieViewModel = viewModel(factory = EditMovieViewModel.Factory),
    upPress: () -> Unit,
) {
    val genres = viewModel.genres.collectAsState()
    val countries = viewModel.countries.collectAsState()
    val movie = viewModel.movie.collectAsState(initial = null)

    val deleteMovieResponse = viewModel.deleteMovieResponse.collectAsState(initial = null)
    val deleteSessionResponse = viewModel.deleteSessionResponse.collectAsState(initial = null)

    val genreResponse = remember {
        mutableStateListOf<GenreModifiedModel>(
//            mutableListOf<GenreModifiedModel>()
        )
    }

    val context = LocalContext.current

    Log.d("AbobaDelete", deleteMovieResponse.value.toString())

    when (val response = deleteMovieResponse.value) {
        is ApiResponse.Failure -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, response.errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        ApiResponse.Loading -> {}

        is ApiResponse.Success -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "Фильм удалён", Toast.LENGTH_SHORT).show()
                upPress()
            }

        }

        null -> {}
    }

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
                viewModel.getMovieInfo()
            }
        }

        null -> {}
    }

    LaunchedEffect(Unit) {

        when (val a = genres.value) {
            is ApiResponse.Failure -> {}
            ApiResponse.Loading -> {}
            is ApiResponse.Success -> genreResponse.addAll(a.data)
        }
    }

    val countryResponse = remember {
        mutableStateListOf<CountryModifiedModel>(
        )
    }

    LaunchedEffect(Unit) {
        when (val a = countries.value) {
            is ApiResponse.Failure -> {}
            ApiResponse.Loading -> {}
            is ApiResponse.Success -> countryResponse.addAll(a.data)
        }
    }


    val updateResponse = viewModel.updateResponse.collectAsState(initial = null)

    when (val response = updateResponse.value) {
        is ApiResponse.Failure -> {
            Log.d("Aboba1", response.toString())
            LaunchedEffect(Unit) {
                Toast.makeText(context, response.errorMessage.toString(), Toast.LENGTH_LONG).show()
            }
        }

        ApiResponse.Loading -> {}
        is ApiResponse.Success -> {
            Log.d("Aboba2", response.toString())
            LaunchedEffect(Unit) {
                Toast.makeText(
                    context,
                    if (viewModel.movieId >= 0) "Фильм обновлён!" else "Фильм добавлен!",
                    Toast.LENGTH_LONG
                ).show()
                upPress()
            }
        }

        else -> {}
    }

    Log.d("Aboba", movie.value.toString())

    when (val movieResponse = movie.value) {
        is ApiResponse.Failure -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, movieResponse.errorMessage.toString(), Toast.LENGTH_LONG).show()
            }
        }

        ApiResponse.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    trackColor = MaterialTheme.colorScheme.primary,
                )
            }
        }

        is ApiResponse.Success -> {
            val name = remember { mutableStateOf(movieResponse.data.name) }
            val posterLink = remember { mutableStateOf(movieResponse.data.posterLink) }
            val imageLink = remember { mutableStateOf(movieResponse.data.imageLink) }

            val movieGenres = remember { mutableStateOf(movieResponse.data.genres) }
            val movieCountries = remember { mutableStateOf(movieResponse.data.countries) }

            val description = remember { mutableStateOf(movieResponse.data.description) }

            val sessions = remember { mutableStateOf(movieResponse.data.sessionMap) }

            val c: Calendar = Calendar.getInstance()
            c.set(movieResponse.data.createdYear + 1, 0, 0)

            val selectedDate = remember {
                mutableLongStateOf(c.timeInMillis)
            }

            LaunchedEffect(Unit) {
                movieResponse.data.genres.forEach { genre ->
                    genreResponse.map { response ->
                        if (response.id == genre.id) response.isSelected = true
                    }
                }
                movieResponse.data.countries.forEach { country ->
                    countryResponse.map { response ->
                        if (response.id == country.id) response.isSelected = true
                    }
                }
            }

            var showAddSessionDialog by remember {
                mutableStateOf(false)
            }

            if (showAddSessionDialog) {
                AddSessionDialog(
                    onDismissRequest = { showAddSessionDialog = false },
                    movieId = viewModel.movieId,
                    onSuccessRequest = {
                        showAddSessionDialog = false
                        viewModel.getMovieInfo()
                    }
                )
            }


            EditMovieScreenSuccess(
                viewModel.movieId,
                name = name,
                posterLink = posterLink,
                imageLink = imageLink,
//                movieGenres = movieGenres,
//                movieCountries = movieCountries,
                allGenres = genreResponse,
                description = description,
                allCountries = countryResponse,
                sessions = sessions,
                date = selectedDate,
                updateMovie = {
                    viewModel.updateMovieInfo(
                        name.value,
                        posterLink.value,
                        imageLink.value,
                        description.value,
                        selectedDate.longValue,
                        movieResponse.data.ageLimit,
                        genreResponse.filter { it.isSelected }.map { Genre(it.id, it.name) },
                        countryResponse.filter { it.isSelected }.map { Country(it.id, it.name) }
                    )
                },
                addMovie = {

                },
                addSession = {
                    showAddSessionDialog = true
                },
                deleteSession = {
                    viewModel.deleteSession(it)
                },
                deleteMovie = { viewModel.deleteMovie() },
                getMovie = { viewModel.getMovieInfo() }
            )
        }

        null -> {
            val name = remember { mutableStateOf("") }
            val posterLink = remember { mutableStateOf("") }
            val imageLink = remember { mutableStateOf("") }

            val movieGenres = remember {
                mutableStateOf(listOf<Genre>())
            }

            val movieCountries = remember {
                mutableStateOf(listOf<Country>())
            }

            val description = remember { mutableStateOf("") }

            val selectedDate = remember {
                mutableLongStateOf(0)
            }

            EditMovieScreenSuccess(
                viewModel.movieId,
                name = name,
                posterLink = posterLink,
                imageLink = imageLink,
//                movieGenres = movieGenres,
//                movieCountries = movieCountries,
                allGenres = genreResponse,
                description = description,
                allCountries = countryResponse,
                sessions = null,
                date = selectedDate,
                updateMovie = {
//
                },
                addMovie = {
                    viewModel.addMovieInfo(
                        name.value,
                        posterLink.value,
                        imageLink.value,
                        description.value,
                        selectedDate.longValue,
                        0,
                        genreResponse.filter { it.isSelected }.map { Genre(it.id, it.name) },
                        countryResponse.filter { it.isSelected }.map { Country(it.id, it.name) },

                        )
                },
                deleteMovie = { viewModel.deleteMovie() },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditMovieScreenSuccess(
    movieId: Int = -1,
    name: MutableState<String>,
    posterLink: MutableState<String>,
    imageLink: MutableState<String>,
//    movieGenres: MutableState<List<Genre>>,
//    movieCountries: MutableState<List<Country>>,
    date: MutableState<Long>,
    allGenres: SnapshotStateList<GenreModifiedModel>,
    description: MutableState<String>,
    allCountries: SnapshotStateList<CountryModifiedModel>,
    sessions: MutableState<Map<String, List<SessionModel>>>?,
    updateMovie: () -> Unit,
    addMovie: () -> Unit,
    addSession: () -> Unit = {},
    deleteSession: (Int) -> Unit = {},
    deleteMovie: () -> Unit = {},
    getMovie: () -> Unit = {},
) {
    var expandedGenres by remember { mutableStateOf(false) }
    var expandedCountries by remember { mutableStateOf(false) }

    var posterText by remember {
        mutableStateOf(posterLink.value)
    }

    var imageText by remember {
        mutableStateOf(imageLink.value)
    }

    var selectedSession by remember {
        mutableIntStateOf(-1)
    }

    Log.d("Aboba", date.value.toString())

    val calendar = Calendar.getInstance()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = calendar.timeInMillis,
        initialDisplayMode = DisplayMode.Input
    )

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var openDeleteMovieDialog by remember { mutableStateOf(false) }

    when {
        openDeleteMovieDialog -> AlertDialog(
            title = {
                Text(text = "Удалить фильм?")
            },
            modifier = Modifier.border(
                1.dp,
                MaterialTheme.colorScheme.onBackground,
                AlertDialogDefaults.shape
            ),
            text = {
                Text(text = "Вы действительно хотите удалить данный фильм?")
            },
            onDismissRequest = {
                openDeleteMovieDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        deleteMovie()
                        openDeleteMovieDialog = false
                    }
                ) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDeleteMovieDialog = false
                    }
                ) {
                    Text("Нет")
                }
            }
        )
    }


    var openDeleteSessionDialog by remember { mutableStateOf(false) }

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
                        deleteSession(selectedSession)
                        openDeleteSessionDialog = false
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


    val dateFormatter = SimpleDateFormat("yyyy", Locale.getDefault())

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    date.value = datePickerState.selectedDateMillis!!
                }) {
                    Text(text = "Подтвердить")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) {
                    Text(text = "Скрыть")
                }
            },
            colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceTint)
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false
            )
        }
    }

    var refreshing by remember {
        mutableStateOf(false)
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing), onRefresh = {
            refreshing = false
            getMovie()
        }, modifier = Modifier.fillMaxSize()
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Box {
                SubcomposeAsyncImage(
                    model = posterLink.value, contentDescription = posterLink.value,
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

                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                    IconButton(onClick = { openDeleteMovieDialog = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Дата",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }


            }

            Spacer(modifier = Modifier.height(10.dp))
            SubcomposeAsyncImage(
                model = imageLink.value, contentDescription = imageLink.value,
                contentScale = ContentScale.FillWidth,
                error = {
                    Image(
                        ImageBitmap.imageResource(R.drawable.no_photo_ver),
                        contentDescription = "Нельзя загрузить изображение"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(10.dp))
            StyledTextField(
                Modifier.fillMaxWidth(),
                posterText,
                { posterText = it },
                "Ссылка (горизонтальный постер)",
                maxLines = 1,
                trailingIcon = {
                    IconButton(onClick = { posterLink.value = posterText }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "Дата",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            StyledTextField(
                Modifier.fillMaxWidth(),
                imageText,
                { imageText = it },
                "Ссылка (вертикальный постер)",
                maxLines = 1,
                trailingIcon = {
                    IconButton(onClick = { imageLink.value = imageText }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "Дата",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            StyledTextField(
                Modifier.fillMaxWidth(),
                name.value,
                { name.value = it },
                "Название фильма"
            )
            Spacer(modifier = Modifier.height(10.dp))
            ExposedDropdownMenuBox(
                expanded = expandedGenres,
                onExpandedChange = {
                    expandedGenres = !expandedGenres
                }
            ) {
                StyledTextField(
                    value = allGenres.filter { it.isSelected }
                        .joinToString(separator = ", ") { it.name },
                    onValueChange = {},
                    labelText = "Жанры",
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGenres)
                    },
//                colors = alertDialogsTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedGenres,
                    onDismissRequest = { expandedGenres = false }
                ) {
                    allGenres.forEachIndexed { index, genre ->
                        Row {
                            Checkbox(
                                checked = genre.isSelected,
                                onCheckedChange = {
                                    allGenres[index] =
                                        allGenres[index].copy(isSelected = it)
//                                genre.isSelected = !genre.isSelected
//                                                country.isSelected = !country.isSelected
                                })
                            DropdownMenuItem(
                                text = { Text(text = genre.name) },
                                onClick = {
                                    allGenres[index] =
                                        allGenres[index].copy(isSelected = !genre.isSelected)
//                                genre.isSelected = !genre.isSelected
//                                                country.isSelected = !country.isSelected
                                },
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            StyledTextField(
                Modifier.fillMaxWidth(),
                description.value,
                { description.value = it },
                "Описание фильма",
                singleLine = false,
                minLines = 5
            )
            Spacer(modifier = Modifier.height(10.dp))
            ExposedDropdownMenuBox(
                expanded = expandedCountries,
                onExpandedChange = {
                    expandedCountries = !expandedCountries
                },

                ) {
                StyledTextField(
                    value = allCountries.filter { it.isSelected }
                        .joinToString(separator = ", ") { it.name },
                    onValueChange = {},
                    labelText = "Страны",
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCountries)
                    },
//                colors = alertDialogsTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedCountries,
                    onDismissRequest = { expandedCountries = false }
                ) {
                    allCountries.forEachIndexed { index, country ->
                        Row {
                            Checkbox(
                                checked = country.isSelected,
                                onCheckedChange = {
                                    allCountries[index] =
                                        allCountries[index].copy(isSelected = it)
//                                country.isSelected = !country.isSelected
                                })
                            DropdownMenuItem(
                                text = { Text(text = country.name) },
                                onClick = {
                                    allCountries[index] =
                                        allCountries[index].copy(isSelected = !country.isSelected)
//                                country.isSelected = !country.isSelected
                                },
                            )
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(10.dp))
            StyledTextField(
                value = dateFormatter.format(Date(date.value)) ?: "Выберите дату",
                onValueChange = {},
                readOnly = true,
                labelText = "Год выхода",
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = "Дата",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                modifier = Modifier
//                colors = alertDialogsTextFieldColors()
            )
            Spacer(modifier = Modifier.height(10.dp))
            when (val a = sessions?.value) {
                null -> {
                    Button(
                        onClick = { addMovie() },
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

                else -> {
                    Text(
                        text = "Сеансы:",
                        fontSize = 26.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,

                        ) {
                        Button(
                            onClick = addSession,
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                        ) {
                            Text(
                                text = "Добавить",
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(4.dp, 4.dp),
                                fontSize = 12.sp
                            )
                        }
                    }
                    a.forEach {

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
                                    ), additionInfo = ""
                                ) {
                                    selectedSession = session.id
                                    openDeleteSessionDialog = true
                                }
                            }
                        }
                    }
                    Button(
                        onClick = { updateMovie() },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.width(250.dp)
                    ) {
                        Text(
                            text = "Обновить фильм",
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


@Preview(showBackground = true)
@Composable
fun EditMovieScreenSuccessPreview(
    movie: MovieModel = MovieMapper.toModel(
        MovieLongResponse(
            1,
            "Drive",
            "Молчаливый водитель спасает девушку от гангстеров. Неонуар с Райаном Гослингом и пульсирующим саундтреком",
            genres = listOf(
                GenreResponse(1, "криминал"),
                GenreResponse(2, "драма"),
                GenreResponse(3, "триллер")
            ),
            countries = listOf(CountryResponse(1, "США")),
            createdYear = 2002,
            ageLimit = 18,
            imageLink = "https://avatars.mds.yandex.net/get-kinopoisk-image/1946459/af69a221-5921-4186-b700-2197d39e8362/1920x",
            posterLink = "https://avatars.mds.yandex.net/get-kinopoisk-image/1704946/66b27e0c-9f85-424c-bfb0-415bd8475bc8/1920x",
            sessions = listOf(
                SessionDateResponse(6, "2023-12-24 14:47", 1000),
                SessionDateResponse(6, "2023-12-24 12:30", 20),
                SessionDateResponse(6, "2023-12-25 14:47", 1000)
            )
        )
    )
) {
    val name = remember {
        mutableStateOf(movie.name)
    }
    val posterLink = remember {
        mutableStateOf(movie.posterLink)
    }
    val imageLink = remember {
        mutableStateOf(movie.imageLink)
    }
    val genres = remember {
        mutableStateOf(movie.genres)
    }

    val countries = remember {
        mutableStateOf(movie.countries)
    }

    val allGenres = remember {
        mutableStateListOf(
            GenreModifiedModel(1, "криминал", false),
            GenreModifiedModel(2, "драма", false),
            GenreModifiedModel(3, "триллер", false)
        )
    }
    val description = remember {
        mutableStateOf(
            movie.description
        )
    }
    val allCountries = remember {
        mutableStateListOf(
            CountryModifiedModel(1, "Россия", false),
            CountryModifiedModel(2, "США", false),
            CountryModifiedModel(3, "Китай", false)
        )
    }
    val sessions = remember {

        mutableStateOf<Map<String, List<SessionModel>>>(
            movie.sessionMap
        )
    }
    val selectedDate = remember {
        mutableLongStateOf(0)
    }
    LightCinemaTheme {

        EditMovieScreenSuccess(
            name = name,
            posterLink = posterLink,
            imageLink = imageLink,
//            movieGenres = genres,
//            movieCountries = countries,
            date = selectedDate,
            allGenres = allGenres,
            description = description,
            allCountries = allCountries,
            sessions = null,
            updateMovie = {},
            addMovie = {},
            addSession = {},
            deleteSession = {},
            deleteMovie = {},
//            getMovie = {}
        )
    }
}