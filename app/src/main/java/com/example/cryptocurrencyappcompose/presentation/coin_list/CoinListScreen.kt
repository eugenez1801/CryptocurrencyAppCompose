package com.example.cryptocurrencyappcompose.presentation.coin_list

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cryptocurrencyappcompose.presentation.Screen
import com.example.cryptocurrencyappcompose.presentation.coin_list.components.CoinListItem
import com.example.cryptocurrencyappcompose.presentation.coin_list.dialogs.search_dialog.SearchDialog
import kotlinx.coroutines.launch

@Composable
fun CoinListScreen(
    navController: NavController,
    viewModel: CoinListViewModel = viewModel()
) {
    val state = viewModel.state.value

    val showSearchDialog = viewModel.showSearchDialog.value

    val searchStatusBar = viewModel.searchStatusBarState.value

    val scope = rememberCoroutineScope()

    val backToStart = viewModel.backToStartState.value

    val context = LocalContext.current
    val currentUser = viewModel.currentUser.value
    val isGreetingShown = viewModel.isGreetingShown.value
    LaunchedEffect(currentUser) {
        if (currentUser != null && !isGreetingShown){
            Toast.makeText(context, "Hello, ${viewModel.currentUser.value?.displayName}",
                Toast.LENGTH_SHORT).show()
            viewModel.isGreeting()
        }
    }

    Box(Modifier.fillMaxSize()) {
        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (searchStatusBar.enteredText != null){
                    Column(
                        modifier = Modifier
                            .padding(top = 5.dp, start = 20.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = "You entered: ${searchStatusBar.enteredText!!}"
                        )
                        Text(
                            text = "Type search: ${searchStatusBar.searchType.toString()}"
                        )
                    }
                }else Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 10.dp, top = 5.dp)
                ){
                    IconButton(
                        onClick = {
                            scope.launch {
                                viewModel.signOut()
                                navController.navigate(Screen.AuthScreen)
                            }
                        },
                        modifier = Modifier
                            .padding(end = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "refresh",
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            viewModel.getCoinsAfterRefresh(searchStatusBar.enteredText)
                        },
                        modifier = Modifier
                            .padding(end = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "refresh",
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            viewModel.dialogIsShown()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "search",
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
            }

            HorizontalDivider(thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth(0.90f)
                    .align(Alignment.CenterHorizontally))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = if (backToStart) 40.dp else 0.dp),
            ) {
                items(state.result.listCoins.size) { coinId ->
                    CoinListItem(
                        coin = state.result.listCoins[coinId],
                        onItemClick = {
                            navController.navigate(
                                Screen.CoinDetailScreen(
                                coinId = state.result.listCoins[coinId].id
                            ))
                        },
                        isAccurateCoin = if (coinId == 0) state.result.accurateCoinExists
                        else false
                    )

                    if (state.result.accurateCoinExists && coinId == 0) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ){
                            HorizontalDivider(
                                thickness = 1.dp,
                                modifier = Modifier
                                    .fillMaxWidth(0.90f)
                                    .align(Alignment.Center)
                            )
                        }

                        Text(
                            text = "Coins starting with your input string",
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp)
                        )

                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ){
                            HorizontalDivider(
                                thickness = 1.dp,
                                modifier = Modifier
                                    .fillMaxWidth(0.90f)
                                    .align(Alignment.Center)
                            )
                        }
                    }

                    if (state.result.indexOfLastStartWithListElement == coinId){
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ){
                            HorizontalDivider(
                                thickness = 1.dp,
                                modifier = Modifier
                                    .fillMaxWidth(0.90f)
                                    .align(Alignment.Center)
                            )
                        }

                        Text(
                            text = "Coins containing your input string",
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp)
                        )

                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ){
                            HorizontalDivider(
                                thickness = 1.dp,
                                modifier = Modifier
                                    .fillMaxWidth(0.90f)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }

        if (backToStart){
            TextButton(
                onClick = {
                    viewModel.getCoins()
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = "Back to start list"
                )
            }
        }

        if (showSearchDialog){
            SearchDialog(
                showAlertDialog = viewModel.showSearchDialog,
                onConfirmation = { searchType, string ->
                    viewModel.updateSearchStatusBar(searchType, string)
                    viewModel.getCoins(string)
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

                if (state.error == "Result list is empty." ||
                    state.error == "The search field should not be empty.")
                {
                    TextButton(
                        onClick = {
                            viewModel.dialogIsShown()
                        }
                    ) {
                        Text(
                            text = "Go to search again"
                        )
                    }
                }
                else{
                    TextButton(
                        onClick = {
                            viewModel.getCoinsAfterRefresh(searchStatusBar.enteredText)
                        }
                    ) {
                        Text(
                            text = "Try again"
                        )
                    }

                    if (!viewModel.loadedListOfCoins.value.isNullOrEmpty()){
                        TextButton(
                            onClick = {
                                viewModel.getCoins()
                            }
                        ) {
                            Text(
                                text = "Back to start list of coins"
                            )
                        }
                    }
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}