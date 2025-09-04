package com.example.cryptocurrencyappcompose.presentation

import kotlinx.serialization.Serializable

sealed class Screen() {
    @Serializable
    object AuthScreen: Screen()

    @Serializable
    object CoinListScreen: Screen()

    @Serializable
    data class CoinDetailScreen(val coinId: String): Screen()
}