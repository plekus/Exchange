package ru.app.data

import org.junit.jupiter.api.Assertions
import org.spekframework.spek2.Spek
import ru.app.domain.model.Currency.*
import ru.app.domain.model.CurrencyDetails
import ru.app.domain.utility.CurrencyUtility


object CurrencyUtilityTest: Spek({

    group("test CurrencyUtility") {

        val baseRates = mapOf(
            US_DOLLAR to 1.5f,
            BRITISH_POUND to 2f,
            EURO to 1.0f
        )
        val details = CurrencyUtility().getCurrenciesDetails(CurrencyDetails(EURO, baseRates))

        test("details has all three currencies") {
            Assertions.assertEquals(3, details.size)
        }

        test("dollar rate to dollar is 1.0f") {
            Assertions.assertEquals(1.0f, details.first { it.currency == US_DOLLAR }.rates[US_DOLLAR])
        }

        test("dollar rate to pound is 1.33f") {
            Assertions.assertEquals(1.33f, details.first { it.currency == US_DOLLAR }.rates[BRITISH_POUND])
        }

        test("pound rate to dollar is 0.75f") {
            Assertions.assertEquals(0.75f, details.first { it.currency == BRITISH_POUND }.rates[US_DOLLAR])
        }

    }

})
