package com.example.lightcinema.data.mappers

import com.example.lightcinema.data.visitor.network.responses.MovieLongResponse
import com.example.lightcinema.ui.screens.movie_info.MovieModel
import com.example.lightcinema.ui.screens.movie_info.SessionModel
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object MovieMapper : Mapper<MovieLongResponse, MovieModel> {
    override fun toModel(value: MovieLongResponse): MovieModel {

        val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val sessions =
            value.sessions.groupBy({ LocalDateTime.parse(it.dateTime, formatterDate).toLocalDate() }, {
                Triple<Int, LocalTime, Int>(
                    it.id, LocalDateTime.parse(it.dateTime, formatterDate).toLocalTime(), it.minPrice
                )
            })

        return MovieModel(
            value.id,
            value.name,
            value.description,
            value.genres,
            value.createdYear,
            value.countries,
            value.onlyAdult,
            value.imageLink,
            sessions.mapKeys {
                "${
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
                } ${it.key.dayOfMonth}.${it.key.monthValue}"
            }.mapValues {
                it.value.map { triple ->
                    SessionModel(
                        triple.first, triple.second.format(
                            DateTimeFormatter.ofPattern(
                                "HH:mm", Locale.getDefault()
                            )
                        ), triple.third
                    )
                }
            }
        )
    }

    override fun fromModel(value: MovieModel): MovieLongResponse {
        TODO("Not yet implemented")
    }
}
