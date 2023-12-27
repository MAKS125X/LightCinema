package com.example.lightcinema.data.admin.mappers

import com.example.lightcinema.data.admin.network.responses.AllCountriesResponse
import com.example.lightcinema.data.common.Mapper
import com.example.lightcinema.ui.screens.admin.edit_movie.CountryModifiedModel


object AllCountriesMapper : Mapper<AllCountriesResponse, MutableList<CountryModifiedModel>> {
    override fun toModel(value: AllCountriesResponse): MutableList<CountryModifiedModel> {
        return value.countries.map { CountryModifiedModel(it.id, it.name, false) }.toMutableList()
    }

    override fun fromModel(value: MutableList<CountryModifiedModel>): AllCountriesResponse {
        TODO("Not yet implemented")
    }
}