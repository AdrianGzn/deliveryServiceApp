package com.alilopez.kt_demohilt.features.auth.domain.repositories

import com.alilopez.kt_demohilt.features.user.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isUserLoggedIn: Flow<Boolean>
    val userRole: Flow<String?>
    val userId: Flow<Int?>
    val userName: Flow<String?>

    suspend fun checkLoginStatus()
    fun setUserLoggedIn(user: User)
    fun logout()
}