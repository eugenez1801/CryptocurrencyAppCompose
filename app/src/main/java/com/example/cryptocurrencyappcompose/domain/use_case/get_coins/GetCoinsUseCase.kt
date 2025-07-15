package com.example.cryptocurrencyappcompose.domain.use_case.get_coins

import com.example.cryptocurrencyappcompose.common.Resource
import com.example.cryptocurrencyappcompose.common.SearchType
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

    operator fun invoke(name: String? = null, symbol: String? = null): Flow<Resource<ResultGetCoinsUseCase>> = flow {
        try {
//            Log.d("SearchCheck", "$name, $symbol")
            emit(Resource.Loading())
            val listOfCoinsDto = repository.getCoins()

            if (name != null){
//                Log.d("SearchCheck", "If сработал")
                emit(Resource.Success(filterForSearch(name, SearchType.NAME, listOfCoinsDto)))
            }

            else if (symbol != null){
                emit(Resource.Success(filterForSearch(symbol, SearchType.SYMBOL, listOfCoinsDto)))
            }

            else {
                emit(Resource.Success(coinDtoToCoin(listOfCoinsDto)))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException){
            emit(Resource.Error("Could't reach server. Check your internet connection"))
        } catch (e: ResultListException){
            emit(Resource.Error(e.message!!))
        }
    }

    private fun coinDtoToCoin(listDto: List<CoinDto>): ResultGetCoinsUseCase{
        return ResultGetCoinsUseCase(
            listDto.map {
                it.toCoin()
            }
        )
    }

    private fun filterForSearch(stringForSearch: String, typeOfSearchType: SearchType, listDto: List<CoinDto>): ResultGetCoinsUseCase{
//        Log.d("EmptyField", "Before throw")
        if (stringForSearch.isBlank()) throw ResultListException("The search field should not be empty.")
//        Log.d("EmptyField", "After throw")

        val startWithList: MutableList<CoinDto> = mutableListOf()//список для коинов, начинающихся с этих букв
        val filteredList: MutableList<CoinDto> = mutableListOf()
        var accurateCoin: CoinDto? = null //для точного попадания
        val lowerStringForSearch = stringForSearch.lowercase().trim()//нижнее не учитывает пробелы в начале
//        if (lowerStringForSearch.last() == ' ') lowerStringForSearch = lowerStringForSearch.dropLast(1)

        when(typeOfSearchType){
            SearchType.NAME -> listDto.forEach { coinDto ->
                val coinName = coinDto.name.lowercase()
                if (coinName == lowerStringForSearch && accurateCoin == null) accurateCoin = coinDto
                else if (coinName.startsWith(lowerStringForSearch))
                    startWithList.add(coinDto)
                else if (lowerStringForSearch in coinName)
                    filteredList.add(coinDto)
            }
            SearchType.SYMBOL -> listDto.forEach { coinDto ->
                val coinSymbol = coinDto.symbol.lowercase()
                if (coinSymbol == lowerStringForSearch && accurateCoin == null) accurateCoin = coinDto
                else if (coinSymbol.startsWith(lowerStringForSearch))
                    startWithList.add(coinDto)
                else if (lowerStringForSearch in coinSymbol)
                    filteredList.add(coinDto)
            }
        }

//        Log.d("SearchCheck", "$startWithList")
        val listResult: List<Coin>
        if (accurateCoin != null) {
            listResult = (listOf(accurateCoin!!) + startWithList + filteredList).map { it.toCoin() }
            val indexOfLastStartWithListElement = startWithList.size
            if (listResult.isEmpty()) throw ResultListException("Result list is empty.")
            return ResultGetCoinsUseCase(listResult, true,
                indexOfLastStartWithListElement)
        }
        else {
            listResult = (startWithList + filteredList).map { it.toCoin() }
            val indexOfLastStartWithListElement = startWithList.size - 1
            if (listResult.isEmpty()) throw ResultListException("Result list is empty.")
            return ResultGetCoinsUseCase(listResult,
                indexOfLastStartWithListElement = indexOfLastStartWithListElement)
        }
    }
}

class ResultListException(message: String): Exception(message)