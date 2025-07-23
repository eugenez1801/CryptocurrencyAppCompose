package com.example.cryptocurrencyappcompose.domain.use_case.auth

import com.example.cryptocurrencyappcompose.domain.repository.FirebaseRepository
import com.example.cryptocurrencyappcompose.presentation.auth.AuthState
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthState{
        return firebaseRepository.signIn(email, password)
    }
}