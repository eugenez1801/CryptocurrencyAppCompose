package com.example.cryptocurrencyappcompose.domain.model

import com.google.gson.annotations.SerializedName

data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    val rank: Int,
    val isActive: Boolean,
)