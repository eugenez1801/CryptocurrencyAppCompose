package com.example.cryptocurrencyappcompose.domain.use_case.auth

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthStateListenerUseCase @Inject constructor(
    private val auth: FirebaseAuth
) {
    operator fun invoke(){
        auth.addAuthStateListener { firebaseAuth ->

        }
    }
}