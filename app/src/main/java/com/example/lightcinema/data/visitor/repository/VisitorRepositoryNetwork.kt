package com.example.lightcinema.data.visitor.repository

import com.example.lightcinema.data.ApiResponse
import com.example.lightcinema.data.apiRequestFlow
import com.example.lightcinema.data.visitor.network.api.VisitorService
import com.example.lightcinema.data.visitor.network.requests.UserRequest
import com.example.lightcinema.data.visitor.network.responses.UserResponse
import kotlinx.coroutines.flow.Flow

class VisitorRepositoryNetwork(
    override val remoteDataSource: VisitorService
) : VisitorRepository {


}