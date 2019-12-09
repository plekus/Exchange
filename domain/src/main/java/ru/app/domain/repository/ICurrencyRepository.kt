package ru.app.domain.repository

import io.reactivex.Observable
import ru.app.domain.model.CurrencyDetails

interface ICurrencyRepository {
    fun subscribeToCurrenciesRates(): Observable<List<CurrencyDetails>>
}