package ru.app.data.network

import io.reactivex.Single
import retrofit2.http.GET
import ru.app.data.network.response.RatesResponse

interface ExchangeRatesAPI {

    @GET("latest?symbols=USD,GBP,RON&base=EUR")
    fun getRates(): Single<RatesResponse>

}