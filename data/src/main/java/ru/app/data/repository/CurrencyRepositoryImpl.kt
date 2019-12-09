package ru.app.data.repository

import io.reactivex.Observable
import ru.app.data.network.ExchangeRatesAPI
import ru.app.domain.model.CurrencyDetails
import ru.app.domain.repository.ICurrencyRepository
import ru.app.domain.utility.CurrencyUtility
import java.util.concurrent.TimeUnit

class CurrencyRepositoryImpl(
    private val api: ExchangeRatesAPI,
    private val currencyUtility: CurrencyUtility
): ICurrencyRepository {

    private val WAIT_TIME_MS = 30_000L

    override fun subscribeToCurrenciesRates(): Observable<List<CurrencyDetails>> {
        return Observable.interval(0, WAIT_TIME_MS, TimeUnit.MILLISECONDS)
            .flatMap {
                api.getRates()
                    .map { it.toBaseCurrencyDetails() }
                    .toObservable()
            }
            .map { currencyUtility.getCurrenciesDetails(it) }
    }
}