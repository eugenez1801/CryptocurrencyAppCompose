package com.example.cryptocurrencyappcompose.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cryptocurrencyappcompose.presentation.Screen
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: SignUpViewModel = viewModel()
) {
    //все для экранов Sign InUp
    val emailText = viewModel.emailTextState.value
    val passwordText = viewModel.passwordTextState.value

    val emailLoginText = viewModel.emailLoginTextState.value
    val passwordLoginText = viewModel.passwordLoginTextState.value

    val isPasswordShown = viewModel.isPasswordShownState.value

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val tabList = listOf("Sign In", "Sign Up")
    val pagerState = rememberPagerState(
        initialPage = viewModel.currentPageInPager.intValue,
        pageCount = { tabList.size }
    )
    val tabIndex = pagerState.currentPage

    Column() {
        TabRow(
            selectedTabIndex = tabIndex,
            indicator = { pos ->
                SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(currentTabPosition = pos[tabIndex]),
                    color = Color.White
                )
            }
        ) {
            tabList.forEachIndexed { index, text ->
                Tab(
                    selected = false,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(text = text)
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState
        ) { index ->
            when (index) {
                0 -> {
                    SignInScreen(
                        emailText = emailLoginText,
                        onEmailTextChange = { newText ->
                            viewModel.emailLoginTextState.value = newText
                        },
                        passwordText = passwordLoginText,
                        onPasswordTextChange = { newText ->
                            viewModel.passwordLoginTextState.value = newText
                        },
                        isPasswordShown = isPasswordShown,
                        onIsPasswordShownChange = {
                            viewModel.isPasswordShownState.value =
                                !viewModel.isPasswordShownState.value
                        },
                        onLoginClick = {
                            scope.launch {
                                when (val resultSignIn = viewModel.loginUser()) {
                                    is AuthState.Authenticated -> {
                                        navController.navigate(Screen.CoinListScreen.route)
                                    }

                                    is AuthState.Error -> {
                                        Toast.makeText(
                                            context, resultSignIn.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    else -> {
                                        Toast.makeText(
                                            context, "Unexpected error",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                        /*navigateToSignUp = {
                            navController.navigate(Screen.SignUpScreen.route)
                        }*/
                    )
                }

                1 -> {
                    SignUpScreen(
                        emailText = emailText,
                        onEmailTextChange = { newText ->
                            viewModel.emailTextState.value = newText
                        },
                        passwordText = passwordText,
                        onPasswordTextChange = { newText ->
                            viewModel.passwordTextState.value = newText
                        },
                        isPasswordShown = isPasswordShown,
                        onIsPasswordShownChange = {
                            viewModel.isPasswordShownState.value =
                                !viewModel.isPasswordShownState.value
                        },
                        onSignUpClick = {
                            scope.launch {
                                when (val resultSignUp = viewModel.registerNewUser()) {
                                    is AuthState.Authenticated -> {
                                        navController.navigate(Screen.CoinListScreen.route)
                                    }

                                    is AuthState.Error -> {
                                        Toast.makeText(
                                            context, resultSignUp.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    else -> {
                                        Toast.makeText(
                                            context, "Unexpected error",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                        /*navigateToSignIn = {
                            navController.navigate(Screen.SignInScreen.route)
                        }*/
                    )
                }
            }
        }
    }
}