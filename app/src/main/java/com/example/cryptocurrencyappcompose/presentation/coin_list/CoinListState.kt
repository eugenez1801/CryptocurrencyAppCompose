package com.example.cryptocurrencyappcompose.presentation.coin_list

import com.example.cryptocurrencyappcompose.domain.use_case.get_coins.ResultGetCoinsUseCase

data class CoinListState(
    val isLoading: Boolean = false,
    val result: ResultGetCoinsUseCase = ResultGetCoinsUseCase(),
    val error: String = ""
)