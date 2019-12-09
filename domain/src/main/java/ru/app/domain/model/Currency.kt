package ru.app.domain.model

import java.io.Serializable

class CurrencyDetails(
    val currency: Currency,
    val rates: Map<Currency, Float>
): Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CurrencyDetails

        if (currency != other.currency) return false
        if (rates != other.rates) return false

        return true
    }

    override fun hashCode(): Int {
        var result = currency.hashCode()
        result = 31 * result + rates.hashCode()
        return result
    }
}

enum class Currency(val title: String, val icon: String) {
    EURO("EUR", "€"),
    BRITISH_POUND("GBP","£"),
    US_DOLLAR("USD","$"),
    ROMANIAN_LEU("RON","L")
}