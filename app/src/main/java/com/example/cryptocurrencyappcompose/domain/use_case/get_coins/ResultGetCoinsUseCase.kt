package com.example.cryptocurrencyappcompose.domain.use_case.get_coins

import com.example.cryptocurrencyappcompose.domain.model.Coin

data class ResultGetCoinsUseCase(
    val listCoins: List<Coin> = emptyList(),
    val accurateCoinExists: Boolean = false,
    val indexOfLastStartWithListElement: Int? = null
)