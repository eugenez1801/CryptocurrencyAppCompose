package com.example.cryptocurrencyappcompose.domain.repository

import com.example.cryptocurrencyappcompose.data.remote.dto.CoinDetailDto
import com.example.cryptocurrencyappcompose.data.remote.dto.CoinDto

interface CoinRepository {

    suspend fun getCoins(): List<CoinDto>

    suspend fun getCoinById(coinId: String): CoinDetailDto
}