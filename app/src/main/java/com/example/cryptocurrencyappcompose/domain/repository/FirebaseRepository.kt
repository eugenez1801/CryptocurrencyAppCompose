package com.example.cryptocurrencyappcompose.domain.repository

import com.example.cryptocurrencyappcompose.presentation.sign_up.AuthState

interface FirebaseRepository {
    suspend fun signUp(email: String, password: String): AuthState

    suspend fun signIn(email: String, password: String): AuthState
}