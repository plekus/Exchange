package ru.app.currency.presentation.pages.main

import ru.app.domain.model.Currency

interface MainListener {

    fun getStartCurrencyRate(): String
    fun getEndCurrencyRate(): String

    fun getAvailableAmount(currency: Currency): String

    fun getAmountForStart(): String
    fun getAmountForEnd(): String

    fun onStartCurrencyEdited()
    fun onEndCurrencyEdited()
}