package com.example.cryptocurrencyappcompose.presentation.auth.components

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
fun SignInTab(
    emailText: String,
    onEmailTextChange: (String) -> Unit,
    passwordText: String,
    onPasswordTextChange: (String) -> Unit,
    isPasswordShown: Boolean,
    onIsPasswordShownChange: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TextField(
            value = emailText,
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
            value = passwordText,
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
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
                onLoginClick()
            },
            modifier = Modifier
                .fillMaxWidth(0.7f),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Login"
            )
        }
    }
}