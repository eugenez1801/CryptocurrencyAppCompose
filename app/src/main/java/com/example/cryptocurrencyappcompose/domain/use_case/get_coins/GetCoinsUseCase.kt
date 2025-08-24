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
//            Log.d("RefreshCheck", "Use case начался: request = $request," +
//                    "searchType = $searchType, loadedList = $loadedList, needRefresh = $needRefresh")
            emit(Resource.Loading())
            var listOfCoins: List<Coin>? = null

            if (needRefresh){
//                Log.d("RefreshCheck", "Первый If сработал. Обращение к репозиторию")
                listOfCoins = repository.getCoins().map { it.toCoin() }
                if (request == null){
//                    Log.d("RefreshCheck", "request == null")
                    emit(Resource.Success(ResultGetCoinsUseCase(listOfCoins, refreshedListCoins = listOfCoins)))
                }

                else if (request != null){
//                    Log.d("RefreshCheck", "Второй If сработал")
                    emit(Resource.Success(filterForSearch(request, searchType!!, listOfCoins, true)))
                }
            }

            else if (loadedList == null && request == null){ //по хорошему это должно быть вызвано один раз
//                Log.d("LoadedListCheck", "Обращение к репозиторию. Первый if")
                listOfCoins = repository.getCoins().map { it.toCoin() }

                emit(Resource.Success(ResultGetCoinsUseCase(listOfCoins)))
            }

            else if (request != null && loadedList != null){//обычный запрос поиска
//                Log.d("LoadedListCheck", "Второй if")
                emit(Resource.Success(filterForSearch(request, searchType!!, loadedList)))
            }

            else if (request != null && loadedList == null){
//                Log.d("LoadedListCheck", "Третий if")
                listOfCoins = repository.getCoins().map { it.toCoin() }
                emit(Resource.Success(filterForSearch(request, searchType!!, listOfCoins)))
            }

            else if (request == null && loadedList != null){//чисто для случая при backToStartState == true
//                Log.d("LoadedListCheck", "Четвертый if")
                emit(Resource.Success(ResultGetCoinsUseCase(listCoins = loadedList)))
            }

            else if (listOfCoins != null) {//самая первая загрузка
//                Log.d("LoadedListCheck", "Шестой if")
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

    /*private fun coinDtoToCoin(listDto: List<CoinDto>): ResultGetCoinsUseCase{
        return ResultGetCoinsUseCase(
            listDto.map {
                it.toCoin()
            }
        )
    }*/

    private fun filterForSearch(stringForSearch: String, typeOfSearchType: SearchType, listCoin: List<Coin>,
                                needRefreshedList: Boolean = false): ResultGetCoinsUseCase{
//        Log.d("EmptyField", "Before throw")
//        if (stringForSearch.isBlank()) throw ResultListException("The search field should not be empty.")
//        Log.d("EmptyField", "After throw")

        val startWithList: MutableList<Coin> = mutableListOf()//список для коинов, начинающихся с этих букв
        val filteredList: MutableList<Coin> = mutableListOf()
        var accurateCoin: Coin? = null //для точного попадания
        val lowerStringForSearch = stringForSearch.lowercase().trim()//нижнее не учитывает пробелы в начале
//        if (lowerStringForSearch.last() == ' ') lowerStringForSearch = lowerStringForSearch.dropLast(1)

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

//        Log.d("SearchCheck", "$startWithList")
        var refreshedListForResult = emptyList<Coin>()
        if (needRefreshedList) refreshedListForResult = listCoin
//        Log.d("LoadedListCheck", "refreshedListForResult = ${refreshedListForResult.size}")
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