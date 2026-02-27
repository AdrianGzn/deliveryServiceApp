package com.alilopez.kt_demohilt.features.auth.data.repositories

import com.alilopez.kt_demohilt.features.auth.domain.repositories.AuthRepository
import com.alilopez.kt_demohilt.features.user.domain.entities.User
import com.alilopez.kt_demohilt.features.user.domain.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userRepository: UserRepository
) : AuthRepository {

    private val _isUserLoggedIn = MutableStateFlow(false)
    override val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    private val _userRole = MutableStateFlow<String?>(null)
    override val userRole: StateFlow<String?> = _userRole.asStateFlow()

    private val _userId = MutableStateFlow<Int?>(null)
    override val userId: StateFlow<Int?> = _userId.asStateFlow()

    private val _userName = MutableStateFlow<String?>(null)
    override val userName: StateFlow<String?> = _userName.asStateFlow()

    init {
        // No podemos llamar a suspend functions en init
        // Mejor hacerlo cuando se necesite
    }

    override suspend fun checkLoginStatus() {
        try {
            val isLoggedIn = userRepository.isUserLoggedIn()
            _isUserLoggedIn.update { isLoggedIn }
        } catch (e: Exception) {
            _isUserLoggedIn.update { false }
        }
    }

    override fun setUserLoggedIn(user: User) {
        _isUserLoggedIn.update { true }
        _userRole.update { user.role }
        _userId.update { user.id }
        _userName.update { user.name }
    }

    override fun logout() {
        _isUserLoggedIn.update { false }
        _userRole.update { null }
        _userId.update { null }
        _userName.update { null }
    }
}