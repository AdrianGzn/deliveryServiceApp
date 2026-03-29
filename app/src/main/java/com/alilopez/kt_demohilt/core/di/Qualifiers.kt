package com.alilopez.kt_demohilt.core.di

import javax.inject.Qualifier

// Qualifiers for Hilt Dependency Injection
// Add more here if needed for multiple instances of the same type

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DelivaryServiceRetrofit
