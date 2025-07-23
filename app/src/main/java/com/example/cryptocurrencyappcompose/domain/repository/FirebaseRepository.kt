package com.example.cryptocurrencyappcompose.domain.repository

import com.example.cryptocurrencyappcompose.presentation.auth.AuthState
import com.google.firebase.auth.FirebaseUser

interface FirebaseRepository {
    val currentUser: FirebaseUser?

    suspend fun signUp(nickname: String, email: String, password: String): AuthState

    suspend fun signIn(email: String, password: String): AuthState

    suspend fun signOut(): AuthState
}