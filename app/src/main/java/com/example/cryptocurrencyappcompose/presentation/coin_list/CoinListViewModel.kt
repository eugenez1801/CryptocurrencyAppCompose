package com.example.cryptocurrencyappcompose.presentation.coin_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencyappcompose.common.Resource
import com.example.cryptocurrencyappcompose.common.SearchType
import com.example.cryptocurrencyappcompose.domain.use_case.get_coins.GetCoinsUseCase
import com.example.cryptocurrencyappcompose.presentation.coin_list.dialogs.SearchStatusBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase
): ViewModel() {

    private val _state = mutableStateOf(CoinListState())//оно приватное и изменяемое, чтобы его изменять могла только вьюмодель
    val state: State<CoinListState> = _state//он же открыт для composable, которые не могут его изменять, поскольку это не MutableState

    val showSearchDialog = mutableStateOf(false)

    val searchStatusBarState = mutableStateOf(SearchStatusBar())//для сохранения после поворота

    //для диалога состояния
    val textInTextFieldState = mutableStateOf("")

    val currentSearchTypeState =  mutableStateOf(SearchType.NAME)

    val focusRequesterState = FocusRequester()

    init {
        getCoins()
    }

    fun getCoins(name: String? = null, symbol: String? = null){
        getCoinsUseCase(name, symbol).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = CoinListState(
                        coins = result.data ?: emptyList(),
                    )
                }
                is Resource.Error -> {
                    _state.value = CoinListState(
                        error = result.message ?: "Unknown error"
                    )
                }
                is Resource.Loading -> {
                    _state.value = CoinListState(
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updateSearchStatusBar(searchType: SearchType, string: String) {
        searchStatusBarState.value = SearchStatusBar(
            searchType = searchType,
            enteredText = string
        )
    }

    fun dialogIsShown(){
        showSearchDialog.value = true
    }

    fun updateDialogsState(text: String? = null, searchType: SearchType? = null){//метод чисто для диалога
        if (text != null){
            textInTextFieldState.value = text
        }
        else currentSearchTypeState.value = searchType!!
    }
}