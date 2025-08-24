package com.example.cryptocurrencyappcompose.presentation.auth

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cryptocurrencyappcompose.domain.use_case.auth.SignInUseCase
import com.example.cryptocurrencyappcompose.domain.use_case.auth.SignUpUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val auth: FirebaseAuth
): ViewModel() {
    val nicknameTextState = mutableStateOf("")
    val emailTextState = mutableStateOf("")
    val passwordTextState = mutableStateOf("")

    val isPasswordShownState = mutableStateOf(false)

    val emailLoginTextState = mutableStateOf("")
    val passwordLoginTextState = mutableStateOf("")

    val currentPageInPager = mutableIntStateOf(0)

    val currentUser = mutableStateOf<FirebaseUser?>(auth.currentUser)

    val isLoadingState = mutableStateOf(false)

    suspend fun registerNewUser(): AuthState{
        isLoadingState.value = true
        val res = signUpUseCase(nicknameTextState.value,emailTextState.value, passwordTextState.value)
        when (res){
            is AuthState.Authenticated -> {}//ничего не делаем
            else -> {isLoadingState.value = false}//если ошибка то ставим else
        }
        return res
    }

    suspend fun loginUser(): AuthState{
        isLoadingState.value = true
        val res = signInUseCase(emailLoginTextState.value, passwordLoginTextState.value)
        when (res){
            is AuthState.Authenticated -> {}//ничего не делаем
            else -> {isLoadingState.value = false}//если ошибка то ставим else
        }
        return res
    }
}