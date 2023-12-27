package com.example.lightcinema.data.common

interface Mapper<Response, Model : Any> {
    fun toModel(value: Response): Model
    fun fromModel(value: Model): Response
}