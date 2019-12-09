package ru.app.domain.repository

import ru.app.domain.model.Currency

interface IAmountRepository {
    fun getAvailableAmount(currency: Currency): Float
    fun setAmount(currency: Currency, amount: Float)
    fun increaseAmount(currency: Currency, amount: Float)
    fun decreaseAmount(currency: Currency, amount: Float)
    fun clear()
}