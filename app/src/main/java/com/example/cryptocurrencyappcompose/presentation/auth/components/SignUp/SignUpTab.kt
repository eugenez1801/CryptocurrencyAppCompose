package com.example.cryptocurrencyappcompose.presentation.auth.components.SignUp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.cryptocurrencyappcompose.R

@Composable
fun SignUpTab(
    state: SignUpTabState,
    onNicknameTextChange: (String) -> Unit,
    onEmailTextChange: (String) -> Unit,
    onPasswordTextChange: (String) -> Unit,
    isPasswordShown: Boolean,
    onIsPasswordShownChange: () -> Unit,
    onSignUpClick: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TextField(
            value = state.nicknameText,
            onValueChange = {
                onNicknameTextChange(it)
            },
            label = {
                Text(text = "Your nickname in app")
            },
            placeholder = {
                Text(text = "Your nickname in app")
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(0.7f)
        )

        Spacer(Modifier.height(10.dp))

        TextField(
            value = state.emailText,
            onValueChange = {
                onEmailTextChange(it)
            },
            label = {
                Text(text = "Email address")
            },
            placeholder = {
                Text(text = "Your email")
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(0.7f)
        )

        Spacer(Modifier.height(10.dp))

        TextField(
            value = state.passwordText,
            onValueChange = {
                onPasswordTextChange(it)
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
                        onIsPasswordShownChange()
                    },
                ) {
                    Icon(
                        painter = if (isPasswordShown) painterResource(R.drawable.ic_eye)
                        else painterResource(R.drawable.ic_eye_slash),
                        contentDescription = "Show password"
                    )
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(0.7f)
        )

        Spacer(Modifier.height(15.dp))

        Button(
            onClick = {
                onSignUpClick()
            },
            modifier = Modifier
                .fillMaxWidth(0.7f),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Sign Up"
            )
        }
    }
}