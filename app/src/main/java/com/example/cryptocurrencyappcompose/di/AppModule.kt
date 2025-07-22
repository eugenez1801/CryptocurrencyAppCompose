package com.example.cryptocurrencyappcompose.di

import com.example.cryptocurrencyappcompose.common.Constants
import com.example.cryptocurrencyappcompose.data.remote.CoinPaprikaApi
import com.example.cryptocurrencyappcompose.data.repository.CoinRepositoryImpl
import com.example.cryptocurrencyappcompose.data.repository.FirebaseRepositoryImpl
import com.example.cryptocurrencyappcompose.domain.repository.CoinRepository
import com.example.cryptocurrencyappcompose.domain.repository.FirebaseRepository
import com.example.cryptocurrencyappcompose.domain.use_case.sign_up.SignInUseCase
import com.example.cryptocurrencyappcompose.domain.use_case.sign_up.SignUpUseCase
import com.example.cryptocurrencyappcompose.presentation.sign_up.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth
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
    fun provideCoinRepository(api: CoinPaprikaApi): CoinRepository{
        return CoinRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(auth: FirebaseAuth): FirebaseRepository{
        return FirebaseRepositoryImpl(auth)
    }

    @Provides
    @Singleton
    fun provideSignUpUseCase(repository: FirebaseRepository): SignUpUseCase{
        return SignUpUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSignInUseCase(repository: FirebaseRepository): SignInUseCase {
        return SignInUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSignUpViewModel(signUpUseCase: SignUpUseCase, signInUseCase: SignInUseCase): SignUpViewModel{
        return SignUpViewModel(signUpUseCase, signInUseCase)
    }
}