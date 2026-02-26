package com.alilopez.kt_demohilt.features.pets.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alilopez.kt_demohilt.features.pets.domain.model.Pet
import com.alilopez.kt_demohilt.features.pets.domain.usecases.*
import com.alilopez.kt_demohilt.features.pets.presentation.PetsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetsViewModel @Inject constructor(
    private val getPetsUseCase: GetPetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PetsUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        loadPets()
    }

    fun loadPets() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            runCatching {
                getPetsUseCase()
            }.onSuccess { pets ->
                _uiState.update { it.copy(isLoading = false, pets = pets) }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(isLoading = false, error = exception.message)
                }
            }
        }
    }


    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}