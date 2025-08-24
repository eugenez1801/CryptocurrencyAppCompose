package com.example.cryptocurrencyappcompose.data.repository

import com.example.cryptocurrencyappcompose.domain.repository.FirebaseRepository
import com.example.cryptocurrencyappcompose.presentation.auth.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): FirebaseRepository {

    override val currentUser = firebaseAuth.currentUser

    override suspend fun signUp(nickname: String, email: String, password: String): AuthState {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.updateProfile(userProfileChangeRequest {
                displayName = nickname
                photoUri = null
            })?.await()
            AuthState.Authenticated
        }
        catch (e: Exception){
            AuthState.Error(e.message!!)
        }
    }

    override suspend fun signIn(email: String, password: String): AuthState {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            AuthState.Authenticated
        }
        catch (e: Exception){
            AuthState.Error(e.message!!)
        }
    }

    override suspend fun signOut(): AuthState {
        return try {
            firebaseAuth.signOut()
            AuthState.Unauthenticated
        }
        catch (e: Exception){
            AuthState.Error(e.message!!)
        }
    }
}