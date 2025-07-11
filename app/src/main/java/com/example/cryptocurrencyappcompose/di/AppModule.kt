package com.example.cryptocurrencyappcompose.di

import com.example.cryptocurrencyappcompose.common.Constants
import com.example.cryptocurrencyappcompose.data.remote.CoinPaprikaApi
import com.example.cryptocurrencyappcompose.data.repository.CoinRepositoryImpl
import com.example.cryptocurrencyappcompose.domain.repository.CoinRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePaprikaApi(): CoinPaprikaApi{
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofitBuilder.create(CoinPaprikaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: CoinPaprikaApi): CoinRepository{
        return CoinRepositoryImpl(api)
    }
}