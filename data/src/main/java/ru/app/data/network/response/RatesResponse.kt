package ru.app.data.network.response

import com.google.gson.annotations.SerializedName
import ru.app.domain.model.Currency.*
import ru.app.domain.model.CurrencyDetails

data class RatesResponse(
    @SerializedName("rates") val rates: Rates
) {
    fun toBaseCurrencyDetails(): CurrencyDetails {
        val baseRates = mapOf(
            US_DOLLAR to rates.usd,
            ROMANIAN_LEU to rates.ron,
            BRITISH_POUND to rates.gbp,
            EURO to 1.0f
        )
        return CurrencyDetails(EURO, baseRates)
    }
}

data class Rates(
    @SerializedName("USD") val usd: Float,
    @SerializedName("RON") val ron: Float,
    @SerializedName("GBP") val gbp: Float
)