package com.example.cryptocurrencyappcompose.presentation.auth

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    class Error(val message: String) : AuthState()
}