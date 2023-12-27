package com.example.lightcinema.data.admin

import com.example.lightcinema.data.admin.repository.AdminRepository

interface AdminModule {
    val adminRepository: AdminRepository
}