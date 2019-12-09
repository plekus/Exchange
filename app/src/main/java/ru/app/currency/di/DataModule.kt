package ru.app.currency.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.app.data.network.ExchangeRatesAPI
import ru.app.data.repository.AmountRepositoryImpl
import ru.app.data.repository.CurrencyRepositoryImpl
import ru.app.domain.repository.IAmountRepository
import ru.app.domain.repository.ICurrencyRepository
import ru.app.domain.utility.CurrencyUtility
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideCurrencyRepository(api: ExchangeRatesAPI, currencyUtility: CurrencyUtility): ICurrencyRepository {
        return CurrencyRepositoryImpl(api, currencyUtility)
    }

    @Provides
    @Singleton
    fun provideAmountRepository(context: Context): IAmountRepository {
        return AmountRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideCurrencyUtility(): CurrencyUtility = CurrencyUtility()

}