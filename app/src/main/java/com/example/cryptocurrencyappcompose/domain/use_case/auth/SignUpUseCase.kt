package com.example.cryptocurrencyappcompose.domain.use_case.auth

import com.example.cryptocurrencyappcompose.domain.repository.FirebaseRepository
import com.example.cryptocurrencyappcompose.presentation.auth.AuthState
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(nickname: String, email: String, password: String): AuthState {
        return firebaseRepository.signUp(nickname, email, password)
    }
}