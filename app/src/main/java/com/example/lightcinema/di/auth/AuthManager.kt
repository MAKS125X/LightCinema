package com.example.lightcinema.di.auth

//import android.content.SharedPreferences
//
//class AuthManager(private val tokenManager: TokenManager) {
//
//    var currentUser: User?
//
//    init {
//        val userId = sharedPreferences.getString("userId", null)
//        val role = sharedPreferences.getString("role", null)
//        currentUser = if (userId != null && role != null) User(
//            userId,
//            UserRole.valueOf(role)
//        ) else null
//    }
//
//    fun loginUser(userId: String, role: UserRole) {
//        sharedPreferences.edit()
//            .putString("userId", userId)
//            .putString("role", role.name)
//            .apply()
//    }
//
//    fun logoutUser() {
//        sharedPreferences.edit().remove("userId").apply()
//        sharedPreferences.edit().remove("role").apply()
//        currentUser = null
//    }
//}