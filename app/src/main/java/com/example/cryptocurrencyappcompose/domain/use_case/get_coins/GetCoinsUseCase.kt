package com.example.cryptocurrencyappcompose.domain.use_case.get_coins

import com.example.cryptocurrencyappcompose.common.Resource
import com.example.cryptocurrencyappcompose.data.remote.dto.CoinDto
import com.example.cryptocurrencyappcompose.data.remote.dto.toCoin
import com.example.cryptocurrencyappcompose.domain.model.Coin
import com.example.cryptocurrencyappcompose.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
) {

    operator fun invoke(): Flow<Resource<List<Coin>>> = flow {
        try {
            emit(Resource.Loading())
            val coins = repository.getCoins()
            emit(Resource.Success(coinDtoToCoin(coins)))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException){
            emit(Resource.Error("Could't reach server. Check your internet connection"))
        }
    }

    private fun coinDtoToCoin(listDto: List<CoinDto>): List<Coin>{
        return listDto.map {
            it.toCoin()
        }
    }
}