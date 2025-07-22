package com.example.cryptocurrencyappcompose.presentation.sign_up

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cryptocurrencyappcompose.R
import com.example.cryptocurrencyappcompose.presentation.Screen
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignUpViewModel = viewModel()
) {
    val emailText = viewModel.emailLoginTextState.value
    val passwordText = viewModel.passwordLoginTextState.value

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
                viewModel.emailLoginTextState.value = it
            },
            label = {
                Text(text = "Email address")
            },
            placeholder = {
                Text(text = "Your email")
            },
            modifier = Modifier
                .fillMaxWidth(0.7f)
        )

        Spacer(Modifier.height(10.dp))

        TextField(
            value = passwordText,
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
            onValueChange = {
                viewModel.passwordLoginTextState.value = it
            },
            label = {
                Text(text = "Password")
            },
            placeholder = {
                Text(text = "Your password")
            },
            visualTransformation = if (isPasswordShown) VisualTransformation.None
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
            },
            modifier = Modifier
                .fillMaxWidth(0.7f)
        )

        Spacer(Modifier.height(15.dp))

        Button(
            onClick = {
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
            },
            modifier = Modifier
                .fillMaxWidth(0.7f),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Login"
            )
        }

        /*Box(при таком подходе возникает эффект нажатия ненужный и неуместный
            modifier = Modifier
                .fillMaxWidth(0.7f)
        ){
            TextButton(
                onClick = {
                    navController.navigate(Screen.SignUpScreen.route)
                },
                modifier = Modifier
                    .height(30.dp),
                contentPadding = PaddingValues(
                    horizontal = 4.dp,
                    vertical = 0.dp
                )
            ) {
                Text(
                    text = "Don't have an account? Sign Up"
                )
            }
        }*/

        Box(
            Modifier.fillMaxWidth(0.7f)
        ){
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = 5.dp)
            ){
                Text(
                    text = "Don't have an account?",
                    fontSize = 15.sp
                )

                Text(
                    text = "Sign Up",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(start = 3.dp)
                        .clickable/*(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        )*/{
                            navController.navigate(Screen.SignUpScreen.route)
                        }
                )
            }
        }
    }
}