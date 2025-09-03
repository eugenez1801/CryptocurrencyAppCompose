package com.example.cryptocurrencyappcompose.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cryptocurrencyappcompose.presentation.Screen
import com.example.cryptocurrencyappcompose.presentation.auth.components.SignInTab
import com.example.cryptocurrencyappcompose.presentation.auth.components.SignUpTab
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val nicknameText = viewModel.nicknameTextState.value
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

    val isLoading = viewModel.isLoadingState.value

    val navigateToScreenList = viewModel.navigateToScreenList.value

    LaunchedEffect(navigateToScreenList) {
        if (navigateToScreenList) {
            navController.navigate(Screen.CoinListScreen.route) {
                popUpTo(Screen.AuthScreen.route) {
                    inclusive = true
                }
            }
            //сбрасывать navigateToScreenList до false не нужно, тк VM все равно уничтожится
        }
    }

    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        if (!isLoading){
            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                Spacer(
                    Modifier
                        .weight(1f)
                )
                TabRow(
                    selectedTabIndex = tabIndex,
                    indicator = { pos ->
                        SecondaryIndicator(
                            modifier = Modifier
                                .tabIndicatorOffset(currentTabPosition = pos[tabIndex]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.CenterHorizontally)
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
                    state = pagerState,
                    modifier = Modifier
                        .weight(2f, fill = true)
                        .padding(top = 15.dp)
                ) { index ->
                    when (index) {
                        0 -> {
                            SignInTab(
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
                                onLoginClick = { viewModel.loginUser() }
                            )
                        }

                        1 -> {
                            SignUpTab(
                                nicknameText = nicknameText,
                                onNicknameTextChange = { newText ->
                                    viewModel.nicknameTextState.value = newText
                                },
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
                                onSignUpClick = { viewModel.registerNewUser() }
                            )
                        }
                    }
                }
            }
        }
        else CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}