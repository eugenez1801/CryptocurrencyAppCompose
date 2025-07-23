package com.example.cryptocurrencyappcompose.presentation.auth

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cryptocurrencyappcompose.domain.use_case.auth.GetCurrentUserUseCase
import com.example.cryptocurrencyappcompose.domain.use_case.auth.SignInUseCase
import com.example.cryptocurrencyappcompose.domain.use_case.auth.SignUpUseCase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): ViewModel() {
    val nicknameTextState = mutableStateOf("")
    val emailTextState = mutableStateOf("")
    val passwordTextState = mutableStateOf("")

    val isPasswordShownState = mutableStateOf(false)

    val emailLoginTextState = mutableStateOf("")
    val passwordLoginTextState = mutableStateOf("")

    //для pager
    val currentPageInPager = mutableIntStateOf(0)

    suspend fun registerNewUser(): AuthState{
        return signUpUseCase(nicknameTextState.value,emailTextState.value, passwordTextState.value)
    }

    suspend fun loginUser(): AuthState{
        return signInUseCase(emailLoginTextState.value, passwordLoginTextState.value)
    }

    fun currentUser(): FirebaseUser? {
        return getCurrentUserUseCase()
    }
}