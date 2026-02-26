package com.alilopez.kt_demohilt.features.feature01.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _msn = MutableStateFlow("Cargando...")
    val msn = _msn.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000) // Simular carga
            _msn.value = "¡Bienvenido a Home!"
        }
    }
}