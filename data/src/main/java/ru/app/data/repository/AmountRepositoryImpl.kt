package ru.app.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import ru.app.domain.model.Currency
import ru.app.domain.repository.IAmountRepository
import ru.app.domain.utility.format2decimals

private const val NAME = "available_amount"

class AmountRepositoryImpl(context: Context): IAmountRepository {

    private val preferences = context.getSharedPreferences(NAME, MODE_PRIVATE)

    override fun getAvailableAmount(currency: Currency): Float {
        return preferences.getFloat(currency.title, 100f)
    }

    override fun setAmount(currency: Currency, amount: Float) {
        preferences.edit()
            .putFloat(currency.title, amount.format2decimals().toFloat())
            .apply()
    }

    override fun increaseAmount(currency: Currency, amount: Float) {
        val available = getAvailableAmount(currency)
        setAmount(currency, available+amount)
    }

    override fun decreaseAmount(currency: Currency, amount: Float) {
        val available = getAvailableAmount(currency)
        setAmount(currency, available-amount)
    }

    override fun clear() {
        preferences.edit()
            .clear()
            .apply()
    }
}