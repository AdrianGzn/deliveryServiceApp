package com.alilopez.kt_demohilt.features.auth.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.alilopez.kt_demohilt.features.auth.domain.repositories.AuthRepository
import com.alilopez.kt_demohilt.features.user.domain.entities.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthRepository {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    private val _isUserLoggedIn = MutableStateFlow(false)
    override val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    private val _userRole = MutableStateFlow<String?>(null)
    override val userRole: StateFlow<String?> = _userRole.asStateFlow()

    private val _userId = MutableStateFlow<Int?>(null)
    override val userId: StateFlow<Int?> = _userId.asStateFlow()

    private val _userName = MutableStateFlow<String?>(null)
    override val userName: StateFlow<String?> = _userName.asStateFlow()

    init {
        checkLoginStatusSync()
    }

    private fun checkLoginStatusSync() {
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)
        if (isLoggedIn) {
            _isUserLoggedIn.value = true
            _userId.value = sharedPreferences.getInt("user_id", -1).takeIf { it != -1 }
            _userRole.value = sharedPreferences.getString("user_role", null)
            _userName.value = sharedPreferences.getString("user_name", null)
        }
    }

    override suspend fun checkLoginStatus() {
        checkLoginStatusSync()
    }

    override fun setUserLoggedIn(user: User) {
        sharedPreferences.edit().apply {
            putBoolean("is_logged_in", true)
            putInt("user_id", user.id)
            putString("user_role", user.role)
            putString("user_name", user.name)
            apply()
        }
        
        _isUserLoggedIn.update { true }
        _userRole.update { user.role }
        _userId.update { user.id }
        _userName.update { user.name }
    }

    override fun logout() {
        sharedPreferences.edit().clear().apply()

        _isUserLoggedIn.update { false }
        _userRole.update { null }
        _userId.update { null }
        _userName.update { null }
    }
}