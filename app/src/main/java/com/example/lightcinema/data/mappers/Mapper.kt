package com.example.lightcinema.data.mappers

interface Mapper<Response, Model : Any> {
    fun toModel(value: Response): Model
    fun fromModel(value: Model): Response
}