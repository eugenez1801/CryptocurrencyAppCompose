package com.example.cryptocurrencyappcompose.presentation.coin_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencyappcompose.common.Resource
import com.example.cryptocurrencyappcompose.common.SearchType
import com.example.cryptocurrencyappcompose.domain.model.Coin
import com.example.cryptocurrencyappcompose.domain.use_case.auth.SignOutUserUseCase
import com.example.cryptocurrencyappcompose.domain.use_case.get_coins.GetCoinsUseCase
import com.example.cryptocurrencyappcompose.domain.use_case.get_coins.ResultGetCoinsUseCase
import com.example.cryptocurrencyappcompose.presentation.auth.AuthState
import com.example.cryptocurrencyappcompose.presentation.coin_list.dialogs.SearchStatusBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val signOutUserUseCase: SignOutUserUseCase,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _state = mutableStateOf(CoinListState())
    val state: State<CoinListState> = _state

    val showSearchDialog = mutableStateOf(false)

    val searchStatusBarState = mutableStateOf(SearchStatusBar())//для сохранения после поворота

    val backToStartState = mutableStateOf(false)

    val loadedListOfCoins = mutableStateOf<List<Coin>?>(null)


    //для диалога состояния
    val textInTextFieldState = mutableStateOf("")

    val currentSearchTypeState =  mutableStateOf(SearchType.NAME)

    val focusRequesterState = FocusRequester()


    val isGreetingShown = mutableStateOf(false)
    fun isGreeting(){
        isGreetingShown.value = true
    }
    val currentUser = mutableStateOf<FirebaseUser?>(auth.currentUser)

    suspend fun signOut(): AuthState {
        return signOutUserUseCase()
    }

    init {
        getCoins()
        auth.addAuthStateListener { firebaseAuth ->
            currentUser.value = firebaseAuth.currentUser
        }
    }

    fun getCoins(request: String? = null){
        if (request != null) backToStartState.value = true
        else {
            backToStartState.value = false
            searchStatusBarState.value = SearchStatusBar()//прячем этот бар после нажатия возврата к начальному списку
        }

        getCoinsUseCase(request, searchStatusBarState.value.searchType,
            loadedListOfCoins.value).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = CoinListState(
                        result = result.data ?: ResultGetCoinsUseCase()
                    )
                    if (loadedListOfCoins.value.isNullOrEmpty()) loadedListOfCoins.value =
                        result.data?.listCoins
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

    fun getCoinsAfterRefresh(request: String? = null){
        if (request != null) backToStartState.value = true
        else {
            backToStartState.value = false
            searchStatusBarState.value = SearchStatusBar()//прячем этот бар после нажатия возврата к начальному списку
        }
        getCoinsUseCase(
            request = request,
            searchType = searchStatusBarState.value.searchType,
            needRefresh = true
        ).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = CoinListState(
                        result = result.data!!
                    )
                    loadedListOfCoins.value = result.data.refreshedListCoins
                    if (request != null) backToStartState.value = true//возвращаем кнопку после Loading
                }

                is Resource.Error -> {
                    _state.value = CoinListState(
                        error = result.message ?: "Unknown error"
                    )
                    if (request != null) backToStartState.value = true//возвращаем кнопку после Loading
                }

                is Resource.Loading -> {
                    _state.value = CoinListState(
                        isLoading = true
                    )
                    backToStartState.value = false//чтобы во время Loading этой кнопки не было
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updateSearchStatusBar(searchType: SearchType?, string: String?) {
        searchStatusBarState.value = SearchStatusBar(
            searchType = searchType,
            enteredText = string
        )
    }

    fun dialogIsShown(){
        showSearchDialog.value = true
    }

    fun updateDialogsState(text: String? = null, searchType: SearchType? = null){
        if (text != null){
            textInTextFieldState.value = text
        }
        else currentSearchTypeState.value = searchType!!
    }
}