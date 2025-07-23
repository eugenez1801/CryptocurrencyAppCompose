package com.example.cryptocurrencyappcompose.domain.use_case.auth

import com.example.cryptocurrencyappcompose.domain.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    operator fun invoke(): FirebaseUser?{
        return firebaseRepository.currentUser
    }
}