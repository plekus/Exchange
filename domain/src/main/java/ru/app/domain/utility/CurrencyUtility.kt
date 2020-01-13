package ru.app.domain.utility

import ru.app.domain.model.Currency
import ru.app.domain.model.CurrencyDetails
import java.util.*
/**
 * comment 2
 * */
class CurrencyUtility {

    fun getCurrenciesDetails(baseCurrencyDetails: CurrencyDetails): List<CurrencyDetails> {
        return baseCurrencyDetails
            .rates
            .asSequence()
            .map { entry ->
                val rates: Map<Currency, Float> = baseCurrencyDetails.rates
                    .mapValues {
                        val longRate = it.value/entry.value
                        longRate.format2decimals().toFloat()
                    }
                CurrencyDetails(entry.key, rates)
            }
            .toList()
    }

}