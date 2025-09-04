package com.example.cryptocurrencyappcompose.presentation.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencyappcompose.domain.use_case.auth.SignInUseCase
import com.example.cryptocurrencyappcompose.domain.use_case.auth.SignUpUseCase
import com.example.cryptocurrencyappcompose.presentation.auth.components.SignIn.SignInTabState
import com.example.cryptocurrencyappcompose.presentation.auth.components.SignUp.SignUpTabState
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

    private val _stateSignUpTab = mutableStateOf(SignUpTabState())
    val stateSignUpTab: State<SignUpTabState> = _stateSignUpTab
    fun updateSignUpNickname(text: String){_stateSignUpTab.value = _stateSignUpTab.value.copy(nicknameText = text)}
    fun updateSignUpEmail(text: String){_stateSignUpTab.value = _stateSignUpTab.value.copy(emailText = text)}
    fun updateSignUpPassword(text: String){_stateSignUpTab.value = _stateSignUpTab.value.copy(passwordText = text)}

    private val _isPasswordShownState = mutableStateOf(false)
    val isPasswordShownState: State<Boolean> = _isPasswordShownState
    fun changePasswordShownState(){
        _isPasswordShownState.value = !_isPasswordShownState.value
    }

    private val _stateSignInTab = mutableStateOf(SignInTabState())
    val stateSignInTab: State<SignInTabState> = _stateSignInTab
    fun updateSignInEmail(text: String){_stateSignInTab.value = _stateSignInTab.value.copy(emailText = text)}
    fun updateSignInPassword(text: String){_stateSignInTab.value = _stateSignInTab.value.copy(passwordText = text)}

    val currentPageInPager = mutableIntStateOf(0)

    private val _currentUser = mutableStateOf<FirebaseUser?>(auth.currentUser)
    val currentUser: State<FirebaseUser?> = _currentUser

    private val _isLoadingState = mutableStateOf(false)
    val isLoadingState: State<Boolean> = _isLoadingState

    private val _navigateToScreenList = mutableStateOf(false)
    val navigateToScreenList: State<Boolean> = _navigateToScreenList

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    fun registerNewUser(){
        viewModelScope.launch {
            _isLoadingState.value = true
            val res = signUpUseCase(
                _stateSignUpTab.value.nicknameText,
                _stateSignUpTab.value.emailText,
                _stateSignUpTab.value.passwordText
            )
            when (res){
                is AuthState.Authenticated -> {
                    _navigateToScreenList.value = true
                }
                is AuthState.Error -> {
                    _isLoadingState.value = false
                    _toastMessage.emit(res.message)
                }
                is AuthState.Unauthenticated -> {}
            }
        }
    }

    fun loginUser(){
        viewModelScope.launch {
            _isLoadingState.value = true
            val res = signInUseCase(_stateSignInTab.value.emailText,_stateSignInTab.value.passwordText)
            when (res){
                is AuthState.Authenticated -> {
                    _navigateToScreenList.value = true
                }
                is AuthState.Error -> {
                    _isLoadingState.value = false
                    _toastMessage.emit(res.message)
                }
                is AuthState.Unauthenticated -> {}
            }
        }
    }
}