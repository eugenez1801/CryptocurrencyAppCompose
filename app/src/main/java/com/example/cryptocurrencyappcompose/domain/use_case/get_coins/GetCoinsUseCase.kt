package com.example.cryptocurrencyappcompose.domain.use_case.get_coins

import com.example.cryptocurrencyappcompose.common.Resource
import com.example.cryptocurrencyappcompose.common.SearchType
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

    operator fun invoke(request: String? = null, searchType: SearchType? = null, loadedList:
    List<Coin>? = null, needRefresh: Boolean = false): Flow<Resource<ResultGetCoinsUseCase>> = flow {
        try {
            emit(Resource.Loading())
            var listOfCoins: List<Coin>? = null

            if (needRefresh){
                listOfCoins = repository.getCoins().map { it.toCoin() }
                if (request == null){
                    emit(Resource.Success(ResultGetCoinsUseCase(listOfCoins, refreshedListCoins = listOfCoins)))
                }

                else if (request != null){
                    emit(Resource.Success(filterForSearch(request, searchType!!, listOfCoins, true)))
                }
            }

            else if (loadedList == null && request == null){
                listOfCoins = repository.getCoins().map { it.toCoin() }

                emit(Resource.Success(ResultGetCoinsUseCase(listOfCoins)))
            }

            else if (request != null && loadedList != null){//обычный запрос поиска
                emit(Resource.Success(filterForSearch(request, searchType!!, loadedList)))
            }

            else if (request != null && loadedList == null){
                listOfCoins = repository.getCoins().map { it.toCoin() }
                emit(Resource.Success(filterForSearch(request, searchType!!, listOfCoins)))
            }

            else if (request == null && loadedList != null){//чисто для случая при backToStartState == true
                emit(Resource.Success(ResultGetCoinsUseCase(listCoins = loadedList)))
            }

            else if (listOfCoins != null) {//самая первая загрузка
                emit(Resource.Success(ResultGetCoinsUseCase(listOfCoins)))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException){
            emit(Resource.Error("Could't reach server. Check your internet connection"))
        } catch (e: ResultListException){
            emit(Resource.Error(e.message!!))
        }
    }

    private fun filterForSearch(stringForSearch: String, typeOfSearchType: SearchType, listCoin: List<Coin>,
                                needRefreshedList: Boolean = false): ResultGetCoinsUseCase{
        val startWithList: MutableList<Coin> = mutableListOf()//список для коинов, начинающихся с этих букв
        val filteredList: MutableList<Coin> = mutableListOf()
        var accurateCoin: Coin? = null //для точного совпадения
        val lowerStringForSearch = stringForSearch.lowercase().trim()

        when(typeOfSearchType){
            SearchType.NAME -> listCoin.forEach { coinDto ->
                val coinName = coinDto.name.lowercase()
                if (coinName == lowerStringForSearch && accurateCoin == null) accurateCoin = coinDto
                else if (coinName.startsWith(lowerStringForSearch))
                    startWithList.add(coinDto)
                else if (lowerStringForSearch in coinName)
                    filteredList.add(coinDto)
            }
            SearchType.SYMBOL -> listCoin.forEach { coinDto ->
                val coinSymbol = coinDto.symbol.lowercase()
                if (coinSymbol == lowerStringForSearch && accurateCoin == null) accurateCoin = coinDto
                else if (coinSymbol.startsWith(lowerStringForSearch))
                    startWithList.add(coinDto)
                else if (lowerStringForSearch in coinSymbol)
                    filteredList.add(coinDto)
            }
        }

        var refreshedListForResult = emptyList<Coin>()
        if (needRefreshedList) refreshedListForResult = listCoin
        val listResult: List<Coin>
        if (accurateCoin != null) {
            listResult = listOf(accurateCoin!!) + startWithList + filteredList
            val indexOfLastStartWithListElement = startWithList.size
            if (listResult.isEmpty()) throw ResultListException("Result list is empty.")
            return ResultGetCoinsUseCase(listResult, true,
                indexOfLastStartWithListElement, refreshedListForResult)
        }
        else {
            listResult = startWithList + filteredList
            val indexOfLastStartWithListElement = startWithList.size - 1
            if (listResult.isEmpty()) throw ResultListException("Result list is empty.")
            return ResultGetCoinsUseCase(listResult,
                indexOfLastStartWithListElement = indexOfLastStartWithListElement,
                refreshedListCoins = refreshedListForResult)
        }
    }
}

class ResultListException(message: String): Exception(message)