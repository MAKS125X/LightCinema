package com.example.lightcinema.ui.mappers

interface Mapper<Response : Any, Model : Any> {
    fun toModel(value: Response): Model
//    fun fromModel(value: Model): Response
}