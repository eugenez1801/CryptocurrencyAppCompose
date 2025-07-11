package com.example.cryptocurrencyappcompose.presentation.coin_list

import com.example.cryptocurrencyappcompose.domain.model.Coin

data class CoinListState(
    val isLoading: Boolean = false,
    val coins: List<Coin> = emptyList(),
    val error: String = ""
)
