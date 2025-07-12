package com.example.cryptocurrencyappcompose.presentation.coin_detail

import com.example.cryptocurrencyappcompose.domain.model.CoinDetail

data class CoinDetailState(
    val isLoading: Boolean = false,
    val coin: CoinDetail? = null,
    val error: String = ""
)