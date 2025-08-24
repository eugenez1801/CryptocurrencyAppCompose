package com.example.cryptocurrencyappcompose.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cryptocurrencyappcompose.presentation.auth.AuthScreen
import com.example.cryptocurrencyappcompose.presentation.coin_detail.CoinDetailScreen
import com.example.cryptocurrencyappcompose.presentation.coin_detail.CoinDetailViewModel
import com.example.cryptocurrencyappcompose.presentation.coin_list.CoinListScreen
import com.example.cryptocurrencyappcompose.presentation.coin_list.CoinListViewModel
import com.example.cryptocurrencyappcompose.presentation.auth.AuthViewModel
import com.example.cryptocurrencyappcompose.presentation.ui.theme.CryptocurrencyAppComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            CryptocurrencyAppComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.AuthScreen.route
                    ) {
                        composable(
                            route = Screen.CoinListScreen.route
                        ) {
                            val viewModel = hiltViewModel<CoinListViewModel>()

                            CoinListScreen(navController)
                        }

                        composable(
                            route = Screen.CoinDetailScreen.route +
                            "/{coinId}"
                        ) {
                            val viewModel = hiltViewModel<CoinDetailViewModel>()
                            CoinDetailScreen()
                        }

                        composable(
                            route = Screen.AuthScreen.route
                        ) {
                            val viewModel = hiltViewModel<AuthViewModel>()
                            if (viewModel.currentUser.value != null) {
                                LaunchedEffect(Unit) {
                                    navController.navigate(Screen.CoinListScreen.route){
                                        popUpTo(Screen.AuthScreen.route){
                                            inclusive = true
                                        }
                                    }
//                                    viewModel.currentUser() = null
                                }
                            }
                            else AuthScreen(navController, viewModel)
                        }
                    }
                }
            }
        }
    }
}