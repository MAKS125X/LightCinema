package com.example.lightcinema.ui.mappers

import android.annotation.SuppressLint
import com.example.lightcinema.data.visitor.network.responses.SessionDateResponse
import com.example.lightcinema.ui.screens.filminfo.MovieModel
import com.example.lightcinema.ui.screens.filminfo.SessionModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("SimpleDateFormat")
fun main() {

    val sessions = listOf<SessionDateResponse>(
        SessionDateResponse(
            1, "2023-12-10 12:00", 300
        ),
        SessionDateResponse(
            2, "2023-12-10 14:00", 300
        ),
        SessionDateResponse(
            3, "2023-12-11 12:00", 300
        ),
        SessionDateResponse(
            4, "2023-12-11 14:00", 300
        ),
        SessionDateResponse(
            5, "2023-12-10 16:00", 400
        ),
        SessionDateResponse(
            6, "2023-12-11 16:00", 500
        ),
        SessionDateResponse(
            5, "2023-12-10 18:00", 400
        ),
        SessionDateResponse(
            6, "2023-12-11 18:00", 500
        ),
        SessionDateResponse(
            5, "2023-12-10 20:00", 400
        ),
        SessionDateResponse(
            6, "2023-12-11 20:00", 500
        ),
        SessionDateResponse(
            5, "2023-12-10 22:00", 400
        ),
        SessionDateResponse(
            6, "2023-12-11 22:00", 500
        ),
    )

    val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    val mapped = sessions.groupBy({ LocalDateTime.parse(it.date, formatterDate).toLocalDate() }, {
        SessionModel(
            it.id, LocalDateTime.parse(it.date, formatterDate).toLocalTime(), it.minPrice
        )
    })

    mapped.forEach {

//        val sdf = SimpleDateFormat.getDateInstance().format(it.key)
//        val sdf = SimpleDateFormat("EEE").format(it.key)
        println("${
            it.key.format(
                DateTimeFormatter.ofPattern(
                    "EEEE", Locale.getDefault()
                )
            )
                .replaceFirstChar { letter -> if (letter.isLowerCase()) letter.titlecase(Locale.getDefault()) else letter.toString() }
        } ${it.key.dayOfMonth}.${it.key.monthValue}")
    }

    val newFormatterDate = DateTimeFormatter.ofPattern("yyyy-MMMM-dd HH:mm")
    println(LocalDateTime.parse("2023-12-10 12:00", formatterDate) )

}