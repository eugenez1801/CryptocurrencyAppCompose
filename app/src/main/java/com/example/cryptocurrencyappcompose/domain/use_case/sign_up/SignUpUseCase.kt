package com.example.cryptocurrencyappcompose.domain.use_case.sign_up

import com.example.cryptocurrencyappcompose.domain.repository.FirebaseRepository
import com.example.cryptocurrencyappcompose.presentation.sign_up.AuthState
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthState {
        return firebaseRepository.signUp(email, password)
    }
}