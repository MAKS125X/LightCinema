package com.example.lightcinema.data.auth.models

import android.content.SharedPreferences

class TokenManager(private val sharedPreferences: SharedPreferences) {

    fun getToken(): User? {
        val userId = sharedPreferences.getString(USER_TOKEN_KEY, null)
        val role = sharedPreferences.getString(USER_ROLE_KEY, null)
        val userName = sharedPreferences.getString(USER_USERNAME_KEY, null)
        return if (userId != null && role != null && !userName.isNullOrEmpty()) User(
            userId,
            userName,
            UserRole.valueOf(role)
        ) else null
    }

    fun saveToken(user: User) {
        sharedPreferences.edit()
            .putString(USER_TOKEN_KEY, user.userToken)
            .putString(USER_USERNAME_KEY, user.userName)
            .putString(USER_ROLE_KEY, user.role.name)
            .apply()
    }

//    fun saveToken(token: String, role: String) {
//        saveToken(User(token, role))
//    }

    fun deleteToken() {
        sharedPreferences.edit().remove(USER_TOKEN_KEY).apply()
        sharedPreferences.edit().remove(USER_ROLE_KEY).apply()
        sharedPreferences.edit().remove(USER_USERNAME_KEY).apply()
    }

    companion object {
        private val USER_TOKEN_KEY = "userToken"
        private val USER_ROLE_KEY = "userRole"
        private val USER_USERNAME_KEY = "userName"
    }
}