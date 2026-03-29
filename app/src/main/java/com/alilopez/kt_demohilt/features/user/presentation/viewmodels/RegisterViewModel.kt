package com.alilopez.kt_demohilt.features.user.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.features.auth.domain.repositories.AuthRepository
import com.alilopez.kt_demohilt.features.user.domain.usescase.UserRegisterUseCase
import com.alilopez.kt_demohilt.features.user.presentation.screens.RegisterUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRegisterUseCase: UserRegisterUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState: StateFlow<RegisterUIState> = _uiState.asStateFlow()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address.asStateFlow()

    private val _establishmentName = MutableStateFlow("")
    val establishmentName: StateFlow<String> = _establishmentName.asStateFlow()

    private val _establishmentAddress = MutableStateFlow("")
    val establishmentAddress: StateFlow<String> = _establishmentAddress.asStateFlow()

    private val _selectedRole = MutableStateFlow("customer")
    val selectedRole: StateFlow<String> = _selectedRole.asStateFlow()

    fun onNameChange(name: String) {
        _name.value = name
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun onAddressChange(address: String) {
        _address.value = address
    }

    fun onEstablishmentNameChange(name: String) {
        _establishmentName.value = name
    }

    fun onEstablishmentAddressChange(address: String) {
        _establishmentAddress.value = address
    }

    fun onRoleChange(role: String) {
        _selectedRole.value = role
    }

    fun onRegisterClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val user = userRegisterUseCase(
                    name = _name.value,
                    password = _password.value,
                    role = _selectedRole.value,
                    address = if (_address.value.isNotBlank()) _address.value else null,
                    establishmentName = if (_selectedRole.value == "seller") _establishmentName.value else null,
                    establishmentAddress = if (_selectedRole.value == "seller") _establishmentAddress.value else null
                )

                authRepository.setUserLoggedIn(user)

                _uiState.update {
                    it.copy(
                        isRegistered = true,
                        isLoading = false,
                        user = user
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al registrar usuario"
                    )
                }
            }
        }
    }
}