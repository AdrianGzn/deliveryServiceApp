package com.alilopez.kt_demohilt.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.features.user.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole.asStateFlow()

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            _isUserLoggedIn.value = userRepository.isUserLoggedIn()
        }
    }

    fun setUserLoggedIn(role: String) {
        _isUserLoggedIn.value = true
        _userRole.value = role
    }

    fun logout() {
        viewModelScope.launch {
            // Aquí implementarías la lógica de logout (limpiar DataStore)
            _isUserLoggedIn.value = false
            _userRole.value = null
        }
    }
}