package ru.app.currency.presentation.pages.main

import io.reactivex.disposables.CompositeDisposable
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.app.domain.model.Currency
import ru.app.domain.model.CurrencyDetails
import ru.app.domain.repository.IAmountRepository
import ru.app.domain.repository.ICurrencyRepository
import ru.app.domain.rx.IRxSchedulers
import javax.inject.Inject

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun updateAdapters(items: List<CurrencyDetails>)
    fun showNotEnoughAmount()
    fun onConvertSuccess(fromCurrency: Currency, toCurrency: Currency, amount: Float)
}

@InjectViewState
class MainPresenter @Inject constructor(
    private val currencyRepository: ICurrencyRepository,
    private val amountRepository: IAmountRepository,
    private val rxSchedulers: IRxSchedulers
) : MvpPresenter<MainView>() {

    private val compositeDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        compositeDisposable.add(
            currencyRepository.subscribeToCurrenciesRates()
                .subscribeOn(rxSchedulers.io)
                .observeOn(rxSchedulers.ui)
                .subscribe({
                    viewState.updateAdapters(it)
                }, {
                    it.printStackTrace()
                })
        )
    }

    fun getAvailableAmount(currency: Currency): Float = amountRepository.getAvailableAmount(currency)

    fun convert(details: CurrencyDetails?, currency: Currency?, fromAmount: Float?) {
        details ?: return
        currency ?: return
        fromAmount ?: return
        if (details.currency == currency) return
        val rate = details.rates[currency] ?: return
        if (getAvailableAmount(details.currency) < fromAmount) {
            viewState.showNotEnoughAmount()
            return
        }
        val result = rate * fromAmount
        amountRepository.increaseAmount(currency, result)
        amountRepository.decreaseAmount(details.currency, fromAmount)
        viewState.onConvertSuccess(details.currency, currency, result)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}