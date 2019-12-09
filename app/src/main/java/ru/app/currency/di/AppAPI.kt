package ru.app.currency.di

import ru.app.domain.repository.IAmountRepository
import ru.app.domain.repository.ICurrencyRepository

interface AppAPI {
    fun amountRepository(): IAmountRepository
    fun currencyRepository(): ICurrencyRepository
}