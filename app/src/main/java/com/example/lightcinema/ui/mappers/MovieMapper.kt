package com.example.lightcinema.ui.mappers

import com.example.lightcinema.data.visitor.network.responses.MovieLongResponse
import com.example.lightcinema.ui.screens.filminfo.MovieModel
import com.example.lightcinema.ui.screens.filminfo.SessionModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MovieMapper : Mapper<MovieLongResponse, MovieModel> {
    override fun toModel(value: MovieLongResponse): MovieModel {

        val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        return MovieModel(
            value.id,
            value.name,
            value.description,
            value.genre,
            value.createdYear,
            value.country,
            value.onlyAdult,
            value.imageLink,
            value.sessions.groupBy({ LocalDateTime.parse(it.date, formatterDate).toLocalDate() }, {
                SessionModel(
                    it.id, LocalDateTime.parse(it.date, formatterDate).toLocalTime(), it.minPrice
                )
            })
        )
    }
}