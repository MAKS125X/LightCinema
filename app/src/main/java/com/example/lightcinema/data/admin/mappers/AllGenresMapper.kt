package com.example.lightcinema.data.admin.mappers

import com.example.lightcinema.data.admin.network.responses.AllGenresResponse
import com.example.lightcinema.data.common.Mapper
import com.example.lightcinema.ui.screens.admin.edit_movie.GenreModifiedModel

object AllGenresMapper : Mapper<AllGenresResponse, MutableList<GenreModifiedModel>> {
    override fun toModel(value: AllGenresResponse): MutableList<GenreModifiedModel> {
        return value.genres.map { GenreModifiedModel(it.id, it.name, false) }.toMutableList()
    }

    override fun fromModel(value: MutableList<GenreModifiedModel>): AllGenresResponse {
        TODO("Not yet implemented")
    }
}