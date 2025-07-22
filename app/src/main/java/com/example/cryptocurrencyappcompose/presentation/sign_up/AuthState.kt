package com.example.cryptocurrencyappcompose.presentation.sign_up

sealed class AuthState {//заменять на data class не нужно, поскольку требуются синглтоны
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    class Error(val message: String) : AuthState()
}