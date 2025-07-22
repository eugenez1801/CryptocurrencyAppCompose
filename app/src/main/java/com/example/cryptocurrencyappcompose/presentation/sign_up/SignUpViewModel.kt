package com.example.cryptocurrencyappcompose.presentation.sign_up

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cryptocurrencyappcompose.domain.use_case.sign_up.SignInUseCase
import com.example.cryptocurrencyappcompose.domain.use_case.sign_up.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase
): ViewModel() {
    val emailTextState = mutableStateOf("")
    val passwordTextState = mutableStateOf("")

    val isPasswordShownState = mutableStateOf(false)

    val emailLoginTextState = mutableStateOf("")
    val passwordLoginTextState = mutableStateOf("")

    suspend fun registerNewUser(): AuthState{
        return signUpUseCase(emailTextState.value, passwordTextState.value)
    }

    suspend fun loginUser(): AuthState{
        return signInUseCase(emailLoginTextState.value, passwordLoginTextState.value)
    }
}