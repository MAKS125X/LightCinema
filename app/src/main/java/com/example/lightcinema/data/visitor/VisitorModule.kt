package com.example.lightcinema.data.visitor

import com.example.lightcinema.data.visitor.repository.VisitorRepository

interface VisitorModule {
    val visitorRepository: VisitorRepository
}