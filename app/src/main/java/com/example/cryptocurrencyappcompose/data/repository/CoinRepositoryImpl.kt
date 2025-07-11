package com.example.cryptocurrencyappcompose.data.repository

import com.example.cryptocurrencyappcompose.data.remote.CoinPaprikaApi
import com.example.cryptocurrencyappcompose.data.remote.dto.CoinDetailDto
import com.example.cryptocurrencyappcompose.data.remote.dto.CoinDto
import com.example.cryptocurrencyappcompose.domain.repository.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinPaprikaApi
): CoinRepository {
    override suspend fun getCoins(): List<CoinDto> {
        return api.getCoins()
    }

    override suspend fun getCoinById(coinId: String): CoinDetailDto {
        return api.getCoinById(coinId)
    }
}