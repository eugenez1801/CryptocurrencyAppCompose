package com.example.cryptocurrencyappcompose.presentation.coin_list.dialogs.search_dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cryptocurrencyappcompose.common.SearchType
import com.example.cryptocurrencyappcompose.presentation.coin_list.CoinListViewModel
import com.example.cryptocurrencyappcompose.presentation.coin_list.dialogs.search_dialog.components.DefaultRadioButton

@Composable
fun SearchDialog(
    showAlertDialog: MutableState<Boolean>,
    onConfirmation: (SearchType, String) -> Unit,
    viewModel: CoinListViewModel = viewModel()
) {
    val text = viewModel.textInTextFieldState.value
//    Log.d("TextProblem", "Text: ${text} ")
    val currentSearchType = viewModel.currentSearchTypeState.value

    val focusRequester = viewModel.focusRequesterState//для установки фокуса на текстФилд

    /*if (showAlertDialog.value) { это работает благодаря delay, но как то ненадежно, не хочу так
        LaunchedEffect(showAlertDialog.value) {
            // Ждем следующего кадра, чтобы TextField успел привязаться к FocusRequester
            delay(50) // Минимальная задержка (можно убрать, если не помогает)
            focusRequester.requestFocus()
        }
    }*/

    LaunchedEffect(Unit) {
//        viewModel.updateDialogsState("") при повороте экрана все тоже сбрасывается
        kotlinx.coroutines.delay(5)//все же нужен этот костыль
        focusRequester.requestFocus()
    }

    AlertDialog(
        title = {
            Text(text = "Choose option")
        },
        text = {
            Column(

            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DefaultRadioButton(
                        text = "Name",
                        selected = currentSearchType == SearchType.NAME
                    ) {
                        viewModel.updateDialogsState(searchType = SearchType.NAME)
                    }

                    DefaultRadioButton(
                        text = "Symbol",
                        selected = currentSearchType == SearchType.SYMBOL
                    ) {
                        viewModel.updateDialogsState(searchType = SearchType.SYMBOL)
                    }
                }

                Spacer(Modifier.height(10.dp))

                TextField(
                    value = text,
                    onValueChange = { newText ->
//                        Log.d("TextProblem", "New text: $newText")
                        if (/*newText из-за этого была ошибка*/text.length <= 28) viewModel.textInTextFieldState.value = newText/*проблема
                        с отображением при максимальной длине строки*/
//                        viewModel.updateDialogsState(newText.take(28)) не помогло
                    },
                    modifier = Modifier.focusRequester(focusRequester),
                    singleLine = true
                )
            }
        },
        onDismissRequest = {
            showAlertDialog.value = false
            viewModel.updateDialogsState("")
        },
        confirmButton = {
            TextButton(
                onClick = {
//                    Log.d("TextProblem", text)
                    onConfirmation(currentSearchType, text.trim())
                    showAlertDialog.value = false
                    viewModel.updateDialogsState("")
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    showAlertDialog.value = false
                    viewModel.updateDialogsState("")
                }
            ) {
                Text("Cancel")
            }
        }
    )
}