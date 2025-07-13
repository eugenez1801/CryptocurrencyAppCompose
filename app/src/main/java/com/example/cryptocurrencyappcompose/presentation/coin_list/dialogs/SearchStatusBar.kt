package com.example.cryptocurrencyappcompose.presentation.coin_list.dialogs

import com.example.cryptocurrencyappcompose.common.SearchType

data class SearchStatusBar(
    val enteredText: String? = null,
    val searchType: SearchType? = null
)