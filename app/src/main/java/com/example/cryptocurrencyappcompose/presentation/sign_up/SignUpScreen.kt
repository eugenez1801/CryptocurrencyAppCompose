package com.example.cryptocurrencyappcompose.presentation.sign_up

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cryptocurrencyappcompose.R
import com.example.cryptocurrencyappcompose.presentation.Screen
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = viewModel()
) {
    val emailText = viewModel.emailTextState.value
    val passwordText = viewModel.passwordTextState.value

    val isPasswordShown = viewModel.isPasswordShownState.value

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = emailText,
            onValueChange = {
                viewModel.emailTextState.value = it
            },
            label = {
                Text(text = "Email address")
            },
            placeholder = {
                Text(text = "Your email")
            }
        )

        Spacer(Modifier.height(10.dp))

        TextField(
            value = passwordText,
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
            onValueChange = {
                viewModel.passwordTextState.value = it
            },
            label = {
                Text(text = "Password")
            },
            placeholder = {
                Text(text = "Your password")
            },
            visualTransformation = if (isPasswordShown)VisualTransformation.None
                else PasswordVisualTransformation(),
            trailingIcon = {
                IconToggleButton(
                    checked = isPasswordShown,
                    onCheckedChange = {
                        viewModel.isPasswordShownState.value = !viewModel.isPasswordShownState.value
                    },
                ) {
                    Icon(
                        painter = if (isPasswordShown) painterResource(R.drawable.ic_eye)
                                else painterResource(R.drawable.ic_eye_slash),
                        contentDescription = "Show password"
                    )
                }
            }
        )

        TextButton(
            onClick = {
                scope.launch {
                    val resultSignUp = viewModel.registerNewUser()
                    when (resultSignUp){
                        is AuthState.Authenticated -> {
                            navController.navigate(Screen.CoinListScreen.route)
                        }
                        is AuthState.Error -> {
                            Toast.makeText(context, resultSignUp.message,
                                Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(context, "Unexpected error",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        ) {
            Text(
                text = "Sign up"
            )
        }
    }
}