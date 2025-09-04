package com.example.cryptocurrencyappcompose.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
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
        enableEdgeToEdge()
        setContent {
            CryptocurrencyAppComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.systemBars) { innerPadding ->
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.AuthScreen,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<Screen.CoinListScreen>{
                            val viewModel = hiltViewModel<CoinListViewModel>()

                            CoinListScreen(navController)
                        }

                        composable<Screen.CoinDetailScreen> {
                            val viewModel = hiltViewModel<CoinDetailViewModel>()
                            CoinDetailScreen()
                        }

                        composable<Screen.AuthScreen> {
                            val viewModel = hiltViewModel<AuthViewModel>()
                            if (viewModel.currentUser.value != null) {
                                LaunchedEffect(Unit) {
                                    navController.navigate(Screen.CoinListScreen){
                                        popUpTo(Screen.AuthScreen){
                                            inclusive = true
                                        }
                                    }
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