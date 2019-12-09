package ru.app.currency.presentation.pages

import ru.app.domain.model.CurrencyDetails

interface IFragmentListener {
    fun onShow()
    fun onHide()
    fun updateDetails(newDetails: CurrencyDetails)
    fun updateAmount(string: String)
    fun notifyChange()
    fun getEnteredText(): String?
    fun onConvertSuccess()
}