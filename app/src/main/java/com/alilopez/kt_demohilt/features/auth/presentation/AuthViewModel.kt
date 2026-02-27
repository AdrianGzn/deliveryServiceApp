package com.alilopez.kt_demohilt.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.features.auth.domain.repositories.AuthRepository
import com.alilopez.kt_demohilt.features.user.domain.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val isUserLoggedIn = authRepository.isUserLoggedIn
    val userRole = authRepository.userRole
    val userId = authRepository.userId
    val userName = authRepository.userName

    init {
        viewModelScope.launch {
            authRepository.checkLoginStatus()
        }
    }

    fun setUserLoggedIn(user: User) {
        authRepository.setUserLoggedIn(user)
    }

    fun logout() {
        authRepository.logout()
    }
}