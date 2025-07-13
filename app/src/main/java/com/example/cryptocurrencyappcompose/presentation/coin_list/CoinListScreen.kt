package com.example.cryptocurrencyappcompose.presentation.coin_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cryptocurrencyappcompose.common.SearchType
import com.example.cryptocurrencyappcompose.presentation.Screen
import com.example.cryptocurrencyappcompose.presentation.coin_list.components.CoinListItem
import com.example.cryptocurrencyappcompose.presentation.coin_list.dialogs.search_dialog.SearchDialog

@Composable
fun CoinListScreen(
    navController: NavController,
    viewModel: CoinListViewModel = viewModel()
) {
    val state = viewModel.state.value

    val showSearchDialog = remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        Column(

        ) {
            IconButton(
                onClick = {
                    showSearchDialog.value = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search"
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.coins) { coin ->
                    CoinListItem(
                        coin = coin,
                        onItemClick = {
                            navController.navigate(
                                Screen.CoinDetailScreen.route +
                                        "/${coin.id}"
                            )
                        }
                    )
                }
            }
        }

        if (showSearchDialog.value){
            SearchDialog(
                showAlertDialog = showSearchDialog,
                onConfirmation = { searchType, string ->
                    when (searchType) {
                        SearchType.NAME -> {
                            viewModel.getCoins(name = string)
                        }

                        SearchType.SYMBOL -> {
                            viewModel.getCoins(symbol = string)
                        }
                    }
                }
            )
        }

        if (state.error.isNotBlank()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )

                TextButton(
                    onClick = {
                        viewModel.getCoins()
                    }
                ) {
                    Text(
                        text = "Try again"
                    )
                }
            }
        }
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}