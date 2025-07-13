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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.example.cryptocurrencyappcompose.common.SearchType
import com.example.cryptocurrencyappcompose.presentation.coin_list.dialogs.search_dialog.components.DefaultRadioButton

@Composable
fun SearchDialog(
    showAlertDialog: MutableState<Boolean>,
    onConfirmation: (SearchType, String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    val currentSearchType = remember { mutableStateOf(SearchType.NAME) }

    val focusRequester = remember { FocusRequester() }//для установки фокуса на текстФилд

    LaunchedEffect(Unit) {
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
                        selected = currentSearchType.value == SearchType.NAME
                    ) {
                        currentSearchType.value = SearchType.NAME
                    }

                    DefaultRadioButton(
                        text = "Symbol",
                        selected = currentSearchType.value == SearchType.SYMBOL
                    ) {
                        currentSearchType.value = SearchType.SYMBOL
                    }
                }

                Spacer(Modifier.height(10.dp))

                TextField(
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                    },
                    modifier = Modifier.focusRequester(focusRequester)
                )
            }
        },
        onDismissRequest = {
            showAlertDialog.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation(currentSearchType.value, text)
                    showAlertDialog.value = false
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    showAlertDialog.value = false
                }
            ) {
                Text("Cancel")
            }
        }
    )
}
