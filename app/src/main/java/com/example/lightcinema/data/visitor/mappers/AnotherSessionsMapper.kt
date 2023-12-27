package com.example.lightcinema.data.visitor.mappers

import com.example.lightcinema.data.common.Mapper
import com.example.lightcinema.data.visitor.network.responses.SessionsByMovieResponse
import com.example.lightcinema.ui.models.SessionModel
import com.example.lightcinema.ui.screens.visitor.reserving.AnotherSessionModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object AnotherSessionsMapper : Mapper<SessionsByMovieResponse, AnotherSessionModel> {
    override fun toModel(value: SessionsByMovieResponse): AnotherSessionModel {
        val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
        val formattedDate = LocalDate.parse(value.sessionsDate, formatterDate)
        val dateString = "${
            formattedDate.format(
                DateTimeFormatter.ofPattern(
                    "EEEE", Locale.getDefault()
                )
            ).replaceFirstChar { letter ->
                if (letter.isLowerCase()) letter.titlecase(
                    Locale.getDefault()
                ) else letter.toString()
            }
        }, ${
            formattedDate.format(
                DateTimeFormatter.ofPattern(
                    "dd MMMM", Locale.getDefault()
                )
            )
        }"
        return AnotherSessionModel(
            value.movieId,
            value.movieName.uppercase(),
            dateString,
            value.sessions.map {
                SessionModel(it.id, it.time, it.minPrice)
            })
    }

    override fun fromModel(value: AnotherSessionModel): SessionsByMovieResponse {
        TODO("Not yet implemented")
    }
}