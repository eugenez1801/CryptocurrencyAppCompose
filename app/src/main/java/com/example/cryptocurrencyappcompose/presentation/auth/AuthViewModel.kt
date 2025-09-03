package com.example.cryptocurrencyappcompose.presentation.auth

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencyappcompose.domain.use_case.auth.SignInUseCase
import com.example.cryptocurrencyappcompose.domain.use_case.auth.SignUpUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
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

    val navigateToScreenList = mutableStateOf(false)

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    fun registerNewUser(){
        viewModelScope.launch {
            isLoadingState.value = true
            val res = signUpUseCase(nicknameTextState.value,emailTextState.value, passwordTextState.value)
            when (res){
                is AuthState.Authenticated -> {
                    navigateToScreenList.value = true
                }
                is AuthState.Error -> {
                    isLoadingState.value = false
                    _toastMessage.emit(res.message)
                }
                is AuthState.Unauthenticated -> {}
            }
        }
    }

    fun loginUser(){
        viewModelScope.launch {
            isLoadingState.value = true
            val res = signInUseCase(emailLoginTextState.value, passwordLoginTextState.value)
            when (res){
                is AuthState.Authenticated -> {
                    navigateToScreenList.value = true
                }
                is AuthState.Error -> {
                    isLoadingState.value = false
                    _toastMessage.emit(res.message)
                }
                is AuthState.Unauthenticated -> {}
            }
        }
    }
}